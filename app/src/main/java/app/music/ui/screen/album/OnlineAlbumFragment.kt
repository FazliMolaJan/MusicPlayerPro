package app.music.ui.screen.album

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.AlbumAdapter
import app.music.base.BaseFragment
import app.music.databinding.FragmentAlbumBinding
import app.music.listener.homefragmentlistener.AlbumFragmentListener
import app.music.listener.itemclick.AlbumFragmentItemClickListener
import app.music.ui.screen.onlinemusic.OnlineHomeActivity
import app.music.utils.ConstantUtil
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.sort.SortMethodUtils
import app.music.viewmodel.OnlineHomeActivityViewModel
import timber.log.Timber
import java.lang.ref.WeakReference

class OnlineAlbumFragment
    : BaseFragment<FragmentAlbumBinding>(),
        SwipeRefreshLayout.OnRefreshListener,
        AlbumFragmentListener,
        AlbumFragmentItemClickListener {

    private lateinit var mAlbumRecyclerAdapter: AlbumAdapter
    private var mHomeActivityViewModel: OnlineHomeActivityViewModel? = null
    private var mLastSearchingAlbum: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as OnlineHomeActivity).mAlbumFragmentListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeActivityViewModel = ViewModelProviders.of(activity!!).get(OnlineHomeActivityViewModel::class.java)

        mHomeActivityViewModel!!.mIsSearching.observe(
                this,
                Observer { searching ->
                    Timber.i("onCreate: searching changed")
                    if (mIsVisibleToUser) {
                        Timber.i("onCreate: searching changed and visible to user")
                        if (searching != null && searching!!) {
                            Timber.i("onCreate: searching changed and visible to user and searching")
                            mAlbumRecyclerAdapter!!.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                            Timber.i("onCreate: searching changed and visible to user and searching" + mHomeActivityViewModel!!.getSearchingText()!!)
                        } else {
                            Timber.i("onCreate: searching changed and visible to user and not searching")
                            mAlbumRecyclerAdapter?.updateItems(false, LoadMusicUtil.sOnlineAlbumList)
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

    override fun getLayoutId() = R.layout.fragment_album

    override fun getLogTag() = TAG

    override fun initView() {
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = GridLayoutManager(context, ConstantUtil.SPAN_COUNT_THREE)
        binding.refreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mAlbumRecyclerAdapter = AlbumAdapter(
                WeakReference<Activity>(activity),
                this::onAlbumClick,
                this::onAlbumLongClick
        )
        binding.recyclerview.adapter = mAlbumRecyclerAdapter
        mAlbumRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlineAlbumList)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewOnlineAlbumList(
                activity as Activity,
                binding.refreshlayout,
                mAlbumRecyclerAdapter::updateItems
        )
    }

    override fun onSortAlbum(sortBy: String, orderBy: String) {
        SortMethodUtils.sortOnlineAlbumList(
                activity as Activity,
                sortBy,
                orderBy,
                mAlbumRecyclerAdapter::updateItems
        )
    }

    override fun onScrollToTop() {
        binding.recyclerview.scrollToPosition(0)
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel!!.getSearching()) {
            Timber.i("onVisible: mLastSearchingAlbum!=null and searching")
            if (TextUtils.isEmpty(mHomeActivityViewModel!!.getSearchingText())) {
                Timber.i("onVisible: mLastSearchingAlbum!=null and searching and searching text not empty")
                mAlbumRecyclerAdapter?.updateItems(false, LoadMusicUtil.sOnlineAlbumList)
            } else {
                Timber.i("onVisible: mLastSearchingAlbum!=null and searching and searching text not empty")
                if (mLastSearchingAlbum == null || mLastSearchingAlbum != mHomeActivityViewModel!!.getSearchingText()) {
                    Timber.i("onVisible: mLastSearchingAlbum!=null and searching and searching text different")
                    mAlbumRecyclerAdapter!!.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                }
            }
        } else {
            Timber.i("onVisible: not searching")
            if (mLastSearchingAlbum != null) {
                Timber.i("onVisible: not searching and mLastSearchingAlbum != null")
                mAlbumRecyclerAdapter?.updateItems(false, LoadMusicUtil.sOnlineAlbumList)
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingAlbum = mHomeActivityViewModel!!.getSearchingText()
        Timber.i("onInVisible: on invisible and get searching text" + mLastSearchingAlbum!!)
    }

    companion object {

        private val TAG = "AlbumFragment"
    }
}
