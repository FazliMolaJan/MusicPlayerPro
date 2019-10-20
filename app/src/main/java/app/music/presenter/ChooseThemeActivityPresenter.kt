package app.music.presenter

import android.content.Context
import app.music.base.BaseMVVMPPresenter
import app.music.base.contract.ChooseThemeActivityContract
import app.music.model.repository.choosetheme.ChooseThemeRepository
import app.music.viewmodel.ChooseThemeActivityViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChooseThemeActivityPresenter @Inject constructor(
        private val mChooseThemeRepository: ChooseThemeRepository)
    : BaseMVVMPPresenter<ChooseThemeActivityViewModel>(),
        ChooseThemeActivityContract.Presenter {

    override fun saveCurrentTheme(context: Context, baseThemeName: String) {
        mChooseThemeRepository.saveCurrentTheme(context, baseThemeName)
    }

    override fun getCurrentBaseThemeName(weakReference: WeakReference<Context>): String {
        return mChooseThemeRepository.getCurrentBaseThemeName(weakReference)
    }
}
