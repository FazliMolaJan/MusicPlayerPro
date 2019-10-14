package app.music.utils

import android.content.Context
import android.os.Build
import androidx.core.view.ViewCompat
import androidx.appcompat.widget.Toolbar
import android.util.TypedValue
import android.view.View
import android.view.View.*
import android.view.ViewGroup

object InsetUtils {

//    private fun setupInsets() {
//        val baseMoviesPadding = pxFromDp(10f)
//        val toolbarHeight = binding.toolBar.layoutParams.height
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            binding.relativeHome.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        } else {
//            binding.container.setPadding(0, toolbarHeight + baseMoviesPadding, 0, 0)
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.relativeTopBar) { view, insets ->
//            //            binding.relativeTopBar.setMargins(topMarginDp = insets.systemWindowInsetTop)
//            setViewMargins(this, binding.relativeTopBar.layoutParams, 0, insets.systemWindowInsetTop,
//                    0, 0, binding.relativeTopBar)
//            binding.container.setPadding(0, toolbarHeight + insets.systemWindowInsetTop + baseMoviesPadding, 0, 0)
//            insets
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { view, insets ->
//            binding.container.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
//            insets
//        }
//    }
//
//    private fun setupInsets(context: Context, moviesToolbar: Toolbar, moviesRootLayout: View,
//                            moviesRecyclerView: View) {
//        val baseMoviesPadding = pxFromDp(10f, context)
//        val toolbarHeight = moviesToolbar.layoutParams.height
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            moviesRootLayout.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        } else {
////            moviesRecyclerView.updatePadding(top = toolbarHeight + baseMoviesPadding)
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(moviesToolbar) { view, insets ->
//            moviesToolbar.setMarginTop(insets.systemWindowInsetTop)
//            moviesRecyclerView.updatePadding(top = toolbarHeight + insets.systemWindowInsetTop + baseMoviesPadding)
//            insets
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(moviesRecyclerView) { view, insets ->
//            moviesRecyclerView.updatePadding(bottom = insets.systemWindowInsetBottom)
//            insets
//        }
//    }

    fun setViewMargins(con: Context, params: ViewGroup.LayoutParams, left: Int, top: Int, right: Int, bottom: Int, view: View) {

        val scale = con.resources.displayMetrics.density
        // convert the DP into pixel
        val pixel_left = (left * scale + 0.5f).toInt()
        val pixel_top = (top * scale + 0.5f).toInt()
        val pixel_right = (right * scale + 0.5f).toInt()
        val pixel_bottom = (bottom * scale + 0.5f).toInt()

        val s = params as ViewGroup.MarginLayoutParams
        s.setMargins(pixel_left, pixel_top, pixel_right, pixel_bottom)

        view.layoutParams = params
    }

    fun Int.dpToPx(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

    fun View.setMargins(leftMarginDp: Int? = null, topMarginDp: Int? = null,
                        rightMarginDp: Int? = null, bottomMarginDp: Int? = null) {
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            val params = layoutParams as ViewGroup.MarginLayoutParams
            leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
            topMarginDp?.run { params.topMargin = this.dpToPx(context) }
            rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
            bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
            requestLayout()
        }
    }

    private fun pxFromDp(dp: Float, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}