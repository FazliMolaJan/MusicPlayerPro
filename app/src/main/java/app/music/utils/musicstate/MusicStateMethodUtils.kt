package app.music.utils.musicstate

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import app.music.model.BaseMusik
import app.music.model.Music
import app.music.model.OnlineMusik
import app.music.utils.ConstantUtil
import app.music.utils.sharepreferences.SharedPrefMethodUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.lang.ref.WeakReference
import java.util.*

object MusicStateMethodUtils {

    private var mEditor: SharedPreferences.Editor? = null
    private val mGson = Gson()

    fun getLastPlayedSong(contextReference: WeakReference<Context>): BaseMusik? {
        val sharedPreferences = SharedPrefMethodUtils.getSharedPreferences(contextReference)
        val lastMusicObject = sharedPreferences.getString(
                MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT, "")
        if (TextUtils.isEmpty(lastMusicObject)) return null
        return if (isLastPlayedSongAOnlineMusic(contextReference)) {
            mGson.fromJson(lastMusicObject, OnlineMusik::class.java)
        } else {
            mGson.fromJson(lastMusicObject, Music::class.java)
        }
    }

    private fun isLastPlayedSongAOnlineMusic(contextReference: WeakReference<Context>): Boolean {
        val sharedPreferences = SharedPrefMethodUtils.getSharedPreferences(contextReference)
        val objectType = sharedPreferences.getString(
                MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT_TYPE, "")
        return objectType == MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT_TYPE_ONLINE_MUSIC
    }

    fun getLastCountTime(contextReference: WeakReference<Context>): Long {
        val sharedPreferences = SharedPrefMethodUtils.getSharedPreferences(contextReference)
        return sharedPreferences.getLong(MusicStateConstantUtils.PREF_LAST_COUNT_TIME, 0)
    }

    fun getLastPlayedMusicList(contextReference: WeakReference<Context>): List<BaseMusik> {
        val sharedPreferences = SharedPrefMethodUtils.getSharedPreferences(contextReference)
        val valueString = sharedPreferences.getString(MusicStateConstantUtils.PREF_LAST_MUSIC_LIST, "")
        val dataList = ArrayList<BaseMusik>()
        if (TextUtils.isEmpty(valueString)) return dataList
        val musicDataArray = JsonParser().parse(valueString!!).asJsonArray
        if (musicDataArray == null || musicDataArray.size() < 1) return dataList

//        for (json in musicDataArray) {
//            json?.let {
//                if (json.toString().contains(ConstantUtil.OFFLINE_MUSIC)) {
//                    dataList.add(mGson.fromJson(json, Music::class.java))
//                } else {
//                    dataList.add(mGson.fromJson(json, OnlineMusik::class.java))
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

    fun saveLastState(
            contextReference: WeakReference<Context>, musicList: List<BaseMusik>, lastCountTime: Long?,
            music: BaseMusik, replayState: Boolean, shuffleState: Boolean, objectType: String) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }

        val musicObjectString = mGson.toJson(music)
        mEditor!!.putLong(MusicStateConstantUtils.PREF_LAST_COUNT_TIME, lastCountTime!!)
        mEditor!!.putString(MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT, musicObjectString)
        mEditor!!.putString(MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT_TYPE, objectType)
        mEditor!!.putBoolean(MusicStateConstantUtils.PREF_REPLAY_STATE, replayState)
        mEditor!!.putBoolean(MusicStateConstantUtils.PREF_SHUFFLE_STATE, shuffleState)
        saveLastPlayedMusicList(contextReference, musicList)
    }

    fun saveLastType(contextReference: WeakReference<Context>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        mEditor!!.putString(MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT_TYPE, MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT_TYPE_ONLINE_MUSIC)
        mEditor!!.apply()
    }

    private fun saveLastPlayedMusicList(
            contextReference: WeakReference<Context>, musicList: List<BaseMusik>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        //        LastMusicListContainer container = new LastMusicListContainer(musicList);
        val json = mGson.toJson(musicList)
        mEditor!!.putString(MusicStateConstantUtils.PREF_LAST_MUSIC_LIST, json)
        mEditor!!.apply()
    }
}
