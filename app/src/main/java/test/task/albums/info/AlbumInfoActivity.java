package test.task.albums.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ponomareva.iuliia.albums.api.Album;
import ponomareva.iuliia.albums.HttpUtils;
import ponomareva.iuliia.albums.R;

public class AlbumInfoActivity extends AppCompatActivity implements InfoContract.View {
    public static final String ALBUM = "album";
    private Album album;
    @BindView(R.id.info)
    TextView infoView;
    @BindView(R.id.songs)
    TextView songsView;
    @BindView(R.id.artwork)
    ImageView imageView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    InfoContract.Presenter presenter;
    private SongsViewModel songsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);
        ButterKnife.bind(this);
        songsViewModel = ViewModelProviders.of(this).get(SongsViewModel.class);
        presenter = new InfoPresenter(new ModelImpl(), this);
        if (savedInstanceState != null) {
            album = savedInstanceState.getParcelable(ALBUM);
        } else {
            Intent intent = getIntent();
            album = intent.getParcelableExtra(ALBUM);
        }
        songsViewModel.getData(presenter, album).observe(this, new Observer<List<InfoContract.Song>>() {
            @Override
            public void onChanged(List<InfoContract.Song> songs) {
                if (songs != null) {
                    displayInTextView(songs);
                }
            }
        });
    }

    @Override
    public void displayAlbumInfo() {
        setTitle(album.getCollectionName());
        StringBuilder builder = new StringBuilder();
        builder.append(album.getCollectionName())
                .append("\n")
                .append(album.getArtistName());
        addIfNotEmpty(builder, album.getCountry());
        Integer trackCount = album.getTrackCount();
        if (trackCount != null) {
            builder.append("\n")
                    .append(getResources()
                            .getQuantityString(
                                    R.plurals.tracks_quantity,
                                    trackCount, trackCount));
        }
        if ((album.getCollectionPrice() != null) && (!TextUtils.isEmpty(album.getCurrency()))) {
            builder.append("\n")
                    .append(album.getCollectionPrice())
                    .append(" ")
                    .append(album.getCurrency());
        }
        if (!TextUtils.isEmpty(album.getReleaseDate())) {
            builder.append("\n");
            String dateToParse = album.getReleaseDate();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            try {
                Date pubDate = formatter.parse(dateToParse);
                builder.append(DateUtils.formatDateTime(this, pubDate.getTime(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
            } catch (ParseException e) {
                //e.printStackTrace();
                builder.append(dateToParse);
            }
        }
        addIfNotEmpty(builder, album.getPrimaryGenreName());
        addIfNotEmpty(builder, album.getContentAdvisoryRating());
        addIfNotEmpty(builder, album.getCopyright());
        infoView.setText(builder.toString());
    }

    private void addIfNotEmpty(StringBuilder builder, String text) {
        if (!TextUtils.isEmpty(text)) {
            builder.append("\n")
                    .append(text);
        }
    }

    @Override
    public void displayImage(String url) {
        Picasso.get().load(url).into(imageView);
        imageView.setContentDescription(getString(R.string.artwork, album.getCollectionName()));
    }

    @Override
    public void displaySongs(List<InfoContract.Song> songs) {
        songsViewModel.setData(songs);
        displayInTextView(songs);
    }

    private void displayInTextView(List<InfoContract.Song> songs) {
        StringBuilder builder = new StringBuilder();
        for (InfoContract.Song song : songs) {
            builder.append(song.getTrackNumber())
                    .append(". ")
                    .append(song.getTitle())
                    .append("\n");
        }
        songsView.setText(builder.toString());
    }

    @Override
    public void showProgress() {
        songsView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        songsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayError() {
        songsView.setText(R.string.error_loading_data);
    }

    @Override
    public void displayNoInternet() {
        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isConnectedToNetwork() {
        return HttpUtils.isConnectedToNetwork(getApplicationContext());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ALBUM, album);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

class SongsViewModel extends ViewModel {
    private MutableLiveData<List<InfoContract.Song>> songs;

    LiveData<List<InfoContract.Song>> getData(InfoContract.Presenter presenter, Album album) {
        if (songs == null) {
            songs = new MutableLiveData<>();
            presenter.needSongsForAlbum(album);
        } else {
            presenter.dontNeedSongsForAlbum(album);
        }
        return songs;
    }

    public void setData(List<InfoContract.Song> songs) {
        this.songs.setValue(songs);
    }
}