package app.music.threadhandler.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.music.utils.log.InformationLogUtils
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.sort.SortMethodUtils

class OnlineArtistListWorker(private var context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    val TAG = "OfflineMusicWorker"

    override fun doWork(): Result {
        return try {
            val getAllOnlineSongResult = inputData.getString("GET_ONLINE_MUSIC_FINISHED")
            if (getAllOnlineSongResult.equals(LoadMusicUtil.LOAD_ONLINE_MUSIC_SUCCEED)) {
                InformationLogUtils.logOnlineArtistThreadStart(TAG)
                LoadMusicUtil.getOnlineArtist()
                SortMethodUtils.sortOnlineArtistList(context)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}