package app.music.utils.sharepreferences

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import app.music.model.OnlinePlaylistContainer
import app.music.model.PlaylistContainer
import com.google.gson.Gson
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

object SharedPrefMethodUtils {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS")

    fun getSharedPreferences(contextReference: WeakReference<Context>): SharedPreferences {
        return contextReference.get()!!.getSharedPreferences(
                SharedPrefConstantUtils.PREF_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun <T, C> getList(contextReference: WeakReference<Context>, listType: Class<T>,
                       containerClassName: Class<C>, valueKey: String): MutableList<T> {

        val sharedPreferences = getSharedPreferences(contextReference)
        val valueString = sharedPreferences.getString(valueKey, "")

        var dataList: MutableList<T> = ArrayList()
        if (TextUtils.isEmpty(valueString)) return dataList
        val container = Gson().fromJson(valueString, containerClassName)
        if (container is PlaylistContainer) {
            val tempList = (container as PlaylistContainer).playlistList
            if (null == tempList || tempList.isEmpty()) return dataList
            dataList = ArrayList((tempList as Collection<T>?)!!)
        } else if (container is OnlinePlaylistContainer) {
            val tempList = (container as OnlinePlaylistContainer).playlistList
            if (null == tempList || tempList.isEmpty()) return dataList
            dataList = ArrayList((tempList as Collection<T>?)!!)
        }
        return dataList
    }
}
