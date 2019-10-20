package app.music.viewmodel

import android.content.Context
import app.music.base.BaseMVVMPViewModel
import app.music.base.contract.ChooseThemeActivityContract
import app.music.presenter.ChooseThemeActivityPresenter
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChooseThemeActivityViewModel @Inject constructor(
        chooseThemeActivityPresenter: ChooseThemeActivityPresenter)
    : BaseMVVMPViewModel<ChooseThemeActivityViewModel, ChooseThemeActivityPresenter>(
        chooseThemeActivityPresenter),
        ChooseThemeActivityContract.ViewModel {

    init {
        mPresenter.attachViewModel(this)
    }

    fun saveCurrentTheme(context: Context, baseThemeName: String) {
        mPresenter.saveCurrentTheme(context, baseThemeName)
    }

    fun getCurrentBaseThemeName(weakReference: WeakReference<Context>): String {
        return mPresenter.getCurrentBaseThemeName(weakReference)
    }
}