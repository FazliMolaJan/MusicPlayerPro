package app.music.ui.screen.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.SystemClock
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import app.music.R
import app.music.adapter.viewpager.HomePagerAdapter
import app.music.base.BaseBottomProgressActivity
import app.music.databinding.ActivityHomeBinding
import app.music.listener.ToolbarScrollFlagListener
import app.music.listener.dialoglistener.DialogAddToPlaylistListener
import app.music.listener.dialoglistener.DialogHomeItemSortListener
import app.music.listener.dialoglistener.DialogSongOptionListener
import app.music.listener.homefragmentlistener.*
import app.music.model.entity.BaseMusik
import app.music.model.entity.OnlineMusik
import app.music.ui.screen.equalizer.EqualizerActivity
import app.music.ui.screen.setting.SettingActivity
import app.music.utils.ConstantUtil
import app.music.utils.DoubleClickUtils
import app.music.utils.TabTitleUtils
import app.music.utils.blur.BlurImageUtils
import app.music.utils.blur.DynamicBlurUtils
import app.music.utils.color.AttributeColorUtils
import app.music.utils.imageloading.ImageLoadingUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.log.InformationLogUtils
import app.music.utils.musicloading.LoadMusicUtil
import app.music.viewmodel.HomeActivityViewModel
import br.com.mauker.materialsearchview.MaterialSearchView
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import eightbitlab.com.blurview.BlurView
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by jacky on 3/23/18.
 */

