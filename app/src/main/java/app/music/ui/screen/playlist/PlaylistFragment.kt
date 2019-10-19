package app.music.ui.screen.playlist

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.recycler.PlaylistAdapter
import app.music.databinding.FragmentPlaylistBinding
import app.music.listener.homefragmentlistener.PlaylistFragmentListener
import app.music.listener.itemclick.PlaylistFragmentItemClickListener
import app.music.model.entity.Playlist
import app.music.ui.screen.home.BaseHomeFragment
import app.music.ui.screen.home.HomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewholder.PlaylistViewHolder
import app.music.viewmodel.HomeActivityViewModel
import java.lang.ref.WeakReference

class PlaylistFragment
    : BaseHomeFragment<Playlist,
        HomeActivityViewModel,
        FragmentPlaylistBinding,
        PlaylistViewHolder,
        PlaylistAdapter>(),
        PlaylistFragmentListener,
        PlaylistFragmentItemClickListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).mPlaylistFragmentListener = this
    }

    override fun getRecyclerAdapter(): PlaylistAdapter {
        return PlaylistAdapter(
                WeakReference<Activity>(activity),
                this::onPlaylistClick,
                this::onPlaylistLongClick
        )
    }

    override fun getDataList(): List<Playlist> = LoadMusicUtil.sPlaylistList

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
        HomeFragmentDataUpdatingUtil.getNewPlaylistList(
                activity as Activity,
                binding.refreshlayout,
                mRecyclerAdapter::updateItems
        )
    }

    override fun onSortPlaylist(sortBy: String, isAscending: String) {
        SortMethodUtils.sortPlaylistList(
                activity as Activity,
                sortBy,
                isAscending,
                mRecyclerAdapter::updateItems
        )
    }

    companion object {

        private const val TAG = "PlaylistFragment"
    }
}
