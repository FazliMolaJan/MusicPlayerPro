package app.music.ui.screen.artist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.ArtistAdapter
import app.music.base.BaseFragment
import app.music.databinding.FragmentArtistBinding
import app.music.listener.itemclick.ArtistFragmentItemClickListener
import app.music.listener.homefragmentlistener.ArtistFragmentListener
import app.music.ui.screen.onlinemusic.OnlineHomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewmodel.OnlineHomeActivityViewModel
import java.lang.ref.WeakReference

class OnlineArtistFragment
    : BaseFragment<FragmentArtistBinding>(),
        SwipeRefreshLayout.OnRefreshListener,
        ArtistFragmentListener,
        ArtistFragmentItemClickListener {

    private lateinit var mArtistRecyclerAdapter: ArtistAdapter
    private var mHomeActivityViewModel: OnlineHomeActivityViewModel? = null
    private var mLastSearchingArtist: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as OnlineHomeActivity).mArtistFragmentListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeActivityViewModel = ViewModelProviders.of(activity!!).get(OnlineHomeActivityViewModel::class.java)
        mHomeActivityViewModel!!.mIsSearching.observe(
                this,
                Observer { searching ->
                    if (mIsVisibleToUser) {
                        if (searching != null && searching) {
                            mArtistRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                        } else {
                            mArtistRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlineArtistList)
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

    override fun getLayoutId() = R.layout.fragment_artist

    override fun getLogTag() = TAG

    override fun initView() {
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true, true)
        binding.refreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mArtistRecyclerAdapter = ArtistAdapter(
                WeakReference<Activity>(activity),
                this::onArtistClick,
                this::onArtistLongClick)
        binding.recyclerview.adapter = mArtistRecyclerAdapter
        mArtistRecyclerAdapter!!.updateItems(false, LoadMusicUtil.sOnlineArtistList)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewOnlineArtistList(
                activity as Activity, binding.refreshlayout, mArtistRecyclerAdapter::updateItems)
    }

    override fun onSortArtist(sortBy: String, isAscending: String) {
        SortMethodUtils.sortOnlineArtistList(activity as Activity, sortBy, isAscending,
                mArtistRecyclerAdapter::updateItems)
    }

    override fun onScrollToTop() {
        binding.recyclerview.scrollToPosition(0)
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel!!.getSearching()) {
            if (TextUtils.isEmpty(mHomeActivityViewModel!!.getSearchingText())) {
                mArtistRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlineArtistList)
            } else {
                if (mLastSearchingArtist == null
                        || mLastSearchingArtist != mHomeActivityViewModel!!.getSearchingText()) {
                    mArtistRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                }
            }
        } else {
            if (mLastSearchingArtist != null) {
                mArtistRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlineArtistList)
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingArtist = mHomeActivityViewModel!!.getSearchingText()
    }

    companion object {

        private val TAG = "ArtistFragment"
    }
}
