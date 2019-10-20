package app.music.model.repository.setting

import android.content.Context
import javax.inject.Inject

class SettingRepository @Inject constructor(
        private val settingLocalDataSource: SettingLocalDataSource) {

    fun getCurrentThemeMode(context: Context): Boolean {
        return settingLocalDataSource.getCurrentThemeMode(context)
    }

    fun saveCurrentThemeMode(context: Context, isDarkModeEnabled: Boolean) {
        settingLocalDataSource.saveCurrentThemeMode(context, isDarkModeEnabled)
    }
}