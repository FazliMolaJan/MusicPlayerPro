package app.music.base

import android.content.Intent
import android.os.SystemClock
import android.text.TextUtils
import app.music.R
import app.music.adapter.AlbumSongAdapter
import app.music.databinding.ActivityDetailAlbumBinding
import app.music.model.BaseMusik
import app.music.model.OnlineMusik
import app.music.utils.ConstantUtil
import app.music.utils.DoubleClickUtils
import app.music.utils.gradient.GradientBackgroundUtils
import app.music.utils.imageloading.ImageLoadingUtils
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.toast.ToastUtil
import com.bumptech.glide.request.RequestOptions
import java.lang.ref.WeakReference
import java.util.*

abstract class BaseSingleRecyclerActivity<ObjectType>
    : BaseBottomProgressActivity<ActivityDetailAlbumBinding>() {

    private var mMusicList: List<BaseMusik> = ArrayList()
    private var mLastClickTime: Long = 0
    private val mRequestOptions = RequestOptions().error(R.drawable.ic_album)
    private lateinit var mRecyclerAdapter: AlbumSongAdapter
    override val rootViewId = R.id.relative_detail_album

    override fun onDestroy() {
        super.onDestroy()
        mBinding.recyclerView.adapter = null
    }

    override fun getLayoutId() = R.layout.activity_detail_album

    override fun initView() {
        mButtonPlayPause = mBinding.btnPlayPause
        mTextPlayingArtist = mBinding.txtPlayingArtist
        mTextPlayingSongName = mBinding.txtPlayingSongName
        mImagePlayingCover = mBinding.imgPlayingCover
        mBottomProgressBar = mBinding.bottomProgressBar
        mRelativeBottomBar = mBinding.bottomBar
        mButtonNext = mBinding.btnNext
        mButtonPrev = mBinding.btnPrev
        mBlurBottomBar = mBinding.blurBottom
        super.initView()

        setSupportActionBar(mBinding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val intent = intent
        var `object`: ObjectType? = null
        if (intent != null) {
            `object` = getDataObject(intent)
        }

        val toolbarTitle = getToolbarTitle(`object`)
        if (!TextUtils.isEmpty(toolbarTitle)) {
            supportActionBar!!.title = toolbarTitle
        }

        mMusicList = ArrayList(getDataList(`object`))
        if (mMusicList.isNotEmpty()) {
            val music = mMusicList[0]
            val musicType = music.type
            if (!TextUtils.isEmpty(musicType)) {
                when (musicType) {
                    ConstantUtil.OFFLINE_MUSIC -> {
                        val songLocation = music.location
                        if (!TextUtils.isEmpty(songLocation)) {
                            mMetadataRetriever.setDataSource(songLocation)
                            val bytes = mMetadataRetriever.embeddedPicture
                            if (bytes != null) {
                                GradientBackgroundUtils.createGradientBackground(
                                        this, mCompositeDisposable, bytes, mBinding.coordinatorBackground
                                )
                            }
                            ImageLoadingUtils.loadImage(mBinding.imgCoverArt, bytes, mRequestOptions)
                        }
                    }
                    ConstantUtil.ONLINE_MUSIC -> {
                        val coverArtLink = (music as OnlineMusik).coverArt
                        if (!TextUtils.isEmpty(coverArtLink)) {
                            GradientBackgroundUtils.createGradientBackground(
                                    this, mCompositeDisposable, coverArtLink, mBinding.coordinatorBackground
                            )
                        }
                        ImageLoadingUtils.loadImage(mBinding.imgCoverArt, coverArtLink, mRequestOptions)
                    }
                    else -> {
                    }
                }
            }
        }

        RecyclerViewUtils.setVerticalLinearLayout(mBinding.recyclerView, this, true, true)
    }

    override fun initData() {
        mRecyclerAdapter = AlbumSongAdapter(
                WeakReference(this),
                this::onSongClick,
                this::onSongLongClick
        )
        mBinding.recyclerView.adapter = mRecyclerAdapter
        mRecyclerAdapter.updateItems(false, mMusicList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onSongClick(musik: BaseMusik) {
        if (DoubleClickUtils.isDoubleClick(mLastClickTime)) return
        mLastClickTime = SystemClock.elapsedRealtime()
        mMusicService.setList(mMusicList)
        playPickedSong(musik)
    }

    private fun onSongLongClick(musik: BaseMusik) {
        if (DoubleClickUtils.isDoubleClick(mLastClickTime)) return
        mLastClickTime = SystemClock.elapsedRealtime()
        ToastUtil.showToast("Song Long click${musik.title}")
    }

    protected abstract fun getDataObject(intent: Intent): ObjectType?

    protected abstract fun getToolbarTitle(`object`: ObjectType?): String?

    protected abstract fun getDataList(`object`: ObjectType?): List<BaseMusik>

    companion object {

        private val TAG = "BaseSingleRecyclerActivity"
    }
}
