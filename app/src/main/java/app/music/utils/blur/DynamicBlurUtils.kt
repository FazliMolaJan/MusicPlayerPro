package app.music.utils.blur

import android.app.Activity
import android.view.ViewGroup
import app.music.utils.ConstantUtil
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

object DynamicBlurUtils {

    fun blurView(activity: Activity, rootViewId: Int, vararg blurViews: BlurView) {
        with(activity.window.decorView) {
            val rootView = findViewById<ViewGroup>(rootViewId)
            val windowBackground = background
            blurViews.forEach {
                it.setupWith(rootView)
                        .setFrameClearDrawable(windowBackground)
                        .setBlurAlgorithm(RenderScriptBlur(activity))
                        .setBlurRadius(ConstantUtil.DYNAMIC_BLUR_RADIUS)
                        .setHasFixedTransformationMatrix(true)
            }
        }
    }
}
