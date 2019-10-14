package app.music.ui.screen.genre

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.GenreAdapter
import app.music.base.BaseFragment
import app.music.databinding.FragmentGenreBinding
import app.music.listener.itemclick.GenreFragmentItemClickListener
import app.music.listener.homefragmentlistener.GenreFragmentListener
import app.music.ui.screen.onlinemusic.OnlineHomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewmodel.OnlineHomeActivityViewModel
import java.lang.ref.WeakReference

class OnlineGenreFragment
    : BaseFragment<FragmentGenreBinding>(),
        SwipeRefreshLayout.OnRefreshListener,
        GenreFragmentListener,
        GenreFragmentItemClickListener {

    private lateinit var mGenreRecyclerAdapter: GenreAdapter
    private var mHomeActivityViewModel: OnlineHomeActivityViewModel? = null
    private var mLastSearchingGenre: String? = ""
    override val layoutId = R.layout.fragment_genre
    override val logTag = TAG

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as OnlineHomeActivity).mGenreFragmentListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeActivityViewModel = ViewModelProviders.of(activity!!).get(OnlineHomeActivityViewModel::class.java)
        mHomeActivityViewModel!!.mIsSearching.observe(
                this,
                Observer { searching ->
                    if (mIsVisibleToUser) {
                        if (searching != null && searching) {
                            mGenreRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                        } else {
                            mGenreRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlineGenreList)
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
        RecyclerViewUtils.setVerticalLinearLayout(
                binding.recyclerview, context!!, true, true)
        binding.refreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mGenreRecyclerAdapter = GenreAdapter(
                WeakReference<Activity>(activity),
                this::onGenreClick,
                this::onGenreLongClick
        )
        binding.recyclerview.adapter = mGenreRecyclerAdapter
        mGenreRecyclerAdapter!!.updateItems(false, LoadMusicUtil.sOnlineGenreList)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewOnlineGenreList(
                activity as Activity, binding.refreshlayout, mGenreRecyclerAdapter::updateItems)
    }

    override fun onSortGenre(sortBy: String, isAscending: String) {
        SortMethodUtils.sortOnlineGenreList(activity as Activity, sortBy, isAscending,
                mGenreRecyclerAdapter::updateItems)
    }

    override fun onScrollToTop() {
        binding.recyclerview.scrollToPosition(0)
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel!!.getSearching()) {
            if (TextUtils.isEmpty(mHomeActivityViewModel!!.getSearchingText())) {
                mGenreRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlineGenreList)
            } else {
                if (mLastSearchingGenre == null
                        || mLastSearchingGenre != mHomeActivityViewModel!!.getSearchingText()) {
                    mGenreRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                }
            }
        } else {
            if (mLastSearchingGenre != null) {
                mGenreRecyclerAdapter.updateItems(false, LoadMusicUtil.sOnlineGenreList)
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingGenre = mHomeActivityViewModel!!.getSearchingText()
    }

    companion object {

        private val TAG = "OnlineGenreFragment"
    }
}
