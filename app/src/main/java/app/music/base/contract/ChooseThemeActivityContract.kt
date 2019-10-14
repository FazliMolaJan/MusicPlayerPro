package app.music.base.contract

import android.content.Context
import app.music.base.BaseView
import app.music.base.BaseViewModel

interface ChooseThemeActivityContract {

    interface View : BaseView {

    }

    interface ViewModel : BaseViewModel {

        fun saveCurrentTheme(context: Context, baseThemeName: String)
    }
}