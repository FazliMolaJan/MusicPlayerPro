package app.music.presenter

import android.content.Context
import app.music.base.BaseMVVMPPresenter
import app.music.base.contract.SettingActivityContract
import app.music.utils.theme.ThemeConstantUtils
import app.music.utils.theme.ThemeMethodUtils
import app.music.viewmodel.SettingActivityViewModel
import java.lang.ref.WeakReference

class SettingActivityPresenter
    : BaseMVVMPPresenter<SettingActivityViewModel>(),
        SettingActivityContract.Presenter {

    override fun checkDarkMode(context: Context) {
        with(ThemeConstantUtils) {
            mViewModel.changeDarkMode(
                    when (ThemeMethodUtils.getCurrentThemeMode(WeakReference(context))) {
                        PREF_DARK_MODE -> true
                        PREF_LIGHT_MODE -> false
                        else -> true
                    })
        }
    }

    override fun saveCurrentThemeMode(context: Context, isDarkModeEnabled: Boolean) {
        ThemeMethodUtils.saveCurrentThemeMode(
                WeakReference(context),
                with(ThemeConstantUtils) {
                    if (isDarkModeEnabled) PREF_DARK_MODE
                    else PREF_LIGHT_MODE
                }
        )
    }
}
