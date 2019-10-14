package app.music.threadhandler.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.music.utils.log.InformationLogUtils
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.sort.SortMethodUtils

class OfflineArtistListWorker(private var context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    val TAG = "OfflineMusicWorker"
    private val mContext = context

    override fun doWork(): Result {
        return try {
            val getAllOfflineSongResult = inputData
                    .getBoolean("GET_MUSIC_FINISHED", false)
            if (getAllOfflineSongResult) {
                InformationLogUtils.logArtistThreadStart(TAG)
//            LoadMusicUtil.getArtistWithContext(this@SplashActivity, true)
                LoadMusicUtil.getArtist(mContext, true)
                SortMethodUtils.sortArtistList(context)
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}