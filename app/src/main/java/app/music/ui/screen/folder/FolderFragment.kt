package app.music.ui.screen.folder

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.FolderAdapter
import app.music.base.BaseFragment
import app.music.databinding.FragmentFolderBinding
import app.music.listener.itemclick.FolderFragmentItemClickListener
import app.music.listener.homefragmentlistener.FolderFragmentListener
import app.music.ui.screen.home.HomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewmodel.HomeActivityViewModel
import java.lang.ref.WeakReference

class FolderFragment
    : BaseFragment<FragmentFolderBinding>(),
        SwipeRefreshLayout.OnRefreshListener,
        FolderFragmentListener,
        FolderFragmentItemClickListener {

    private lateinit var mFolderRecyclerAdapter: FolderAdapter
    private var mHomeActivityViewModel: HomeActivityViewModel? = null
    private var mLastSearchingFolder: String? = ""
    override val layoutId = R.layout.fragment_folder
    override val logTag = TAG

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).setFolderFragmentListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeActivityViewModel = ViewModelProviders.of(activity!!).get(HomeActivityViewModel::class.java)
        mHomeActivityViewModel!!.mIsSearching.observe(
                this,
                Observer { searching ->
                    if (mIsVisibleToUser) {
                        if (searching != null && searching) {
                            mFolderRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                        } else {
                            mFolderRecyclerAdapter.updateItems(false, LoadMusicUtil.sFolderList)
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
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true, true)
        binding.refreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mFolderRecyclerAdapter = FolderAdapter(
                WeakReference<Activity>(activity),
                this::onFolderClick,
                this::onFolderLongClick)
        binding.recyclerview.adapter = mFolderRecyclerAdapter
        mFolderRecyclerAdapter.updateItems(false, LoadMusicUtil.sFolderList)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewFolderList(
                activity as Activity,
                binding.refreshlayout,
                mFolderRecyclerAdapter::updateItems
        )
    }

    override fun onSortFolder(sortBy: String, isAscending: String) {
        SortMethodUtils.sortFolderList(
                activity as Activity,
                sortBy,
                isAscending,
                mFolderRecyclerAdapter::updateItems
        )
    }

    override fun onScrollToTop() {
        binding.recyclerview.scrollToPosition(0)
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel!!.getSearching()) {
            if (TextUtils.isEmpty(mHomeActivityViewModel!!.getSearchingText())) {
                mFolderRecyclerAdapter.updateItems(false, LoadMusicUtil.sFolderList)
            } else {
                if (mLastSearchingFolder == null
                        || mLastSearchingFolder != mHomeActivityViewModel!!.getSearchingText()) {
                    mFolderRecyclerAdapter.filter.filter(mHomeActivityViewModel!!.getSearchingText())
                }
            }
        } else {
            if (mLastSearchingFolder != null) {
                mFolderRecyclerAdapter.updateItems(false, LoadMusicUtil.sFolderList)
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingFolder = mHomeActivityViewModel!!.getSearchingText()
    }

    companion object {

        private val TAG = "GenreFragment"
    }
}
