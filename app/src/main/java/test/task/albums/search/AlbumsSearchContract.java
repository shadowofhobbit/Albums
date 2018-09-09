package test.task.albums.search;

import java.util.List;

import ponomareva.iuliia.albums.api.Album;
import test.task.albums.mvp.BaseView;

public interface AlbumsSearchContract {

        interface View extends BaseView {
            void displayAlbums(List<Album> albums);
        }

        interface Presenter extends test.task.albums.mvp.Presenter<View> {
            void needAlbums(String query);
        }

        interface Model {
            void getListOfAlbums(String query, OnFinishedListener listener);
            interface OnFinishedListener {
                void onGotAlbums(List<Album> albums);
                void onFailure(Throwable t);
            }
        }


}
