package app.music.base

import androidx.lifecycle.ViewModel

abstract class BaseMVVMPPresenter<VM : ViewModel> {

    protected var mViewModel: VM? = null

    fun attachViewModel(viewModel: VM) {
        mViewModel = viewModel
    }

    fun detachViewModel() {
        mViewModel = null
    }
}
