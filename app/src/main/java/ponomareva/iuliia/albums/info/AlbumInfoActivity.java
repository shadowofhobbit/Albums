package ponomareva.iuliia.albums;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ponomareva.iuliia.albums.AlbumsActivity.isConnectedToNetwork;

public class AlbumInfoActivity extends AppCompatActivity {
    static final String ALBUM = "album";
    private Album album;
    @BindView(R.id.info) TextView view;
    @BindView(R.id.songs) TextView songsView;
    @BindView(R.id.artwork) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            album = savedInstanceState.getParcelable(ALBUM);
        } else {
            Intent intent = getIntent();
            album = intent.getParcelableExtra(ALBUM);
        }
        view.setText(album.toString());
        if (!isConnectedToNetwork(getApplicationContext())) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            Retrofit build = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://itunes.apple.com/").build();
            ItunesService itunesService = build.create(ItunesService.class);
            itunesService.lookUpAlbumById(album.getCollectionId()).enqueue(new Callback<LookUpResult>() {
                @Override
                public void onResponse(@NonNull Call<LookUpResult> call, @NonNull Response<LookUpResult> response) {
                    LookUpResult searchResult = response.body();
                    if (searchResult != null) {
                        List<Result> results = searchResult.getResults();
                        StringBuilder builder = new StringBuilder();
                        for (Result result: results) {
                            if (result.getWrapperType().equals("track")) {
                                builder.append(result.getTrackNumber()).append(result.getTrackName()).append(result.getTrackTimeMillis()).append("\n");
                            }
                        }
                        songsView.setText(builder.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LookUpResult> call, @NonNull Throwable t) {
                    Log.e("Albums", t.toString());
                }
            });
        }
        Picasso.get().load(album.getArtworkUrl100()).into(imageView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ALBUM, album);

    }
}
