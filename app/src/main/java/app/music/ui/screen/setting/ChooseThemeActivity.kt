package app.music.ui.screen.setting

import android.view.Menu
import app.music.R
import app.music.base.BaseMVVMActivity
import app.music.base.contract.ChooseThemeActivityContract
import app.music.databinding.ActivityChooseThemeBinding
import app.music.utils.theme.ThemeConstantUtils
import app.music.utils.theme.ThemeMethodUtils
import app.music.utils.viewmodel.ViewModelUtils
import app.music.viewmodel.ChooseThemeActivityViewModel
import kotlinx.android.synthetic.main.activity_choose_theme.*
import kotlinx.android.synthetic.main.content_choose_theme.*
import java.lang.ref.WeakReference

class ChooseThemeActivity
    : BaseMVVMActivity<ActivityChooseThemeBinding, ChooseThemeActivityViewModel>(),
        ChooseThemeActivityContract.View {

    override fun getViewModel() = ViewModelUtils.getViewModel<ChooseThemeActivityViewModel>(this)

    override fun getLayoutId() = R.layout.activity_choose_theme

    override fun initView() {
        setupActionBar()
        setThemeRadioButtonCheck()
        setRadioGroupListener()
    }

    override fun initData() {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initInject() = activityComponent.inject(this)

    override fun getLogTag() = TAG

    override fun getOptionMenuId() = 0

    override fun createOptionMenu(menu: Menu) {

    }

    private fun setRadioGroupListener() {
        radio_group_theme.setOnCheckedChangeListener { group, checkedId ->
            mViewModel.saveCurrentTheme(applicationContext,
                    with(ThemeConstantUtils) {
                        when (checkedId) {
                            R.id.radio_black_theme -> PREF_BASE_THEME_BLACK
                            R.id.radio_indigo_theme -> PREF_BASE_THEME_INDIGO
                            R.id.radio_magenta_theme -> PREF_BASE_THEME_MAGENTA
                            R.id.radio_deep_sky_blue_theme -> PREF_BASE_THEME_DEEP_SKY_BLUE
                            R.id.radio_navy_theme -> PREF_BASE_THEME_NAVY
                            R.id.radio_maroon_theme -> PREF_BASE_THEME_MAROON
                            R.id.radio_dark_red_theme -> PREF_BASE_THEME_DARK_RED
                            R.id.radio_brown_theme -> PREF_BASE_THEME_BROWN
                            else -> PREF_BASE_THEME_BLACK
                        }
                    })
            recreate()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setThemeRadioButtonCheck() {
        with(ThemeConstantUtils) {
            when (ThemeMethodUtils.getCurrentBaseThemeName(WeakReference(applicationContext))) {
                PREF_BASE_THEME_BLACK -> radio_black_theme.isChecked = true
                PREF_BASE_THEME_INDIGO -> radio_indigo_theme.isChecked = true
                PREF_BASE_THEME_MAGENTA -> radio_magenta_theme.isChecked = true
                PREF_BASE_THEME_DEEP_SKY_BLUE -> radio_deep_sky_blue_theme.isChecked = true
                PREF_BASE_THEME_NAVY -> radio_navy_theme.isChecked = true
                PREF_BASE_THEME_MAROON -> radio_maroon_theme.isChecked = true
                PREF_BASE_THEME_DARK_RED -> radio_dark_red_theme.isChecked = true
                PREF_BASE_THEME_BROWN -> radio_brown_theme.isChecked = true
                else -> radio_black_theme.isChecked = true
            }
        }
    }

    companion object {

        private const val TAG = "ChooseThemeActivity"
    }
}
