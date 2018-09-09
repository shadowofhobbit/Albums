
package test.task.albums.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("resultCount")
    @Expose
    private Integer resultCount;
    @SerializedName("results")
    @Expose
    private List<Album> albums = null;

    public Integer getResultCount() {
        return resultCount;
    }

    public List<Album> getAlbums() {
        return albums;
    }

}
