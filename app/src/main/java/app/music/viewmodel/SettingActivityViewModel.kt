package app.music.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import app.music.base.BaseNormalViewModel
import app.music.base.contract.SettingActivityContract
import app.music.ui.screen.setting.SettingActivity
import app.music.utils.theme.ThemeConstantUtils
import app.music.utils.theme.ThemeMethodUtils
import java.lang.ref.WeakReference


class SettingActivityViewModel
    : BaseNormalViewModel(),
        SettingActivityContract.ViewModel {

    override val mIsDarkModeEnabled = ObservableBoolean()

    fun checkDarkMode(context: Context) {
        mIsDarkModeEnabled.set(
                with(ThemeConstantUtils) {
                    when (ThemeMethodUtils.getCurrentThemeMode(WeakReference(context))) {
                        PREF_DARK_MODE -> true
                        PREF_LIGHT_MODE -> false
                        else -> true
                    }
                }
        )
    }

    fun changeThemeMode(context: Context) {
        mIsDarkModeEnabled.set(!mIsDarkModeEnabled.get())
        ThemeMethodUtils.saveCurrentThemeMode(
                WeakReference(context),
                with(ThemeConstantUtils) {
                    if (mIsDarkModeEnabled.get()) PREF_DARK_MODE
                    else PREF_LIGHT_MODE
                }
        )
    }
}