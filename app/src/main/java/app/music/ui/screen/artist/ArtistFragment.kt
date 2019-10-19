package app.music.ui.screen.artist

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.recycler.ArtistAdapter
import app.music.databinding.FragmentArtistBinding
import app.music.listener.homefragmentlistener.ArtistFragmentListener
import app.music.listener.itemclick.ArtistFragmentItemClickListener
import app.music.model.entity.Artist
import app.music.ui.screen.home.BaseHomeFragment
import app.music.ui.screen.home.HomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewholder.ArtistViewHolder
import app.music.viewmodel.HomeActivityViewModel
import java.lang.ref.WeakReference

class ArtistFragment
    : BaseHomeFragment<Artist,
        HomeActivityViewModel,
        FragmentArtistBinding,
        ArtistViewHolder,
        ArtistAdapter>(),
        ArtistFragmentListener,
        ArtistFragmentItemClickListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).mArtistFragmentListener = this
    }

    override fun getRecyclerAdapter(): ArtistAdapter {
        return ArtistAdapter(
                WeakReference<Activity>(activity),
                this::onArtistClick,
                this::onArtistLongClick
        )
    }

    override fun getDataList(): List<Artist> = LoadMusicUtil.sArtistList

    override fun getRecyclerView(): RecyclerView = binding.recyclerview

    override fun getRefreshLayout(): SwipeRefreshLayout = binding.refreshlayout

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_artist

    override fun getLogTag() = TAG

    override fun initView() {
        super.initView()
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true, true)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewArtistList(
                activity as Activity,
                binding.refreshlayout,
                mRecyclerAdapter::updateItems
        )
    }

    override fun onSortArtist(sortBy: String, isAscending: String) {
        SortMethodUtils.sortArtistList(
                activity as Activity,
                sortBy,
                isAscending,
                mRecyclerAdapter::updateItems
        )
    }

    companion object {

        private const val TAG = "ArtistFragment"
    }
}
