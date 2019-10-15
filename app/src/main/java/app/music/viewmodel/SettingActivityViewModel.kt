package app.music.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import app.music.base.BaseMVVMPViewModel
import app.music.base.contract.SettingActivityContract
import app.music.presenter.SettingActivityPresenter


class SettingActivityViewModel
    : BaseMVVMPViewModel<SettingActivityPresenter>(),
        SettingActivityContract.ViewModel {

    override val mIsDarkModeEnabled = ObservableBoolean()

    init {
        mPresenter.attachViewModel(this)
    }

    override fun getPresenter() = SettingActivityPresenter()

    override fun onCleared() {
        super.onCleared()
        mPresenter.detachViewModel()
    }

    override fun changeDarkMode(darkModeStatus: Boolean) {
        mIsDarkModeEnabled.set(darkModeStatus)
    }

    fun checkDarkMode(context: Context) {
        mPresenter.checkDarkMode(context)
    }

    fun changeThemeMode(context: Context) {
        changeDarkMode(!mIsDarkModeEnabled.get())
        mPresenter.saveCurrentThemeMode(context, mIsDarkModeEnabled.get())
    }
}