package app.music.viewmodel

import android.content.Context
import app.music.base.BaseNormalViewModel
import app.music.base.contract.ChooseThemeActivityContract
import app.music.ui.screen.setting.ChooseThemeActivity
import app.music.utils.theme.ThemeMethodUtils
import java.lang.ref.WeakReference

class ChooseThemeActivityViewModel
    : BaseNormalViewModel(),
        ChooseThemeActivityContract.ViewModel {

    override fun saveCurrentTheme(context: Context, baseThemeName: String) {
        ThemeMethodUtils.saveCurrentBaseThemeName(WeakReference(context), baseThemeName)
    }
}