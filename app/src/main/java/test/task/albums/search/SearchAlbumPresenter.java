package test.task.albums.search;

import java.util.List;

import ponomareva.iuliia.albums.api.Album;
import test.task.albums.mvp.BasePresenter;

public class SearchAlbumPresenter extends BasePresenter<AlbumsSearchContract.View>
        implements AlbumsSearchContract.Presenter, AlbumsSearchContract.Model.OnFinishedListener {
    private AlbumsSearchContract.Model model;

    SearchAlbumPresenter(AlbumsSearchContract.Model model, AlbumsSearchContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void needAlbums(String query) {
        view.hideProgress();
        if (view.isConnectedToNetwork()) {
            view.showProgress();
            model.getListOfAlbums(query, this);
        } else {
            view.displayNoInternet();
        }
    }

    @Override
    public void onGotAlbums(List<Album> albums) {
        view.hideProgress();
        view.displayAlbums(albums);
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.displayError();
    }
}
