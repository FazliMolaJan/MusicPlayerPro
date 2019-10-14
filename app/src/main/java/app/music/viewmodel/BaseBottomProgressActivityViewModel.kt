package app.music.viewmodel

import androidx.lifecycle.ViewModel
import android.os.Handler
import app.music.base.contract.BaseBottomProgressActivityContract
import app.music.utils.log.InformationLogUtils

class BaseBottomProgressActivityViewModel(
        private var baseBottomProgressActivity: BaseBottomProgressActivityContract.View,
        var mRunnable: Runnable) : ViewModel() {

    private var mHandler = Handler()
    private val mLogTag = "BaseBottomProgressActivityViewModel"

    fun initRunnable() {
//        mRunnable = object : Runnable {
//            override fun run() {
//                logRunnableIsRunning()
//                if (mMusicService != null && mMusicService.isRunning) {
//                    if (mMusicBound && mMusicService.exoPlayer != null) {
//                        when (mMusicService.notificationState) {
//                            -1 -> {
//                                if (mOldNotificationState != mMusicService.notificationState) {
//                                    if (mMusicService.isPlaying) {
//                                        pausePlayer()
//                                    } else {
//                                        loadLastState()
//                                    }
//                                    mOldNotificationState = -1
//                                }
//                            }
//                            1 -> {
//                                mOldNotificationState = 1
//                                if (mOldLocation != mMusicService.getPlayingSongLocation()) {
//                                    mOldLocation = mMusicService.getPlayingSongLocation()
//                                    updateSongInfo()
//                                    mOldPlayerState = !mMusicService.isPlaying
//                                }
//                                if (mOldPlayerState != mMusicService.isPlaying) {
//                                    setPlayPauseImageResource()
//                                    mOldPlayerState = mMusicService.isPlaying
//                                }
//
//                                mBottomProgressBar!!.progress = mMusicService.playerCurrentPosition.toInt()
//                            }
//                        }
//                    }
//                    mHandler.postDelayed(this, 1000)
//                } else {
//                    logServiceIsNotStarted()
//                    baseBottomProgressActivity.loadLastState()
//                }
//            }
//        }
    }

    private fun logRunnableIsRunning() {
        InformationLogUtils.logRunnableIsRunning(mLogTag)
    }

    private fun logServiceIsNotStarted() {
        InformationLogUtils.logServiceIsNotStarted(mLogTag)
    }
}