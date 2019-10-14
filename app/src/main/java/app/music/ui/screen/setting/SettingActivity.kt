package app.music.ui.screen.setting

import android.view.Menu
import app.music.R
import app.music.base.BaseMVVMActivity
import app.music.base.contract.SettingActivityContract
import app.music.databinding.ActivitySettingBinding
import app.music.utils.blur.DynamicBlurUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.viewmodel.ViewModelUtils
import app.music.viewmodel.SettingActivityViewModel
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity
    : BaseMVVMActivity<ActivitySettingBinding, SettingActivityViewModel>(),
        SettingActivityContract.View {

    override fun getViewModel() = ViewModelUtils.getViewModel<SettingActivityViewModel>(this)

    override fun initInject() = activityComponent.inject(this)

    override fun getLayoutId() = R.layout.activity_setting

    override fun getLogTag() = TAG

    override fun getOptionMenuId() = 0

    override fun createOptionMenu(menu: Menu) {

    }

    override fun initView() {
        DynamicBlurUtils.blurView(this, R.id.coordinator_background, mBinding.blurToolBar)
        binding.settingActivityViewModel = mViewModel
        mViewModel.checkDarkMode(applicationContext)
        setViewClickListener()
    }

    override fun initData() {

    }

    private fun setViewClickListener() {
        text_theme.setOnClickListener { IntentMethodUtils.launchChooseThemeActivity(this) }
        check_dark_mode.setOnClickListener {
            mViewModel.changeThemeMode(applicationContext)
            recreate()
        }
    }

    companion object {

        private const val TAG = "SettingActivity"
    }
}
