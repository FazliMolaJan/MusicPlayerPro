package app.music.listener

import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import eightbitlab.com.blurview.BlurView

interface ToolbarScrollFlagListener {

    var mToolbar: BlurView?
    var mAppBarLayout: AppBarLayout?

    fun setPinToolbar() {
        with(getToolbarLayoutParam()) {
            if (scrollFlags == com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                    com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP) {
                mAppBarLayout?.layoutParams = getAppBarLayoutParam().apply {
                    scrollFlags = 0
                    behavior = null
                }
//            val appBarLayoutParams = getAppBarLayoutParam()
//            params.scrollFlags = 0
//            appBarLayoutParams.behavior = null
//            mAppBarLayout?.layoutParams = appBarLayoutParams
            }
        }
    }

    fun setScrollToolBar() {
        with(getToolbarLayoutParam()) {
            if (scrollFlags == 0) {
                mAppBarLayout?.layoutParams = getAppBarLayoutParam().apply {
                    scrollFlags = com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                            com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                            com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                    behavior = com.google.android.material.appbar.AppBarLayout.Behavior()
                }

//            val appBarLayoutParams = getAppBarLayoutParam()
//            params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
//                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
//                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
//            appBarLayoutParams.behavior = AppBarLayout.Behavior()
//            mAppBarLayout?.layoutParams = appBarLayoutParams
            }
        }
    }

    fun getToolbarLayoutParam(): AppBarLayout.LayoutParams {
        return mToolbar?.layoutParams as AppBarLayout.LayoutParams
    }

    fun getAppBarLayoutParam(): CoordinatorLayout.LayoutParams {
        return mAppBarLayout?.layoutParams as CoordinatorLayout.LayoutParams
    }
}
