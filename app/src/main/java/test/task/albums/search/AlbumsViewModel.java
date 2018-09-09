package test.task.albums.search;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import test.task.albums.api.Album;

//With weaker access Espresso tests don't work.
@SuppressWarnings("WeakerAccess")
public class AlbumsViewModel extends ViewModel {
    private String query;
    private MutableLiveData<List<Album>> albums;

    public AlbumsViewModel() {
    }

    LiveData<List<Album>> getData(AlbumsSearchContract.Presenter presenter, String query) {
        if ((albums == null) || (this.query == null) || (!this.query.equals(query))) {
            this.query = query;
            albums = new MutableLiveData<>();
            presenter.needAlbums(query);
        }
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        if (this.albums == null) {
            this.albums = new MutableLiveData<>();
        }
        this.albums.setValue(albums);
    }

}
