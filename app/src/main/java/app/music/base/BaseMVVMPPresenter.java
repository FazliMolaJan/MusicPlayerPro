package app.music.base;

import androidx.lifecycle.ViewModel;

public class BaseMVVMPPresenter<T extends ViewModel> {

    protected T mViewModel = null;

    public void attachViewModel(T viewModel) {
        mViewModel = viewModel;
    }

    public void detachViewModel() {
        mViewModel = null;
    }
}
