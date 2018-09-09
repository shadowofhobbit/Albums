package ponomareva.iuliia.albums;

public abstract class BasePresenter<V extends BaseView> implements Presenter<V> {
    protected V view;

    @Override
    public void detachView() {
        view = null;
    }

}
