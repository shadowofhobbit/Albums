package test.task.albums.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ItunesService {
    @GET("search?media=music&entity=album&limit=200&attribute=albumTerm")
    Call<SearchResult> searchAlbum(@Query("term") String term);

    @GET("lookup?entity=song")
    Call<LookUpResult> lookUpAlbumAndSongsById(@Query("id") Long id);

}
