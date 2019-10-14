package app.music.utils.log

import timber.log.Timber

object InformationLogUtils {

    private fun log(tag: String, message: String) = Timber.i("$tag: $message")

    //Lifecycle Log
    fun logOnAttach(tag: String) = log(tag, "OnAttach")

    fun logOnCreate(tag: String) = log(tag, "OnCreate")

    fun logOnCreateView(tag: String) = log(tag, "OnCreateView")

    fun logOnActivityCreated(tag: String) = log(tag, "OnActivityCreated")

    fun logOnStart(tag: String) = log(tag, "OnStart")

    fun logOnResume(tag: String) = log(tag, "OnResume")

    fun logOnPause(tag: String) = log(tag, "OnPause")

    fun logOnStop(tag: String) = log(tag, "OnStop")

    fun logOnDestroyView(tag: String) = log(tag, "OnDestroyView")

    fun logOnDestroy(tag: String) = log(tag, "OnDestroy")

    fun logOnDetach(tag: String) = log(tag, "OnDetach")

    fun logOnRestart(tag: String) = log(tag, "OnRestart")

    //Service Log
    fun logOnBind(tag: String) = log(tag, "OnBind")

    fun logOnUnbind(tag: String) = log(tag, "OnUnbind")

    fun logOnRebind(tag: String) = log(tag, "OnRebind")

    fun logServiceConnected(tag: String) = log(tag, "Service Bound")

    fun logServiceDisconnected(tag: String) = log(tag, "Service Unbound")

    fun logServiceIsNotStarted(tag: String) = log(tag, "Service Is Not Started")

    fun logServiceIsNotRunning(tag: String) = log(tag, "Service Is Not Running")

    //Notification Log
    fun logNotificationIsPositiveState(tag: String) = log(tag, "Notification State =1")

    fun logNotificationIsNegativeState(tag: String) = log(tag, "Notification State = -1")

    //Other Log
    fun logRunnableIsRunning(tag: String) = log(tag, "Runnable Is Running")

    fun logCoverArtWasSent(tag: String) = log(tag, "Cover Art Was Sent")

    fun logSetDefaultBackground(tag: String) = log(tag, "Set Default Background")

    fun logGetAllListMusicDone(tag: String) = log(tag, "Get All List Music Done")

    fun logMusicThreadStart(tag: String) = log(tag, "Music Thread Start")

    fun logOnlineMusicThreadStart(tag: String) = log(tag, "Online Music Thread Start")

    fun logSongThreadStart(tag: String) = log(tag, "logSongThreadStart")

    fun logAlbumThreadStart(tag: String) = log(tag, "logAlbumThreadStart")

    fun logOnlineSongThreadStart(tag: String) = log(tag, "Song Thread Start")

    fun logArtistThreadStart(tag: String) = log(tag, "logArtistThreadStart")

    fun logOnlineArtistThreadStart(tag: String) = log(tag, "logOnlineArtistThreadStart")

    fun logOnlineAlbumThreadStart(tag: String) = log(tag, "logOnlineAlbumThreadStart")

    fun logGenreThreadStart(tag: String) = log(tag, "logGenreThreadStart")

    fun logFolderThreadStart(tag: String) = log(tag, "logFolderThreadStart")

    fun logOnlineGenreThreadStart(tag: String) = log(tag, "logOnlineGenreThreadStart")

    fun logPlaylistThreadStart(tag: String) = log(tag, "logPlaylistThreadStart")

    fun logOnlinePlaylistThreadStart(tag: String) = log(tag, "logOnlinePlaylistThreadStart")

    fun logFavoriteThreadStart(tag: String) = log(tag, "logFavoriteThreadStart")
}
