package app.music.ui.screen.home

import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.base.BaseFragment
import app.music.base.BaseRecyclerAdapter
import app.music.base.BaseViewHolder
import app.music.listener.RecyclerScrollToTopListener
import app.music.viewmodel.BaseHomeActivityViewModel
import timber.log.Timber
import javax.inject.Inject

abstract class BaseHomeFragment<
        I,
        VM : BaseHomeActivityViewModel,
        FB : ViewDataBinding,
        VH : BaseViewHolder<I, *>,
        RA : BaseRecyclerAdapter<I, VH>>
    : BaseFragment<FB>(),
        SwipeRefreshLayout.OnRefreshListener,
        RecyclerScrollToTopListener {

//    I: ItemType
//    VM: ViewModelType
//    FB: FragmentBindingType
//    VH: ViewHolderType
//    RA: RecyclerAdapterType

    @Inject
    protected lateinit var mHomeActivityViewModel: VM
    protected lateinit var mRecyclerAdapter: RA
    private var mLastSearchingName: String? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRefreshLayout: SwipeRefreshLayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mHomeActivityViewModel.mIsSearching.observe(
                this,
                Observer { searching ->
                    Timber.i("onCreate: searching changed")
                    if (mIsVisibleToUser) {
                        Timber.i("onCreate: searching changed and visible to user")
                        if (searching != null && searching) {
                            Timber.i("onCreate: searching changed and visible to user and searching")
                            mRecyclerAdapter.filter.filter(mHomeActivityViewModel.getSearchingText())
                            Timber.i("onCreate: searching changed and visible to user and searching" + mHomeActivityViewModel.getSearchingText()!!)
                        } else {
                            Timber.i("onCreate: searching changed and visible to user and not searching")
                            mRecyclerAdapter.updateItems(false, getDataList())
                        }
                    }
                }
        )
    }

    override fun onPause() {
        super.onPause()
        mRefreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mRecyclerView.adapter = null
    }

    override fun onVisible() {
        super.onVisible()
        if (mHomeActivityViewModel.getSearching()) {
            Timber.i("onVisible: mLastSearchingName!=null and searching")
            if (TextUtils.isEmpty(mHomeActivityViewModel.getSearchingText())) {
                Timber.i("onVisible: mLastSearchingName!=null and searching and searching text not empty")
                mRecyclerAdapter.updateItems(false, getDataList())
            } else {
                Timber.i("onVisible: mLastSearchingName!=null and searching and searching text not empty")
                if (mLastSearchingName == null || mLastSearchingName != mHomeActivityViewModel.getSearchingText()) {
                    Timber.i("onVisible: mLastSearchingName!=null and searching and searching text different")
                    mRecyclerAdapter.filter.filter(mHomeActivityViewModel.getSearchingText())
                }
            }
        } else {
            Timber.i("onVisible: not searching")
            if (mLastSearchingName != null) {
                Timber.i("onVisible: not searching and mLastSearchingName != null")
                mRecyclerAdapter.updateItems(false, getDataList())
            }
        }
    }

    override fun onInVisible() {
        super.onInVisible()
        mLastSearchingName = mHomeActivityViewModel.getSearchingText()
        Timber.i("onInVisible: on invisible and get searching text" + mLastSearchingName.toString())
    }

    override fun initView() {
        mRecyclerView = getRecyclerView()
        mRefreshLayout = getRefreshLayout()
        mRefreshLayout.setOnRefreshListener(this)
    }

    override fun initData() {
        mRecyclerAdapter = getRecyclerAdapter()
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerAdapter.updateItems(false, getDataList())
    }

    override fun onScrollToTop() = mRecyclerView.scrollToPosition(0)

    abstract fun getRecyclerAdapter(): RA

    abstract fun getDataList(): List<I>

    abstract fun getRecyclerView(): RecyclerView

    abstract fun getRefreshLayout(): SwipeRefreshLayout
}
