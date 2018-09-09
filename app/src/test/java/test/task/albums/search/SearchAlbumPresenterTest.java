package test.task.albums.search;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import test.task.albums.api.Album;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchAlbumPresenterTest {
    @Mock
    private AlbumsSearchContract.Model model;
    @Mock
    private AlbumsSearchContract.View view;
    private SearchAlbumPresenter presenter;

    @Mock
    private Album album1;
    @Mock
    private Album album2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new SearchAlbumPresenter(model, view);
    }

    @Test
    public void needAlbumsWithInternet() {
        when(view.isConnectedToNetwork()).thenReturn(true);
        String query = "my query";
        presenter.needAlbums(query);
        verify(view).showProgress();
        verify(model).getListOfAlbums(query, presenter);
    }

    @Test
    public void needAlbumsNoInternet() {
        when(view.isConnectedToNetwork()).thenReturn(false);
        presenter.needAlbums("my query");
        verify(view).hideProgress();
        verify(view).displayNoInternet();
    }

    @Test
    public void onGotAlbums() {
        List<Album> albums = Arrays.asList(album1, album2);
        presenter.onGotAlbums(albums);
        verify(view).hideProgress();
        verify(view).displayAlbums(albums);
    }

    @Test
    public void onFailure() {
        presenter.onFailure(new Exception());
        verify(view).hideProgress();
        verify(view).displayError();
    }

}