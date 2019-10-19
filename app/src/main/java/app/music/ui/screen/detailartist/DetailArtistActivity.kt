package app.music.ui.screen.detailartist

import android.os.SystemClock
import android.text.TextUtils
import android.view.Menu
import android.view.View
import androidx.viewpager.widget.ViewPager
import app.music.R
import app.music.adapter.viewpager.DetailArtistPagerAdapter
import app.music.base.BaseBottomProgressActivity
import app.music.databinding.ActivityDetailArtistBinding
import app.music.listener.itemclick.AlbumFragmentItemClickListener
import app.music.model.entity.Artist
import app.music.model.entity.BaseMusik
import app.music.model.entity.OnlineMusik
import app.music.utils.ConstantUtil
import app.music.utils.DoubleClickUtils
import app.music.utils.blur.DynamicBlurUtils
import app.music.utils.gradient.GradientBackgroundUtils
import app.music.utils.imageloading.ImageLoadingUtils
import app.music.utils.intent.IntentConstantUtils
import app.music.utils.log.InformationLogUtils
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class DetailArtistActivity
    : BaseBottomProgressActivity<ActivityDetailArtistBinding>(),
        View.OnClickListener,
        AlbumFragmentItemClickListener,
        ViewPager.OnPageChangeListener {

    override val rootViewId: Int = R.id.relative_root
    private var mMusicList: List<BaseMusik> = ArrayList()
    private var mLastClickTime: Long = 0
    var mArtist: Artist? = null
    private val mRequestOptions = RequestOptions().error(R.drawable.ic_album)
    private var mNavigationItemLastReselectedTime: Long = 0
    lateinit var mDetailArtistTrackFragment: DetailArtistTrackFragment
    lateinit var mDetailArtistAlbumFragment: DetailArtistAlbumFragment

    private val mNavigationItemReselectedListener = BottomNavigationView.OnNavigationItemReselectedListener { menuItem ->
        if (DoubleClickUtils.isDoubleClick(mNavigationItemLastReselectedTime)) {
            when (menuItem.itemId) {
                R.id.navigation_song -> mDetailArtistTrackFragment.onScrollToTop()
                R.id.navigation_album -> mDetailArtistAlbumFragment.onScrollToTop()
            }
        }
        mNavigationItemLastReselectedTime = SystemClock.elapsedRealtime()
    }

    private val mNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        with(mBinding.viewPager) {
            when (item.itemId) {
                R.id.navigation_song -> {
                    currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_album -> {
                    currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                else -> {

                }
            }
        }
        false
    }

    override fun logServiceConnected() {
        InformationLogUtils.logServiceConnected(TAG)
    }

    override fun logServiceDisconnected() {
        InformationLogUtils.logServiceDisconnected(TAG)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_artist
    }

    override fun initView() {
        with(mBinding) {
            mButtonPlayPause = btnPlayPause
            mTextPlayingArtist = txtPlayingArtist
            mTextPlayingSongName = txtPlayingSongName
            mImagePlayingCover = imgPlayingCover
            mBottomProgressBar = bottomProgressBar
            mRelativeBottomBar = bottomBar
            mButtonNext = btnNext
            mButtonPrev = btnPrev
            mBlurBottomBar = blurBottom
            DynamicBlurUtils.blurView(this@DetailArtistActivity, rootViewId, blurBottomNavigation)
            super.initView()

            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
            intent?.run {
                mArtist = getParcelableExtra(IntentConstantUtils.EXTRA_ARTIST_OBJECT_TO_DETAIL_ARTIST)
                mArtist?.run {
                    supportActionBar?.title = artistName
                    mMusicList = ArrayList(musicList)
                }
                val music = mMusicList[0]
                with(music) {
                    val musicType = type
                    if (TextUtils.isEmpty(musicType)) return
                    when (musicType) {
                        ConstantUtil.OFFLINE_MUSIC -> {
                            val songLocation = location
                            if (!TextUtils.isEmpty(songLocation)) {
                                mMetadataRetriever.setDataSource(songLocation)
                                val bytes = mMetadataRetriever.embeddedPicture
                                if (bytes != null) {
                                    GradientBackgroundUtils.createGradientBackground(
                                            this@DetailArtistActivity, mCompositeDisposable, bytes,
                                            coordinatorBackground
                                    )
                                }
                                ImageLoadingUtils.loadImage(imgCoverArt, bytes, mRequestOptions)
                            }
                        }
                        ConstantUtil.ONLINE_MUSIC -> {
                            val coverArtLink = (music as OnlineMusik).coverArt
                            if (!TextUtils.isEmpty(coverArtLink)) {
                                GradientBackgroundUtils.createGradientBackground(
                                        this@DetailArtistActivity, mCompositeDisposable, coverArtLink, coordinatorBackground
                                )
                            }
                            ImageLoadingUtils.loadImage(imgCoverArt, coverArtLink, mRequestOptions)
                        }
                        else -> {
                        }
                    }
                }
                setupViewPager()
            }
            viewPager.addOnPageChangeListener(this@DetailArtistActivity)
            with(bottomNavigation) {
                setOnNavigationItemSelectedListener(mNavigationItemSelectedListener)
                setOnNavigationItemReselectedListener(mNavigationItemReselectedListener)
                itemIconTintList = null
            }
            selectBottomNavigationItem(0)
        }
    }

    override fun initInject() = activityComponent.inject(this)

    override fun getLogTag(): String {
        return TAG
    }

    override fun getOptionMenuId(): Int {
        return R.menu.activity_detail_artist
    }

    override fun createOptionMenu(menu: Menu) {

    }

    override fun initData() {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        selectBottomNavigationItem(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    private fun setupViewPager() {
        mBinding.viewPager.adapter = DetailArtistPagerAdapter(supportFragmentManager)
    }

    private fun selectBottomNavigationItem(position: Int) {
        val menuItem = mBinding.bottomNavigation.menu.getItem(position)
        menuItem.isChecked = true
        mNavigationItemSelectedListener.onNavigationItemSelected(menuItem)
    }

    companion object {

        private const val TAG = "DetailArtistActivity"
    }
}
