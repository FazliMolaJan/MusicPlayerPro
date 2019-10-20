package app.music.presenter

import android.content.Context
import app.music.base.BaseMVVMPPresenter
import app.music.base.contract.SettingActivityContract
import app.music.model.repository.setting.SettingRepository
import app.music.viewmodel.SettingActivityViewModel
import javax.inject.Inject

class SettingActivityPresenter @Inject constructor(
        private val mSettingRepository: SettingRepository)
    : BaseMVVMPPresenter<SettingActivityViewModel>(),
        SettingActivityContract.Presenter {

    override fun checkDarkMode(context: Context) {
        mViewModel?.setDarkModeEnabledState(mSettingRepository.getCurrentThemeMode(context))
    }

    override fun saveCurrentThemeMode(context: Context, isDarkModeEnabled: Boolean) {
        mSettingRepository.saveCurrentThemeMode(context, isDarkModeEnabled)
    }
}
