package app.music.base.contract

import androidx.databinding.ObservableBoolean
import app.music.base.BaseView
import app.music.base.BaseViewModel

interface SettingActivityContract {

    interface View : BaseView {

    }

    interface ViewModel : BaseViewModel {
        val mIsDarkModeEnabled: ObservableBoolean
    }
}