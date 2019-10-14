package app.music.utils.snackbar

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import app.music.utils.theme.ThemeMethodUtils
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference


object SnackbarUtils {

    fun showLengthShortSnackbar(context: Context, view: View, message: String) {
        showSnackbar(context, view, message, Snackbar.LENGTH_SHORT)
    }

    fun showLengthLongSnackbar(context: Context, view: View, message: String) {
        showSnackbar(context, view, message, Snackbar.LENGTH_LONG)
    }

    fun showLengthIndefiniteSnackbar(context: Context, view: View, message: String) {
        showSnackbar(context, view, message, Snackbar.LENGTH_INDEFINITE)
    }

    private fun showSnackbar(context: Context, view: View, message: String, duration: Int) {
        if (ThemeMethodUtils.isDarkModeEnabled(WeakReference(context))) {
            showSnackbar(view, message, duration, Color.BLACK, Color.WHITE, "#40E0D0")
        } else {
            showSnackbar(view, message, duration, Color.WHITE, Color.BLACK, "#4169E1")
        }
    }

    private fun showSnackbar(rootView: View, message: String, duration: Int, backgroundColor: Int,
                             textColor: Int, actionTextColor: String) {
        with(Snackbar.make(rootView, message, duration)) {
            with(view) {
                setBackgroundColor(backgroundColor)
                findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        .setTextColor(textColor)
            }
            setAction("DISMISS") { dismiss() }
            setActionTextColor(Color.parseColor(actionTextColor))
            show()
        }
    }
}
