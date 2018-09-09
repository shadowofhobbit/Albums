package test.task.albums.search;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ponomareva.iuliia.albums.api.Album;
import ponomareva.iuliia.albums.HttpUtils;
import ponomareva.iuliia.albums.R;
import test.task.albums.info.AlbumInfoActivity;

public class AlbumsActivity extends AppCompatActivity implements AlbumClickListener, AlbumsSearchContract.View {
    private static final String QUERY = "Query";
    private AlbumsAdapter adapter;
    @BindView(R.id.albumsView)
    RecyclerView albumsView;
    private SearchAlbumPresenter presenter;
    private AlbumsViewModel albumsViewModel;
    private String query;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.emptyView)
    TextView emptyView;
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY);
        }
        presenter = new SearchAlbumPresenter(new ModelImpl(), this);
        albumsViewModel = ViewModelProviders.of(this).get(AlbumsViewModel.class);
        adapter = new AlbumsAdapter(this);
        albumsView.setHasFixedSize(true);
        albumsView.setLayoutManager(new GridLayoutManager(this, 2));
        albumsView.setAdapter(adapter);
        adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showOrHideRecyclerView();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                showOrHideRecyclerView();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                showOrHideRecyclerView();
            }
        };

        albumsViewModel.getData(presenter, query).observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                updateRecyclerView(albums);
            }
        });
    }

    private void showOrHideRecyclerView() {
        if (adapter.getItemCount() == 0) {
            albumsView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            albumsView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_albums, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlbumsActivity.this.query = query;
                if (!TextUtils.isEmpty(query)) {
                    albumsViewModel.getData(presenter, query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AlbumsActivity.this.query = newText;
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));
        if (query != null) {
            searchView.setQuery(query, true);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUERY, query);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.registerAdapterDataObserver(adapterDataObserver);
        showOrHideRecyclerView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.unregisterAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public boolean isConnectedToNetwork() {
        return HttpUtils.isConnectedToNetwork(getApplicationContext());
    }

    @Override
    public void displayError() {
        emptyView.setText(R.string.error_loading_data);
        if (emptyView.getVisibility() != View.VISIBLE) {
            Toast.makeText(AlbumsActivity.this, R.string.error_loading_data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setText(R.string.loading);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        emptyView.setText(R.string.no_albums_search_for_something);
    }

    @Override
    public void displayNoInternet() {
        Toast.makeText(AlbumsActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayAlbums(List<Album> albums) {
        albumsViewModel.setAlbums(albums);
        updateRecyclerView(albums);
    }

    private void updateRecyclerView(List<Album> albums) {
        if (albums.isEmpty()) {
            if (TextUtils.isEmpty(query)) {
                emptyView.setText(R.string.no_albums_search_for_something);
            } else {
                emptyView.setText(R.string.no_albums_found);
            }
        }
        adapter.setData(albums);
    }

    @Override
    public void onClick(Album album) {
        Intent intent = new Intent(this, AlbumInfoActivity.class);
        intent.putExtra(AlbumInfoActivity.ALBUM, album);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}

