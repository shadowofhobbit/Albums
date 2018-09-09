package ponomareva.iuliia.albums;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ponomareva.iuliia.albums.info.AlbumInfoActivity;
import retrofit2.Call;
import retrofit2.Callback;

public class AlbumsActivity extends AppCompatActivity implements AlbumClickListener {
    private AlbumsAdapter adapter;
    @BindView(R.id.albumsView) RecyclerView albumsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        ButterKnife.bind(this);
        adapter = new AlbumsAdapter(this);
        albumsView.setHasFixedSize(true);
        albumsView.setLayoutManager(new GridLayoutManager(this, 2));
        albumsView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_albums, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView =
                (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(false);
        //item.expandActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isConnectedToNetwork(getApplicationContext())) {
                    Toast.makeText(AlbumsActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                } else {
                    HttpUtils.getItunesService().searchAlbum(query).enqueue(new Callback<SearchResult>() {
                        @Override
                        public void onResponse(@NonNull Call<SearchResult> call, @NonNull retrofit2.Response<SearchResult> response) {
                            SearchResult searchResult = response.body();
                            if (searchResult != null) {
                                adapter.setData(searchResult.getAlbums());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SearchResult> call, @NonNull Throwable t) {
                            Log.e("Albums", t.toString());
                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));
        return true;
    }

    public static boolean isConnectedToNetwork(Context appContext) {
        ConnectivityManager manager = (ConnectivityManager) appContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
        }
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onClick(Album album) {
        Intent intent = new Intent(this, AlbumInfoActivity.class);
        intent.putExtra(AlbumInfoActivity.ALBUM, album);
        startActivity(intent);
    }
}
