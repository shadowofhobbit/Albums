package test.task.albums.info;

import java.util.ArrayList;
import java.util.List;

import ponomareva.iuliia.albums.api.Album;
import test.task.albums.mvp.BasePresenter;
import test.task.albums.api.Result;

public class InfoPresenter extends BasePresenter<InfoContract.View>
        implements InfoContract.Presenter, InfoContract.Model.OnFinishedListener {
    private InfoContract.Model model;

    InfoPresenter(InfoContract.Model model, InfoContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void onGotAlbumAndSongs(List<Result> results) {
        List<InfoContract.Song> songs = new ArrayList<>();
        for (Result result: results) {
            if (result.getWrapperType().equals("track") && result.getKind().equals("song")) {
                songs.add(new InfoContract.Song(result.getTrackNumber(), result.getTrackName()));
            }
        }
        view.hideProgress();
        view.displaySongs(songs);
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.displayError();
    }

    @Override
    public void dontNeedSongsForAlbum(Album album) {
        view.hideProgress();
        view.displayAlbumInfo();
        if (view.isConnectedToNetwork()) {
            String url = (album.getArtworkUrl100() != null) ? album.getArtworkUrl100() : album.getArtworkUrl60();
            view.displayImage(url);
        }
    }

    @Override
    public void needSongsForAlbum(Album album) {
        view.hideProgress();
        view.displayAlbumInfo();
        if (view.isConnectedToNetwork()) {
            String url = (album.getArtworkUrl100() != null) ? album.getArtworkUrl100() : album.getArtworkUrl60();
            view.displayImage(url);
            view.showProgress();
            model.getListOfSongs(album, this);
        } else {
            view.displayNoInternet();
        }
    }
}
