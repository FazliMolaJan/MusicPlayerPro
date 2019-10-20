package app.music.model.repository.choosetheme

import android.content.Context
import app.music.utils.theme.ThemeMethodUtils
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChooseThemeLocalDataSource @Inject constructor() {

    fun saveCurrentTheme(context: Context, baseThemeName: String) {
        ThemeMethodUtils.saveCurrentBaseThemeName(WeakReference(context), baseThemeName)
    }

    fun getCurrentBaseThemeName(weakReference: WeakReference<Context>): String {
        return ThemeMethodUtils.getCurrentBaseThemeName(weakReference)
    }
}