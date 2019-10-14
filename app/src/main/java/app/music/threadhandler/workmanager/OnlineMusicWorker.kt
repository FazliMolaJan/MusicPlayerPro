package app.music.threadhandler.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import app.music.utils.log.InformationLogUtils
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.sort.SortMethodUtils

class OnlineMusicWorker(private var context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    val TAG = "OfflineMusicWorker"
    private var mContext: Context = context

    override fun doWork(): Result {
        try {
            InformationLogUtils.logOnlineMusicThreadStart(TAG)
            LoadMusicUtil.getOnlineMusic(null)
            SortMethodUtils.sortOnlineSongList(context)

//            // Get the input
//            val imageUriInput = inputData.getString(Constants.KEY_IMAGE_URI)
//
//            // Do the work
//            val response = upload(imageUriInput)
//
//            // Create the output of the work
//            val imageResponse = response.body()
//            val imgLink = imageResponse.data.link
//
//            // workDataOf (part of KTX) converts a list of pairs to a [Data] object.
            val outputData = workDataOf(
                    "GET_ONLINE_MUSIC_FINISHED" to LoadMusicUtil.sLoadOnlineMusicStatus
            )
            Log.i(TAG, "GET_ONLINE_MUSIC_SUCCESS= ${LoadMusicUtil.sLoadOnlineMusicStatus}")
            return Result.success(outputData)
//
        } catch (e: Exception) {
            Log.i(TAG, "GET_ONLINE_MUSIC_FAILED= ${LoadMusicUtil.sLoadOnlineMusicStatus}")
            return Result.failure()
        }
    }
}