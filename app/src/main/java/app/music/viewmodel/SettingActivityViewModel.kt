package app.music.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import app.music.base.BaseMVVMPViewModel
import app.music.base.contract.SettingActivityContract
import app.music.presenter.SettingActivityPresenter
import javax.inject.Inject


class SettingActivityViewModel @Inject constructor(
        settingActivityPresenter: SettingActivityPresenter)
    : BaseMVVMPViewModel<SettingActivityViewModel, SettingActivityPresenter>(
        settingActivityPresenter),
        SettingActivityContract.ViewModel {

    init {
        mPresenter.attachViewModel(this)
    }

    override val mIsDarkModeEnabled = ObservableBoolean()

    override fun setDarkModeEnabledState(darkModeStatus: Boolean) {
        mIsDarkModeEnabled.set(darkModeStatus)
    }

    fun checkDarkMode(context: Context) {
        mPresenter.checkDarkMode(context)
    }

    fun changeThemeMode(context: Context) {
        setDarkModeEnabledState(!mIsDarkModeEnabled.get())
        mPresenter.saveCurrentThemeMode(context, mIsDarkModeEnabled.get())
    }
}