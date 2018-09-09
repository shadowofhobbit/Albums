package test.task.albums.mvp;

public interface Presenter<V extends BaseView> {
    void detachView();
}


