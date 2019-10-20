package app.music.model.repository.choosetheme

import android.content.Context
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChooseThemeRepository @Inject constructor(
        private val mChooseThemeLocalDataSource: ChooseThemeLocalDataSource) {

    fun saveCurrentTheme(context: Context, baseThemeName: String) {
        mChooseThemeLocalDataSource.saveCurrentTheme(context, baseThemeName)
    }

    fun getCurrentBaseThemeName(weakReference: WeakReference<Context>): String {
        return mChooseThemeLocalDataSource.getCurrentBaseThemeName(weakReference)
    }
}