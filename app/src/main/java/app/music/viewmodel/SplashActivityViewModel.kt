package app.music.viewmodel

import android.os.Handler
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkContinuation
import androidx.work.WorkManager
import app.music.base.BaseNormalViewModel
import app.music.base.contract.SplashActivityContract
import app.music.threadhandler.workmanager.*
import app.music.utils.WorkerUtils

class SplashActivityViewModel
    : BaseNormalViewModel(),
        SplashActivityContract.ViewModel {

    var mLoadingMusicDoneWorkRequest = OneTimeWorkRequest
            .Builder(LoadingMusicDoneWorker::class.java)
            .build()
    private var mMusicLoadingStarted = false
    private var mHandler = Handler()
    var mStoragePermissionGranted = false

    override fun onCleared() {
        super.onCleared()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun loadMusic() {
        mHandler.post(object : Runnable {
            override fun run() {
                if (mStoragePermissionGranted && !mMusicLoadingStarted) {
                    mMusicLoadingStarted = true
                    getAllMusicList()
                    mHandler.removeCallbacksAndMessages(null)
                }
                mHandler.postDelayed(this, 100)
            }
        })
    }

    private fun getAllMusicList() {
        with(WorkerUtils) {
            val onlineMusicChain = WorkManager.getInstance()
                    .beginWith(buildOneTimeWorkRequest<OnlineMusicWorker>())
                    .then(listOf(
                            buildOneTimeWorkRequest<OnlineAlbumListWorker>(),
                            buildOneTimeWorkRequest<OnlineArtistListWorker>(),
                            buildOneTimeWorkRequest<OnlineGenreListWorker>(),
                            buildOneTimeWorkRequest<OnlinePlaylistListWorker>()))
            val offlineMusicChain = WorkManager.getInstance()
                    .beginWith(buildOneTimeWorkRequest<OfflineMusicWorker>())
                    .then(listOf(
                            buildOneTimeWorkRequest<OfflineAlbumListWorker>(),
                            buildOneTimeWorkRequest<OfflineArtistListWorker>(),
                            buildOneTimeWorkRequest<OfflineGenreListWorker>(),
                            buildOneTimeWorkRequest<OfflinePlaylistListWorker>(),
                            buildOneTimeWorkRequest<OfflineFolderListWorker>(),
                            buildOneTimeWorkRequest<OfflineFavortieListWorker>()))
            WorkContinuation
                    .combine(listOf(onlineMusicChain, offlineMusicChain))
                    .then(mLoadingMusicDoneWorkRequest)
                    .enqueue()
        }
    }
}