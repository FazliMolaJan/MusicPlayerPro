package app.music.base.contract

import app.music.base.BaseView
import app.music.base.BaseViewModel

interface SplashActivityContract {

    interface View : BaseView {

    }

    interface ViewModel : BaseViewModel {

        fun loadMusic()
    }
}