package app.music.utils.toast

import android.widget.Toast

import app.music.base.BaseApplication

object ToastUtil {

    private var toast: Toast? = null

    fun showToast(content: String) {
        if (BaseApplication.getApplication() == null) return
        toast?.cancel()
        toast = Toast.makeText(BaseApplication.getApplication(), content, Toast.LENGTH_SHORT)
        toast?.show()
        toast = null
    }
}
