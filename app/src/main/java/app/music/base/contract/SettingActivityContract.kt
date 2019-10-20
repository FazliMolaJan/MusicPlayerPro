package app.music.base.contract

import android.content.Context
import androidx.databinding.ObservableBoolean
import app.music.base.BasePresenter
import app.music.base.BaseViewModel

interface SettingActivityContract {

    interface Presenter : BasePresenter {

        fun checkDarkMode(context: Context)

        fun saveCurrentThemeMode(context: Context, isDarkModeEnabled: Boolean)
    }

    interface ViewModel : BaseViewModel {

        val mIsDarkModeEnabled: ObservableBoolean

        fun setDarkModeEnabledState(darkModeStatus: Boolean)
    }
}