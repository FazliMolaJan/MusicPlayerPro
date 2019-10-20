package app.music.model.repository.setting

import android.content.Context
import app.music.utils.theme.ThemeConstantUtils
import app.music.utils.theme.ThemeMethodUtils
import java.lang.ref.WeakReference
import javax.inject.Inject

class SettingLocalDataSource @Inject constructor() {

    fun getCurrentThemeMode(context: Context): Boolean {
        return with(ThemeConstantUtils) {
            when (ThemeMethodUtils.getCurrentThemeMode(WeakReference(context))) {
                PREF_DARK_MODE -> true
                PREF_LIGHT_MODE -> false
                else -> true
            }
        }
    }

    fun saveCurrentThemeMode(context: Context, isDarkModeEnabled: Boolean) {
        ThemeMethodUtils.saveCurrentThemeMode(
                WeakReference(context),
                with(ThemeConstantUtils) {
                    if (isDarkModeEnabled) PREF_DARK_MODE
                    else PREF_LIGHT_MODE
                }
        )
    }
}