package test.task.albums.mvp;

public interface BaseView {
    void showProgress();
    void hideProgress();
    void displayError();
    void displayNoInternet();
    boolean isConnectedToNetwork();
}
