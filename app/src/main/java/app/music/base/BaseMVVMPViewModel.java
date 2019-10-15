package app.music.base;

import androidx.lifecycle.ViewModel;

public abstract class BaseMVVMPViewModel<T extends BasePresenter>
        extends ViewModel implements BaseViewModel {

    protected T mPresenter = getPresenter();

    protected abstract T getPresenter();
}
