package app.music.base

import androidx.databinding.ViewDataBinding
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.*
import app.music.R
import app.music.model.BaseMusik
import app.music.model.OnlineMusik
import app.music.ui.screen.detailplaymusic.DetailPlayMusicActivity
import app.music.utils.ConstantUtil
import app.music.utils.blur.DynamicBlurUtils
import app.music.utils.imageloading.ImageLoadingUtils
import app.music.utils.log.InformationLogUtils
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.musicstate.MusicStateConstantUtils
import app.music.utils.musicstate.MusicStateMethodUtils
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import eightbitlab.com.blurview.BlurView
import java.lang.ref.WeakReference
import java.util.*

abstract class BaseBottomProgressActivity<T : ViewDataBinding>
    : BaseMusicServiceActivity<T>(),
        View.OnClickListener {

    protected var mButtonPlayPause: ImageButton? = null
    protected var mTextPlayingArtist: TextView? = null
    protected var mTextPlayingSongName: TextView? = null
    protected var mImagePlayingCover: CircleImageView? = null
    protected var mBottomProgressBar: ProgressBar? = null
    protected var mRelativeBottomBar: RelativeLayout? = null
    protected var mButtonNext: ImageButton? = null
    protected var mButtonPrev: ImageButton? = null
    protected var mBlurBottomBar: BlurView? = null
    private val mRequestOptions = RequestOptions().error(R.drawable.ic_bottom_bar_disc)
    private val mLogTag = "BaseBottomProgressActivity"
    protected abstract val rootViewId: Int

    override fun onStart() {
        super.onStart()
        bindMusicService()
        mOldLocation = ""
        mOldQueuePosition = -1
        mOldNotificationState = 0
        mRunnable = object : Runnable {
            override fun run() {
                if (mMusicService != null && mMusicService.isRunning) {
                    if (mMusicBound && mMusicService.exoPlayer != null) {
                        when (mMusicService.notificationState) {
                            -1 -> {
                                if (mOldNotificationState != mMusicService.notificationState) {
                                    if (mMusicService.isPlaying) {
                                        pausePlayer()
                                    } else {
                                        loadLastState()
                                    }
                                    mOldNotificationState = -1
                                }
                            }
                            1 -> {
                                mOldNotificationState = 1
                                if (mOldLocation != mMusicService.getPlayingSongLocation()) {
                                    mOldLocation = mMusicService.getPlayingSongLocation()
                                    updateSongInfo()
                                    mOldPlayerState = !mMusicService.isPlaying
                                }
                                if (mOldPlayerState != mMusicService.isPlaying) {
                                    setPlayPauseImageResource()
                                    mOldPlayerState = mMusicService.isPlaying
                                }

                                mBottomProgressBar!!.progress = mMusicService.playerCurrentPosition.toInt()
                            }
                        }
                    }
                    mHandler.postDelayed(this, 1000)
                } else {
                    logServiceIsNotStarted()
                    loadLastState()
                }
            }
        }
        checkEndSong()
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onStop() {
        super.onStop()
        unbindService(mMusicConnection)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_next -> {
                with(mMusicService) {
                    if (isRunning) {
                        startService()
                        playNext()
                    } else {
                        setList(mLastMusicList)
                        setSong(mLastPlayedMusicObject)
                        playNext()
                        startService()
                        playSong()
                        checkEndSong()
                    }
                    setPlayerShuffleMode()
                    setPlayerRepeatMode()
                }
            }
            R.id.btn_prev -> {
                with(mMusicService) {
                    if (isRunning) {
                        startService()
                        playPrev()
                    } else {
                        setList(mLastMusicList)
                        setSong(mLastPlayedMusicObject)
                        playPrev()
                        startService()
                        playSong()
                        checkEndSong()
                    }
                    setPlayerShuffleMode()
                    setPlayerRepeatMode()
                }
            }
            R.id.btn_play_pause -> {
                mMusicService?.run {
                    if (isRunning) {
                        if (mMusicBound && exoPlayer != null) {
                            if (isPlaying) {
                                pausePlayer()
                            } else {
                                addPlayerListener()
                                startService()
                                playPlayer()
                            }
                        }
                    } else {
                        setList(mLastMusicList)
                        setSong(mLastPlayedMusicObject)
                        startService()
                        seekTime = mBottomProgressBar!!.progress.toLong()
                        playSong()
                        mOldLocation = ""
                        mOldQueuePosition = -1
                        checkEndSong()
                        setPlayerShuffleMode()
                        setPlayerRepeatMode()
                    }
                }
            }
            R.id.bottom_bar -> openActivity(DetailPlayMusicActivity::class.java, null)
        }
    }

    override fun initView() {
        bindMusicService()
        assignViews(mButtonNext, mButtonPrev, mButtonPlayPause, mRelativeBottomBar)
        DynamicBlurUtils.blurView(this, rootViewId, mBlurBottomBar!!)
    }

    protected open fun setLastState(music: BaseMusik) {
        with(music) {
            when (type) {
                ConstantUtil.OFFLINE_MUSIC -> loadCoverArt(location, mImagePlayingCover)
                ConstantUtil.ONLINE_MUSIC -> {
                    val coverArtLink = (this as OnlineMusik).coverArt
                    loadOnlineCoverArt(coverArtLink, mImagePlayingCover)
                }
            }
            mTextPlayingSongName!!.text = title
            mTextPlayingArtist!!.text = artist
            mBottomProgressBar!!.max = duration!!
            mBottomProgressBar!!.progress = mSharedPreferences
                    .getLong(MusicStateConstantUtils.PREF_LAST_COUNT_TIME, 0).toInt()
            mButtonPlayPause!!.setImageResource(R.drawable.ic_outline_play_arrow_48dp)
        }
    }

    protected open fun updateSongInfo() {
        if (mMusicService.songs.size > 0) {
            with(mMusicService.playingSong) {
                when (type) {
                    ConstantUtil.OFFLINE_MUSIC -> loadCoverArt(location, mImagePlayingCover)
                    ConstantUtil.ONLINE_MUSIC -> {
                        loadOnlineCoverArt((this as OnlineMusik).coverArt, mImagePlayingCover)
                    }
                }
                mTextPlayingSongName!!.text = title
                mTextPlayingArtist!!.text = artist
                mBottomProgressBar!!.max = duration!!
            }
        }
        setPlayPauseImageResource()
    }

    public fun playPickedSong(music: BaseMusik) {
        mMusicService?.run {
            if (mMusicBound) {
                setSong(music)
                mPlayIntent.action = ConstantUtil.ACTION.STARTFOREGROUND_ACTION
                startService(mPlayIntent)
                playSong()
                updateSongInfo()
                setPlayerShuffleMode()
                setPlayerRepeatMode()
                openActivity(DetailPlayMusicActivity::class.java, null)
            }
        }
    }

    private fun View.OnClickListener.assignViews(vararg views: View?) {
        views.forEach { it?.setOnClickListener(this) }
    }

    private fun logServiceIsNotStarted() {
        InformationLogUtils.logServiceIsNotStarted(mLogTag)
    }

    private fun logRunnableIsRunning() {
        InformationLogUtils.logRunnableIsRunning(mLogTag)
    }

    private fun pausePlayer() {
        mButtonPlayPause!!.setImageResource(R.drawable.ic_outline_play_arrow_48dp)
        mMusicService.pausePlayer()
    }

    private fun loadLastState() {
        mLastMusicList = ArrayList(
                MusicStateMethodUtils.getLastPlayedMusicList(WeakReference(this)))
        if (mLastMusicList.size > 0) {
            mLastPlayedMusicObject = MusicStateMethodUtils.getLastPlayedSong(WeakReference(this))
            if (mLastPlayedMusicObject == null) {
                mLastPlayedMusicObject = LoadMusicUtil.sMusicList[0]
            }
        } else {
            mLastMusicList = LoadMusicUtil.sMusicList
            mLastPlayedMusicObject = LoadMusicUtil.sMusicList[0]
        }

        val music = mLastPlayedMusicObject
        setLastState(music)
    }

    private fun checkEndSong() {
        mHandler.post(mRunnable)
    }

    private fun setPlayPauseImageResource() {
        mButtonPlayPause!!.setImageResource(
                if (mMusicService.exoPlayer.playWhenReady) R.drawable.ic_pause
                else R.drawable.ic_outline_play_arrow_48dp
        )
    }

    private fun loadCoverArt(link: String, imageView: ImageView?) {
        mMetadataRetriever.setDataSource(link)
        ImageLoadingUtils.loadImage(imageView!!, mMetadataRetriever.embeddedPicture, mRequestOptions)
    }

    private fun loadOnlineCoverArt(link: String, imageView: ImageView?) {
        ImageLoadingUtils.loadImage(imageView!!, link, mRequestOptions)
    }

    private fun playNext() {
        mMusicService.playNext()
    }

    private fun playPrev() {
        mMusicService.playPrev()
    }

    private fun playPlayer() {
        mButtonPlayPause!!.setImageResource(R.drawable.ic_pause)
        with(mMusicService) {
            addPlayerListener()
            playPlayer()
        }
    }

    private fun startService() {
        mPlayIntent.action = ConstantUtil.ACTION.STARTFOREGROUND_ACTION
        ContextCompat.startForegroundService(this, mPlayIntent)
    }
}
