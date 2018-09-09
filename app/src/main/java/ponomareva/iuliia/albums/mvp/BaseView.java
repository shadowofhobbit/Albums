package ponomareva.iuliia.albums;

public interface BaseView {
    void showProgress();
    void hideProgress();
    void displayError();
    void displayNoInternet();
    boolean isConnectedToNetwork();
}
