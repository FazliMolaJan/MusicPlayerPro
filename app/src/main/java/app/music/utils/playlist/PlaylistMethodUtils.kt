package app.music.utils.playlist

import android.content.Context
import android.content.SharedPreferences
import app.music.model.entity.*
import app.music.utils.sharepreferences.SharedPrefMethodUtils
import com.google.gson.Gson
import java.lang.ref.WeakReference
import java.util.*

object PlaylistMethodUtils {

    private var mEditor: SharedPreferences.Editor? = null
    private val mGson = Gson()

    fun saveOnlinePlaylist(
            contextReference: WeakReference<Context>, allPlaylists: List<Playlist>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        //        LastMusicListContainer container = new LastMusicListContainer(musicList);
        val json = mGson.toJson(allPlaylists)
        mEditor?.run {
            putString(PlaylistConstantUtils.PREF_ALL_ONLINE_PLAYLISTS, json)
            apply()
        }
    }

    fun saveAllPlaylists(
            contextReference: WeakReference<Context>, allPlaylists: List<Playlist>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        val playlistContainer = PlaylistContainer(allPlaylists)
        val value = mGson.toJson(playlistContainer)
        mEditor?.run {
            putString(PlaylistConstantUtils.PREF_ALL_PLAYLISTS, value)
            apply()
        }
    }

    fun saveAllOnlinePlaylists(
            contextReference: WeakReference<Context>, allPlaylists: List<OnlinePlaylist>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        val playlistContainer = OnlinePlaylistContainer(allPlaylists)
        val value = mGson.toJson(playlistContainer)
        mEditor?.run {
            putString(PlaylistConstantUtils.PREF_ALL_ONLINE_PLAYLISTS, value)
            apply()
        }
    }

    fun addSongToExistedPlaylist(
            weakReference: WeakReference<Context>, song: BaseMusik?, playlistCreatedTime: Calendar) {
        val allPlaylists = SharedPrefMethodUtils.getList(
                weakReference,
                Playlist::class.java,
                PlaylistContainer::class.java,
                PlaylistConstantUtils.PREF_ALL_PLAYLISTS)
        for (playlist in allPlaylists) {
            if (playlist != null) {
                val createdTime = playlist.createdTime
                if (createdTime != null && createdTime == playlistCreatedTime) {
                    playlist.musicList.add(song)
                    break
                }
            }
        }
        saveAllPlaylists(weakReference, allPlaylists)
    }

    fun addSongToNewPlaylist(
            weakReference: WeakReference<Context>, song: BaseMusik?, playlistName: String) {
        val allPlaylists = SharedPrefMethodUtils.getList(
                weakReference,
                Playlist::class.java,
                PlaylistContainer::class.java,
                PlaylistConstantUtils.PREF_ALL_PLAYLISTS)
        val newMusicList = ArrayList<BaseMusik?>()
        newMusicList.add(song)
        val newPlaylist = Playlist(Calendar.getInstance(), playlistName,
                ArrayList(newMusicList), null, null, null, null, false)
        allPlaylists.add(newPlaylist)
        saveAllPlaylists(weakReference, allPlaylists)
    }

    fun addSongToExistedOnlinePlaylist(
            weakReference: WeakReference<Context>, song: OnlineMusik?, playlistCreatedTime: Calendar) {
        val allPlaylists = SharedPrefMethodUtils.getList(
                weakReference,
                OnlinePlaylist::class.java,
                OnlinePlaylistContainer::class.java,
                PlaylistConstantUtils.PREF_ALL_ONLINE_PLAYLISTS)
        for (playlist in allPlaylists) {
            if (playlist != null) {
                val createdTime = playlist.createdTime
                if (createdTime != null && createdTime == playlistCreatedTime) {
                    playlist.musicList.add(song)
                    break
                }
            }
        }
        saveAllOnlinePlaylists(weakReference, allPlaylists)
    }

    fun addSongToNewOnlinePlaylist(
            weakReference: WeakReference<Context>, song: OnlineMusik?, playlistName: String) {
        val allPlaylists = SharedPrefMethodUtils.getList(
                weakReference,
                OnlinePlaylist::class.java,
                OnlinePlaylistContainer::class.java,
                PlaylistConstantUtils.PREF_ALL_ONLINE_PLAYLISTS)
        val newMusicList = ArrayList<OnlineMusik?>()
        newMusicList.add(song)
        val newPlaylist = OnlinePlaylist(Calendar.getInstance(), playlistName,
                ArrayList(newMusicList), null, null, null, null, true)
        allPlaylists.add(newPlaylist)
        saveAllOnlinePlaylists(weakReference, allPlaylists)
    }

    fun deleteAllPlaylists(contextReference: WeakReference<Context>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        mEditor?.run {
            remove(PlaylistConstantUtils.PREF_ALL_PLAYLISTS)
            apply()
        }
        deleteAllOnlinePlaylists(contextReference)
    }

    fun deleteAllOnlinePlaylists(contextReference: WeakReference<Context>) {
        if (null == mEditor) {
            mEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        mEditor?.run {
            remove(PlaylistConstantUtils.PREF_ALL_ONLINE_PLAYLISTS)
            apply()
        }
    }

    fun getAllPlaylist(context: Context): MutableList<Playlist> {
        return SharedPrefMethodUtils.getList(
                WeakReference(context),
                Playlist::class.java,
                PlaylistContainer::class.java,
                PlaylistConstantUtils.PREF_ALL_PLAYLISTS)
    }

    fun getAllOnlinePlaylist(context: Context): MutableList<OnlinePlaylist> {
        return SharedPrefMethodUtils.getList(
                WeakReference(context),
                OnlinePlaylist::class.java,
                OnlinePlaylistContainer::class.java,
                PlaylistConstantUtils.PREF_ALL_ONLINE_PLAYLISTS)
    }
}
