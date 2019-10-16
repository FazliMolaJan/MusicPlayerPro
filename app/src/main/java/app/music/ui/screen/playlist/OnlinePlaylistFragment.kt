package app.music.ui.screen.playlist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.OnlinePlaylistAdapter
import app.music.base.BaseFragment
import app.music.databinding.FragmentPlaylistBinding
import app.music.listener.itemclick.OnlinePlaylistItemClickListener
import app.music.listener.homefragmentlistener.PlaylistFragmentListener
import app.music.ui.screen.onlinemusic.OnlineHomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewmodel.OnlineHomeActivityViewModel
import java.lang.ref.WeakReference

class OnlinePlaylistFragment
    : BaseFragment<FragmentPlaylistBinding>(),
        SwipeRefreshLayout.OnRefreshListener,
        PlaylistFragmentListener,
        OnlinePlaylistItemClickListener {

    private lateinit var mPlaylistRecyclerAdapter: OnlinePlaylistAdapter
    private var mHomeActivityViewModel: OnlineHomeActivityViewModel? = null
    private var mLastSearchingPlaylist: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as OnlineHomeActivity).mPlaylistFragmentListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeActivityViewModel = ViewModelProviders.of(activity!!).get(OnlineHomeActivityViewModel::class.java)
        mHomeActivityViewModel!!.mIsSearching.observe(
                this,
                Observer { searching ->
                    if (mIsVisibleToUser) {
                        if (searching != null && searching) {
                            mPlaylistRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                        } else {
                            mPlaylistRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlinePlaylistList)
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

    override fun getLayoutId() = R.layout.fragment_playlist

    override fun getLogTag() = TAG

    override fun initView() {
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true)
        binding.refreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mPlaylistRecyclerAdapter = OnlinePlaylistAdapter(
                WeakReference<Activity>(activity),
                this::onPlaylistClick,
                this::onPlaylistLongClick
        )
        binding.recyclerview.adapter = mPlaylistRecyclerAdapter
        mPlaylistRecyclerAdapter!!.updateItems(false, LoadMusicUtil.sOnlinePlaylistList)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewOnlinePlaylistList(
                activity as Activity, binding.refreshlayout, mPlaylistRecyclerAdapter::updateItems)
    }

    override fun onSortPlaylist(sortBy: String, isAscending: String) {
        SortMethodUtils.sortOnlinePlaylistList(activity as Activity, sortBy, isAscending,
                mPlaylistRecyclerAdapter::updateItems)
    }

    override fun onScrollToTop() {
        binding.recyclerview.scrollToPosition(0)
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel!!.getSearching()) {
            if (TextUtils.isEmpty(mHomeActivityViewModel!!.getSearchingText())) {
                mPlaylistRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlinePlaylistList)
            } else {
                if (mLastSearchingPlaylist == null
                        || mLastSearchingPlaylist != mHomeActivityViewModel!!.getSearchingText()) {
                    mPlaylistRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                }
            }
        } else {
            if (mLastSearchingPlaylist != null) {
                mPlaylistRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlinePlaylistList)
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingPlaylist = mHomeActivityViewModel!!.getSearchingText()
    }

    companion object {

        private val TAG = "OnlinePlaylistFragment"
    }
}
