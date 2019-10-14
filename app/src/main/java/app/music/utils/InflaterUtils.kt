package app.music.utils

import android.app.Activity
import android.content.Context
import android.view.View
import java.lang.ref.WeakReference

object InflaterUtils {

    fun getInflatedView(contextReference: WeakReference<Context>, layoutId: Int): View {
        return (contextReference.get() as Activity).layoutInflater.inflate(layoutId, null)
    }
}