package app.music.utils.theme

import android.content.Context
import android.content.SharedPreferences
import app.music.R
import app.music.utils.sharepreferences.SharedPrefMethodUtils
import java.lang.ref.WeakReference

object ThemeMethodUtils {

    private var sEditor: SharedPreferences.Editor? = null

    fun setAppTheme(context: Context, baseThemeName: String, themeMode: String) {
        when (themeMode) {
            ThemeConstantUtils.PREF_DARK_MODE -> setDarkTheme(context, baseThemeName)
            ThemeConstantUtils.PREF_LIGHT_MODE -> setLightTheme(context, baseThemeName)
            else -> {
            }
        }
    }

    private fun setDarkTheme(context: Context, baseThemeName: String) {
        context.setTheme(
                when (baseThemeName) {
                    ThemeConstantUtils.PREF_BASE_THEME_BLACK -> R.style.DarkThemeBlack
                    ThemeConstantUtils.PREF_BASE_THEME_INDIGO -> R.style.DarkThemeIndigo
                    ThemeConstantUtils.PREF_BASE_THEME_MAGENTA -> R.style.DarkThemeMagenta
                    ThemeConstantUtils.PREF_BASE_THEME_DEEP_SKY_BLUE -> R.style.DarkThemeDeepSkyBlue
                    ThemeConstantUtils.PREF_BASE_THEME_NAVY -> R.style.DarkThemeNavy
                    ThemeConstantUtils.PREF_BASE_THEME_MAROON -> R.style.DarkThemeMaroon
                    ThemeConstantUtils.PREF_BASE_THEME_DARK_RED -> R.style.DarkThemeDarkRed
                    ThemeConstantUtils.PREF_BASE_THEME_BROWN -> R.style.DarkThemeBrown
                    else -> R.style.DarkThemeBlack
                })
    }

    private fun setLightTheme(context: Context, baseThemeName: String) {
        context.setTheme(
                when (baseThemeName) {
                    ThemeConstantUtils.PREF_BASE_THEME_BLACK -> R.style.LightThemeBlack
                    ThemeConstantUtils.PREF_BASE_THEME_INDIGO -> R.style.LightThemeIndigo
                    ThemeConstantUtils.PREF_BASE_THEME_MAGENTA -> R.style.LightThemeMagenta
                    ThemeConstantUtils.PREF_BASE_THEME_DEEP_SKY_BLUE -> R.style.LightThemeDeepSkyBlue
                    ThemeConstantUtils.PREF_BASE_THEME_NAVY -> R.style.LightThemeNavy
                    ThemeConstantUtils.PREF_BASE_THEME_MAROON -> R.style.LightThemeMaroon
                    ThemeConstantUtils.PREF_BASE_THEME_DARK_RED -> R.style.LightThemeDarkRed
                    ThemeConstantUtils.PREF_BASE_THEME_BROWN -> R.style.LightThemeBrown
                    else -> R.style.LightThemeBlack
                })
    }

    fun saveCurrentThemeMode(contextReference: WeakReference<Context>, themeMode: String) {
        if (null == sEditor) {
            sEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        sEditor?.run {
            putString(ThemeConstantUtils.PREF_CURRENT_THEME_MODE, themeMode)
            apply()
        }
    }

    fun getCurrentThemeMode(contextReference: WeakReference<Context>): String? {
        val sharedPreferences = SharedPrefMethodUtils.getSharedPreferences(contextReference)
        return sharedPreferences.getString(ThemeConstantUtils.PREF_CURRENT_THEME_MODE,
                ThemeConstantUtils.PREF_DARK_MODE)
    }

    fun isDarkModeEnabled(contextReference: WeakReference<Context>): Boolean {
        return getCurrentThemeMode(contextReference) == ThemeConstantUtils.PREF_DARK_MODE
    }

    fun saveCurrentBaseThemeName(contextReference: WeakReference<Context>, baseThemeName: String) {
        if (null == sEditor) {
            sEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        sEditor?.run {
            putString(ThemeConstantUtils.PREF_BASE_THEME_NAME, baseThemeName)
            apply()
        }
    }

    fun getCurrentBaseThemeName(contextReference: WeakReference<Context>): String {
        val sharedPreferences = SharedPrefMethodUtils.getSharedPreferences(contextReference)
        return sharedPreferences.getString(ThemeConstantUtils.PREF_BASE_THEME_NAME,
                ThemeConstantUtils.PREF_BASE_THEME_MAGENTA).toString()
    }
}
