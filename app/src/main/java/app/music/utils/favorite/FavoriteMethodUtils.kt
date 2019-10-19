package app.music.utils.favorite

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import app.music.model.entity.BaseMusik
import app.music.model.entity.Music
import app.music.model.entity.OnlineMusik
import app.music.utils.ConstantUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.sharepreferences.SharedPrefMethodUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.lang.ref.WeakReference
import java.util.*

object FavoriteMethodUtils {

    private var mEditor: SharedPreferences.Editor? = null
    private val mGson = Gson()

    fun getFavoriteList(contextReference: WeakReference<Context>): List<BaseMusik> {
        val sharedPreferences = SharedPrefMethodUtils.getSharedPreferences(contextReference)
        val valueString = sharedPreferences.getString(FavoriteConstantUtils.PREF_FAVORITE_LIST, "")
        val dataList = ArrayList<BaseMusik>()
        if (TextUtils.isEmpty(valueString)) return dataList
        val jsonValue = JsonParser().parse(valueString!!) ?: return dataList
        val musicDataArray = jsonValue.asJsonArray
        if (musicDataArray == null || musicDataArray.size() == 0) return dataList
//        for (jsonElement in musicDataArray) {
//            jsonElement?.let {
//                if (jsonElement.toString().contains(ConstantUtil.OFFLINE_MUSIC)) {
//                    dataList.add(mGson.fromJson(jsonElement, Music::class.java))
//                } else {
//                    dataList.add(mGson.fromJson(jsonElement, OnlineMusik::class.java))
//                }
//            }
//        }

        musicDataArray.forEach { jsonElement ->
            jsonElement?.let {
                if (jsonElement.toString().contains(ConstantUtil.OFFLINE_MUSIC)) {
                    dataList.add(mGson.fromJson(jsonElement, Music::class.java))
                } else {
                    dataList.add(mGson.fromJson(jsonElement, OnlineMusik::class.java))
                }
            }
        }
        return dataList
    }

    private fun saveFavoriteList(contextReference: WeakReference<Context>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        val json = mGson.toJson(LoadMusicUtil.sFavoriteList)
        mEditor?.run {
            putString(FavoriteConstantUtils.PREF_FAVORITE_LIST, json)
            apply()
        }
    }

    fun editFavoriteList(contextReference: WeakReference<Context>, musik: BaseMusik): Boolean {
        var isAlreadyAFavoriteSong = false
        for (music in LoadMusicUtil.sFavoriteList) {
            if (music != null) {
                val musicLocation = music.location
                if (musicLocation != null && musicLocation == musik.location) {
                    isAlreadyAFavoriteSong = true
                    LoadMusicUtil.sFavoriteList.remove(music)
                    break
                }
            }
        }

//        run loop@{
//            LoadMusicUtil.sFavoriteList.forEach { baseMusik ->
//                baseMusik?.let {
//                    val musicLocation = baseMusik.location
//                    if (musicLocation != null && musicLocation == musik.location) {
//                        isAlreadyAFavoriteSong = true
//                        LoadMusicUtil.sFavoriteList.remove(baseMusik)
//                        return@loop
//                    }
//                }
//            }
//        }

        if (!isAlreadyAFavoriteSong) {
            LoadMusicUtil.sFavoriteList.add(musik)
        }
        if (LoadMusicUtil.sFavoriteList.size > 20) {
            LoadMusicUtil.sFavoriteList.removeAt(0)
        }
        saveFavoriteList(contextReference)
        return isAlreadyAFavoriteSong
    }
}
