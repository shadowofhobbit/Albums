package test.task.albums.info;

import android.support.annotation.NonNull;

import java.util.List;

import test.task.albums.api.Album;
import test.task.albums.HttpUtils;
import test.task.albums.api.LookUpResult;
import test.task.albums.api.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ModelImpl implements InfoContract.Model {

    @Override
    public void getListOfSongs(Album album, final OnFinishedListener listener) {
        HttpUtils.getItunesService().lookUpAlbumAndSongsById(album.getCollectionId()).enqueue(new Callback<LookUpResult>() {
            @Override
            public void onResponse(@NonNull Call<LookUpResult> call, @NonNull Response<LookUpResult> response) {
                LookUpResult searchResult = response.body();
                if (searchResult != null) {
                    List<Result> results = searchResult.getResults();
                    listener.onGotAlbumAndSongs(results);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LookUpResult> call, @NonNull Throwable t) {
                listener.onFailure(t);
            }
        });
    }

}




