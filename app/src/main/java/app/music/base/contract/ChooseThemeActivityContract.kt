package app.music.base.contract

import android.content.Context
import app.music.base.BasePresenter
import app.music.base.BaseViewModel
import java.lang.ref.WeakReference

interface ChooseThemeActivityContract {

    interface Presenter : BasePresenter {

        fun saveCurrentTheme(context: Context, baseThemeName: String)

        fun getCurrentBaseThemeName(weakReference: WeakReference<Context>): String
    }

    interface ViewModel : BaseViewModel {

    }
}