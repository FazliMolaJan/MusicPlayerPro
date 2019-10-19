package app.music.ui.screen.playlist

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.recycler.OnlinePlaylistAdapter
import app.music.databinding.FragmentPlaylistBinding
import app.music.listener.homefragmentlistener.PlaylistFragmentListener
import app.music.listener.itemclick.OnlinePlaylistItemClickListener
import app.music.model.entity.OnlinePlaylist
import app.music.ui.screen.home.BaseHomeFragment
import app.music.ui.screen.onlinemusic.OnlineHomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewholder.OnlinePlaylistViewHolder
import app.music.viewmodel.OnlineHomeActivityViewModel
import java.lang.ref.WeakReference

class OnlinePlaylistFragment
    : BaseHomeFragment<OnlinePlaylist,
        OnlineHomeActivityViewModel,
        FragmentPlaylistBinding,
        OnlinePlaylistViewHolder,
        OnlinePlaylistAdapter>(),
        PlaylistFragmentListener,
        OnlinePlaylistItemClickListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as OnlineHomeActivity).mPlaylistFragmentListener = this
    }

    override fun getRecyclerAdapter(): OnlinePlaylistAdapter {
        return OnlinePlaylistAdapter(
                WeakReference<Activity>(activity),
                this::onPlaylistClick,
                this::onPlaylistLongClick
        )
    }

    override fun getDataList(): List<OnlinePlaylist> = LoadMusicUtil.sOnlinePlaylistList

    override fun getRecyclerView(): RecyclerView = binding.recyclerview

    override fun getRefreshLayout(): SwipeRefreshLayout = binding.refreshlayout

    override fun initInject() {
        fragmentComponent?.inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_playlist

    override fun getLogTag() = TAG

    override fun initView() {
        super.initView()
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewOnlinePlaylistList(
                activity as Activity,
                binding.refreshlayout,
                mRecyclerAdapter::updateItems
        )
    }

    override fun onSortPlaylist(sortBy: String, isAscending: String) {
        SortMethodUtils.sortOnlinePlaylistList(
                activity as Activity,
                sortBy,
                isAscending,
                mRecyclerAdapter::updateItems
        )
    }

    companion object {

        private const val TAG = "OnlinePlaylistFragment"
    }
}
