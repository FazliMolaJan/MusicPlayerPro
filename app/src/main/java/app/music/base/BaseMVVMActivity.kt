package app.music.base

import androidx.databinding.ViewDataBinding


abstract class BaseMVVMActivity<T : ViewDataBinding, VM : BaseViewModel>
    : BaseActivity<T>(), BaseView {

    protected lateinit var mViewModel: VM

    override fun onViewCreated() {
        super.onViewCreated()
        mViewModel = getViewModel()
    }

    abstract fun getViewModel(): VM
}