class HomeActivity : BaseBottomProgressActivity<ActivityHomeBinding>(),
        NavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener,
        DialogAddToPlaylistListener,
        DialogSongOptionListener,
        DialogHomeItemSortListener,
        View.OnClickListener,
        ToolbarScrollFlagListener {

    override var mToolbar: BlurView? = null
    override var mAppBarLayout: AppBarLayout? = null

    override var mSongOptionDialog: BottomSheetDialog? = null

    override var mAlbumSortDialog: BottomSheetDialog? = null
    override var mSongSortDialog: BottomSheetDialog? = null
    override var mArtistSortDialog: BottomSheetDialog? = null
    override var mGenreSortDialog: BottomSheetDialog? = null
    override var mPlaylistSortDialog: BottomSheetDialog? = null
    override var mMenuSortDialog: BottomSheetDialog? = null
    override val rootViewId: Int = R.id.coordinator_background
    private val REQ_CODE_SPEECH_INPUT = 100
    private var mLastClickTime: Long = 0
    //    private AddPlaylistAsyncTask mAddPlaylistAsyncTask;
    private val mPlaylistBottomDialog: BottomSheetDialog? = null
    override var mAlbumFragmentListener: AlbumFragmentListener? = null
    override var mSongFragmentListener: SongFragmentListener? = null
    override var mArtistFragmentListener: ArtistFragmentListener? = null
    override var mGenreFragmentListener: GenreFragmentListener? = null
    override var mPlaylistFragmentListener: PlaylistFragmentListener? = null
    private var mFolderFragmentListener: FolderFragmentListener? = null
    private val mRequestOptions = RequestOptions().error(R.drawable.bg_main_color)
    private var mNavigationItemLastReselectedTime: Long = 0

    @Inject
    lateinit var mHomeActivityViewModel: HomeActivityViewModel

    private val mNavigationItemReselectedListener =
            BottomNavigationView.OnNavigationItemReselectedListener { menuItem ->
                if (DoubleClickUtils.isDoubleClick(mNavigationItemLastReselectedTime)) {
                    setPinToolbar()
                    when (menuItem.itemId) {
                        R.id.navigation_album -> {
                            (mAlbumFragmentListener!! as BaseHomeFragment<*, *, *, *, *>).onScrollToTop()
                        }
                        R.id.navigation_song -> {
                            (mSongFragmentListener!! as BaseHomeFragment<*, *, *, *, *>).onScrollToTop()
                        }
                        R.id.navigation_artist -> {
                            (mArtistFragmentListener!! as BaseHomeFragment<*, *, *, *, *>).onScrollToTop()
                        }
                        R.id.navigation_genre -> {
                            (mGenreFragmentListener!! as BaseHomeFragment<*, *, *, *, *>).onScrollToTop()
                        }
                        R.id.navigation_playlist -> {
                            (mPlaylistFragmentListener!! as BaseHomeFragment<*, *, *, *, *>).onScrollToTop()
                        }
                    }
                }
                mNavigationItemLastReselectedTime = SystemClock.elapsedRealtime()
            }

    private val mNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                setPinToolbar()
                with(mBinding.container) {
                    when (item.itemId) {
                        R.id.navigation_album -> {
                            currentItem = 0
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navigation_song -> {
                            currentItem = 1
                            Timber.i("song tab selected")
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navigation_artist -> {
                            currentItem = 2
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navigation_genre -> {
                            currentItem = 3
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navigation_playlist -> {
                            currentItem = 4
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                }
            }

    override fun onResume() {
        super.onResume()
        mBinding.searchView.activityResumed()
    }

    override fun logServiceConnected() {
        InformationLogUtils.logServiceConnected(TAG)
    }

    override fun logServiceDisconnected() {
        InformationLogUtils.logServiceDisconnected(TAG)
    }

    override fun setLastState(music: BaseMusik) {
        setBackground(music)
        super.setLastState(music)
    }

    override fun initInject() = activityComponent.inject(this)

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun getLogTag(): String = TAG

    override fun getOptionMenuId(): Int = R.menu.activity_home

    override fun createOptionMenu(menu: Menu) {
        //        MenuItem itemSearch = menu.findItem(R.id.search);
        //        SearchView searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);

        //        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_sort -> showMenuSortDialog()
            R.id.option_search -> mBinding.searchView.openSearch()
        }
        return super.onOptionsItemSelected(item)
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
            navView.setNavigationItemSelectedListener(this@HomeActivity)
            DynamicBlurUtils.blurView(this@HomeActivity, rootViewId, blurBottomNavigation, blurToolBar)
        }
        mToolbar = blurToolBar
        mAppBarLayout = appBarHome
        setupSearchView()
        super.initView()
        setupActionBar()
        setupDrawer()
        //        mBinding.toolBar.setIconToolBarLeft(R.drawable.ic_menu_white_24dp);
        //        mBinding.toolBar.setTitleApp(getString(R.string.library));
        //        mBinding.toolBar.setOnClickItemIconToolBar(this);
        setupViewPager()
        container.addOnPageChangeListener(this@HomeActivity)
        // todo BottomNavigationView.OnNavigationItemSelectedListener will get called twice
        //  if both setupViewPager() and container.addOnPageChangeListener(this@HomeActivity) are called
        setupBottomNavigation()
    }

    override fun initData() {
        editMenu()
    }

    private fun editMenu() {
        val menu = mBinding.navView.menu
        with(menu) {
            findItem(R.id.nav_albums).title = "Albums (" + LoadMusicUtil.sAlbumList.size + ")"
            findItem(R.id.nav_songs).title = "Songs (" + LoadMusicUtil.sMusicList.size + ")"
            findItem(R.id.nav_artist).title = "Artists (" + LoadMusicUtil.sArtistList.size + ")"
            findItem(R.id.nav_genres).title = "Genres (" + LoadMusicUtil.sGenreList.size + ")"
            findItem(R.id.nav_playlists).title = "Playlists (" + LoadMusicUtil.sPlaylistList.size + ")"
        }
    }

    override fun onBackPressed() {
        with(mBinding) {
            when {
                drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                searchView.isOpen -> searchView.closeSearch()
                else -> super.onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //            case REQ_CODE_SPEECH_INPUT: {
            //                if (resultCode == RESULT_OK && null != data) {
            //                    ArrayList<String> result = data
            //                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //                    //txtSpeechInput.setText(result.get(0));
            //                }
            //                break;
            //            }

            MaterialSearchView.REQUEST_VOICE -> {
                if (resultCode != Activity.RESULT_OK || data == null) return
                val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (null == matches || matches.size == 0) return
                val searchWord = matches[0]
                if (TextUtils.isEmpty(searchWord)) return
                mBinding.searchView.setQuery(searchWord, false)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        with(mBinding) {
            with(container) {
                when (item.itemId) {
                    R.id.nav_online_music -> {
                        IntentMethodUtils.launchOnlineHomeActivity(this@HomeActivity, true)
                    }
                    R.id.nav_favorite -> {
                        IntentMethodUtils.launchDetailFavoriteActivity(this@HomeActivity)
                    }
                    R.id.nav_albums -> currentItem = 0
                    R.id.nav_songs -> currentItem = 1
                    R.id.nav_artist -> currentItem = 2
                    R.id.nav_genres -> currentItem = 3
                    R.id.nav_playlists -> currentItem = 4
                    R.id.nav_folders -> currentItem = 5
                    R.id.nav_equalizer -> openActivity(EqualizerActivity::class.java, null)
                    R.id.nav_sleep_timer -> {
                    }
                    R.id.nav_share -> {
                    }
                    R.id.nav_feedback -> {
                    }
                    R.id.nav_we_are_social -> {
                    }
                    R.id.nav_settings -> openActivity(SettingActivity::class.java, null)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (position < 5) {
            selectBottomNavigationItem(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun updateSongInfo() {
        val music = mMusicService.playingSong
        setBackground(music)
        super.updateSongInfo()
    }

    fun setFolderFragmentListener(folderFragmentListener: FolderFragmentListener) {
        this.mFolderFragmentListener = folderFragmentListener
    }

    private fun loadDefaultBackGround(imageView: ImageView) {
        ImageLoadingUtils.loadImage(imageView, R.drawable.bg_main_color)
    }

    private fun setupBottomNavigation() {
        with(binding.bottomNavigation) {
            setOnNavigationItemSelectedListener(mNavigationItemSelectedListener)
            setOnNavigationItemReselectedListener(mNavigationItemReselectedListener)
            itemIconTintList = null
//            post(Runnable {
//                run {
//                    //                cardViewMusicProgress.setPadding(0, 0, 0, mBinding.bottomNavigation.measuredHeight)
//                }
//            })
        }
        selectBottomNavigationItem(1)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupDrawer() {
        with(binding) {
            drawerLayout.addDrawerListener(
                    ActionBarDrawerToggle(this@HomeActivity, drawerLayout, toolBar,
                            R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close)
                            .apply {
                                drawerArrowDrawable.color = AttributeColorUtils.getColor(
                                        this@HomeActivity, R.attr.toolbar_text_color)
                                syncState()
                            }
            )
        }
    }

    private fun setupSearchView() {
        with(binding.searchView) {
            setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
//                mBinding.searchView.closeSearch()
//                mBinding.searchView.setCloseOnTintClick(false)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    with(mHomeActivityViewModel) {
                        if (newText.isNotEmpty()) {
                            setSearchingText(newText)
                            setSearching(true)
                        } else {
                            setSearchingText(null)
                            setSearching(false)
                        }
                    }
                    return true
                }
            })

            setSearchViewListener(object : MaterialSearchView.SearchViewListener {
                override fun onSearchViewOpened() {

                }

                override fun onSearchViewClosed() {

                }
            })

            setOnVoiceClickedListener { askSpeechInput() }
        }
    }

    private fun setBackground(music: BaseMusik?) {
        if (music == null) return
        val musicType = music.type
        if (TextUtils.isEmpty(musicType)) return
        with(mBinding) {
            when (musicType) {
                ConstantUtil.OFFLINE_MUSIC -> {
                    val songLocation = music.location
                    if (!TextUtils.isEmpty(songLocation)) {
                        mMetadataRetriever.setDataSource(songLocation)
                        val bytes = mMetadataRetriever.embeddedPicture
                        if (bytes != null) {
                            BlurImageUtils.blurImage(this@HomeActivity, mCompositeDisposable, bytes,
                                    imageHomeBackground)
                        } else {
                            loadDefaultBackGround(imageHomeBackground)
                        }
                    }
                }
                ConstantUtil.ONLINE_MUSIC -> {
                    val coverArtLink = (music as OnlineMusik).coverArt
                    if (!TextUtils.isEmpty(coverArtLink)) {
                        BlurImageUtils.blurImage(this@HomeActivity, mCompositeDisposable,
                                coverArtLink, mRequestOptions, imageHomeBackground)
                    } else {
                        loadDefaultBackGround(imageHomeBackground)
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun setupViewPager() {
        with(mBinding.container) {
            adapter = HomePagerAdapter(supportFragmentManager)
            offscreenPageLimit = TabTitleUtils.HOME_TAB_TITLE.size
        }
    }

    private fun askSpeechInput() {
        try {
            startActivityForResult(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                        putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))
                    },
                    MaterialSearchView.REQUEST_VOICE)
        } catch (a: ActivityNotFoundException) {
            a.printStackTrace()
            Toast.makeText(this,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectBottomNavigationItem(position: Int) {
        val menuItem = mBinding.bottomNavigation.menu.getItem(position)
        menuItem.isChecked = true
        mNavigationItemSelectedListener.onNavigationItemSelected(menuItem)
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}
