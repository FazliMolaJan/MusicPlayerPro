package app.music.base

import androidx.lifecycle.ViewModel

abstract class BaseMVVMPViewModel<VM : ViewModel, P : BaseMVVMPPresenter<VM>>(
        protected var mPresenter: P)
    : ViewModel(), BaseViewModel {

    override fun onCleared() {
        super.onCleared()
        mPresenter.detachViewModel()
    }
}
