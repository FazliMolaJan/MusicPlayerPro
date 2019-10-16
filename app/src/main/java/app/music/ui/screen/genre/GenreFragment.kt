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
import app.music.ui.screen.home.HomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewmodel.HomeActivityViewModel
import java.lang.ref.WeakReference

class GenreFragment
    : BaseFragment<FragmentGenreBinding>(),
        SwipeRefreshLayout.OnRefreshListener,
        GenreFragmentListener,
        GenreFragmentItemClickListener {

    private lateinit var mGenreRecyclerAdapter: GenreAdapter
    private var mHomeActivityViewModel: HomeActivityViewModel? = null
    private var mLastSearchingGenre: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).mGenreFragmentListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeActivityViewModel = ViewModelProviders.of(activity!!).get(HomeActivityViewModel::class.java)
        mHomeActivityViewModel!!.mIsSearching.observe(
                this,
                Observer { searching ->
                    if (mIsVisibleToUser) {
                        if (searching != null && searching) {
                            mGenreRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                        } else {
                            mGenreRecyclerAdapter.updateItems(false, LoadMusicUtil.sGenreList)
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

    override fun getLayoutId() = R.layout.fragment_genre

    override fun getLogTag() = TAG

    override fun initView() {
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true, true)
        binding.refreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mGenreRecyclerAdapter = GenreAdapter(
                WeakReference<Activity>(activity),
                this::onGenreClick,
                this::onGenreLongClick
        )
        binding.recyclerview.adapter = mGenreRecyclerAdapter
        mGenreRecyclerAdapter.updateItems(false, LoadMusicUtil.sGenreList)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewGenreList(
                activity as Activity,
                binding.refreshlayout,
                mGenreRecyclerAdapter::updateItems
        )
    }

    override fun onSortGenre(sortBy: String, isAscending: String) {
        SortMethodUtils.sortGenreList(
                activity as Activity,
                sortBy,
                isAscending,
                mGenreRecyclerAdapter::updateItems
        )
    }

    override fun onScrollToTop() {
        binding.recyclerview.scrollToPosition(0)
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel!!.getSearching()) {
            if (TextUtils.isEmpty(mHomeActivityViewModel!!.getSearchingText())) {
                mGenreRecyclerAdapter.updateItems(false, LoadMusicUtil.sGenreList)
            } else {
                if (mLastSearchingGenre == null
                        || mLastSearchingGenre != mHomeActivityViewModel!!.getSearchingText()) {
                    mGenreRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                }
            }
        } else {
            if (mLastSearchingGenre != null) {
                mGenreRecyclerAdapter.updateItems(false, LoadMusicUtil.sGenreList)
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingGenre = mHomeActivityViewModel!!.getSearchingText()
    }

    companion object {

        private val TAG = "GenreFragment"
    }
}
