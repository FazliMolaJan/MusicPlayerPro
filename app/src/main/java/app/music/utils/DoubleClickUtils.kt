package app.music.utils

import android.os.SystemClock

object DoubleClickUtils {

    fun isDoubleClick(lastClickTime: Long): Boolean {
        return SystemClock.elapsedRealtime() - lastClickTime < 2000
    }
}