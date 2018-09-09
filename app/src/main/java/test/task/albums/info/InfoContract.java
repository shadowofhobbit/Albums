package test.task.albums.info;

import java.util.List;

import ponomareva.iuliia.albums.api.Album;
import test.task.albums.mvp.BaseView;
import test.task.albums.api.Result;

interface InfoContract {
    interface View extends BaseView {
        void displaySongs(List<Song> songs);
        void displayImage(String url);
        void displayAlbumInfo();

    }
    interface Presenter extends test.task.albums.mvp.Presenter<View> {
        void needSongsForAlbum(Album album);
        void dontNeedSongsForAlbum(Album album);
    }

    interface Model {
        void getListOfSongs(Album album, OnFinishedListener listener);
        interface OnFinishedListener {
            void onGotAlbumAndSongs(List<Result> tracks);
            void onFailure(Throwable t);
        }
    }

    class Song {
        private String title;
        private int trackNumber;

        Song(Integer trackNumber, String trackName) {
            this.trackNumber = trackNumber;
            this.title = trackName;
        }

        public String getTitle() {
            return title;
        }

        public int getTrackNumber() {
            return trackNumber;
        }

    }
}
