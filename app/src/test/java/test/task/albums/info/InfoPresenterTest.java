package test.task.albums.info;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import test.task.albums.api.Album;
import test.task.albums.api.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InfoPresenterTest {
    @Mock
    private InfoContract.Model model;
    @Mock
    private InfoContract.View view;
    private InfoPresenter presenter;

    @Mock
    private Album album;

    @Mock
    private Result result;
    @Mock
    private Result result2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new InfoPresenter(model, view);
    }

    @Test
    public void onGotAlbumAndSongs() {
        when(result.getWrapperType()).thenReturn("track");
        when(result.getKind()).thenReturn("song");
        when(result.getTrackName()).thenReturn("A song");
        when(result2.getWrapperType()).thenReturn("collection");
        when(result2.getKind()).thenReturn("album");
        when(result2.getTrackName()).thenReturn("An album");
        presenter.onGotAlbumAndSongs(Arrays.asList(result2, result));
        ArgumentCaptor<List<InfoContract.Song>> argument = ArgumentCaptor.forClass(List.class);
        verify(view).displaySongs(argument.capture());
        assertEquals(1, argument.getValue().size());
        verify(view).hideProgress();
    }

    @Test
    public void onFailure() {
        presenter.onFailure(new Exception());
        verify(view).hideProgress();
        verify(view).displayError();
    }

    @Test
    public void needSongsForAlbumNoInternet() {
        when(view.isConnectedToNetwork()).thenReturn(false);
        presenter.needSongsForAlbum(album);
        verify(view).hideProgress();
        verify(view).displayAlbumInfo();
        verify(view).displayNoInternet();
    }

    @Test
    public void needSongsForAlbumWithInternet() {
        when(view.isConnectedToNetwork()).thenReturn(true);
        String url = "http://www.example.com";
        when(album.getArtworkUrl100()).thenReturn(url);
        presenter.needSongsForAlbum(album);
        verify(view).hideProgress();
        verify(view).displayAlbumInfo();
        ArgumentCaptor<String> argument = forClass(String.class);
        verify(view).displayImage(argument.capture());
        assertTrue(argument.getValue().equals(url));
        verify(view).showProgress();
        verify(model).getListOfSongs(album, presenter);
    }
}