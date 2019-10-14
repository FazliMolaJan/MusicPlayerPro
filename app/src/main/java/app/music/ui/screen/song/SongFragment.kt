package app.music.ui.screen.song

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.SongAdapter
import app.music.base.BaseFragment
import app.music.base.ContainListViewModel
import app.music.databinding.FragmentSongBinding
import app.music.listener.dialoglistener.DialogSongOptionListener
import app.music.listener.homefragmentlistener.SongFragmentListener
import app.music.model.BaseMusik
import app.music.ui.screen.home.HomeActivity
import app.music.utils.DoubleClickUtils
import app.music.utils.dialog.songoption.DialogSongOptionMethodUtils
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils
import app.music.viewmodel.HomeActivityViewModel
import timber.log.Timber
import java.lang.ref.WeakReference

class SongFragment
    : BaseFragment<FragmentSongBinding>(),
        SwipeRefreshLayout.OnRefreshListener,
        SongFragmentListener {

    private lateinit var mSongRecyclerAdapter: SongAdapter
    private var mHomeActivityViewModel: HomeActivityViewModel? = null
    private var mLastSearchingSong: String? = ""
    override val layoutId: Int = R.layout.fragment_song
    override val logTag: String = TAG

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).mSongFragmentListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeActivityViewModel = ViewModelProviders.of(activity!!).get(HomeActivityViewModel::class.java)
        mHomeActivityViewModel!!.mIsSearching.observe(
                this,
                Observer { searching ->
                    Timber.i("onCreate: searching changed")
                    if (mIsVisibleToUser) {
                        Timber.i("onCreate: searching changed and visible to user")
                        if (searching != null && searching!!) {
                            Timber.i("onCreate: searching changed and visible to user and searching")
                            mSongRecyclerAdapter!!.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                            Timber.i("onCreate: searching changed and visible to user and searching" + mHomeActivityViewModel!!.getSearchingText()!!)
                        } else {
                            Timber.i("onCreate: searching changed and visible to user and not searching")
                            mSongRecyclerAdapter?.updateItems(false, LoadMusicUtil.sMusicList)
                        }
                    }
                }
        )
    }

    override fun onPause() {
        super.onPause()
        binding.refreshlayout.isRefreshing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerview.adapter = null
    }

    override fun initView() {
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true)
        binding.recyclerview.itemAnimator = DefaultItemAnimator()
        binding.refreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mSongRecyclerAdapter = SongAdapter(
                WeakReference<Activity>(activity),
                this::onMusicClick,
                this::onMusicLongClick
        )
        binding.recyclerview.adapter = mSongRecyclerAdapter
        mSongRecyclerAdapter.updateItems(false, LoadMusicUtil.sMusicList)
        //        String folder = new File(new File(LoadMusicUtil.sMusicList.get(0).getLocation()).getParent()).getName();
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewSongList(
                activity as Activity,
                binding.refreshlayout,
                mSongRecyclerAdapter::updateItems
        )
    }

    override fun onSortMusic(sortBy: String, isAscending: String) {
        SortMethodUtils.sortSongList(
                activity as Activity,
                sortBy,
                isAscending,
                mSongRecyclerAdapter::updateItems
        )
    }

    //    @Override
    //    public void onFilterList(String filterPattern) {
    //        mSongRecyclerAdapter.getFilter().filter(filterPattern);
    //    }

    override fun onScrollToTop() {
        binding.recyclerview.scrollToPosition(0)
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel!!.getSearching()) {
            Timber.i("onVisible: mLastSearchingAlbum!=null and searching")
            if (TextUtils.isEmpty(mHomeActivityViewModel!!.getSearchingText())) {
                Timber.i("onVisible: mLastSearchingAlbum!=null and searching and searching text not empty")
                mSongRecyclerAdapter?.updateItems(false, LoadMusicUtil.sMusicList)
            } else {
                Timber.i("onVisible: mLastSearchingAlbum!=null and searching and searching text not empty")
                if (mLastSearchingSong == null || mLastSearchingSong != mHomeActivityViewModel!!.getSearchingText()) {
                    Timber.i("onVisible: mLastSearchingAlbum!=null and searching and searching text different")
                    mSongRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                }
            }
        } else {
            Timber.i("onVisible: not searching")
            if (mLastSearchingSong != null) {
                Timber.i("onVisible: not searching and mLastSearchingAlbum != null")
                mSongRecyclerAdapter.updateItems(false, LoadMusicUtil.sMusicList)
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingSong = mHomeActivityViewModel!!.getSearchingText()
    }

    private fun onMusicClick(music: BaseMusik) {
        checkDoubleClick {
            with(activity as HomeActivity) {
                mMusicService.setList(LoadMusicUtil.sMusicList)
                playPickedSong(music)
            }
        }
    }

    private fun onMusicLongClick(music: BaseMusik) {
        DialogSongOptionMethodUtils.showSongOption(activity as DialogSongOptionListener, music)
        ToastUtil.showToast(music.title)
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils.getViewModel<ContainListViewModel>(activity as Activity)
        with(mHomeActivityViewModel) {
            if (!DoubleClickUtils.isDoubleClick(getItemLastClickTime())) {
                setItemLastClickTime(SystemClock.elapsedRealtime())
                listener()
            } else {
                setItemLastClickTime(SystemClock.elapsedRealtime())
            }
        }
    }

    companion object {

        private val TAG = "SongFragment"
    }
}
