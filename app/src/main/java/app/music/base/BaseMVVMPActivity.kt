package app.music.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject


abstract class BaseMVVMPActivity<T : ViewDataBinding, VM : BaseViewModel>
    : BaseActivity<T>() {

    @Inject
    protected lateinit var mViewModelProviderFactory: ViewModelProvider.Factory
    protected lateinit var mViewModel: VM
    protected lateinit var mViewModelProvider: ViewModelProvider

    override fun onViewCreated() {
        super.onViewCreated()
        mViewModelProvider = ViewModelProviders.of(this, mViewModelProviderFactory)
        mViewModel = getViewModel()
    }

    abstract fun getViewModel(): VM
}
