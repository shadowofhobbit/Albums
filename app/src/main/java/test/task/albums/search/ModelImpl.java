package test.task.albums.search;

import android.support.annotation.NonNull;
import android.util.Log;

import ponomareva.iuliia.albums.HttpUtils;
import test.task.albums.api.SearchResult;
import retrofit2.Call;
import retrofit2.Callback;

public class ModelImpl implements AlbumsSearchContract.Model {
    @Override
    public void getListOfAlbums(String query, final OnFinishedListener listener) {
        Log.wtf("albums", "loading albums");
        HttpUtils.getItunesService().searchAlbum(query).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchResult> call, @NonNull retrofit2.Response<SearchResult> response) {
                SearchResult searchResult = response.body();
                if (searchResult != null) {
                    listener.onGotAlbums(searchResult.getAlbums());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResult> call, @NonNull Throwable t) {
                listener.onFailure(t);
            }
        });
    }
}
