package app.music.ui.screen.album

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.recycler.AlbumAdapter
import app.music.databinding.FragmentAlbumBinding
import app.music.listener.homefragmentlistener.AlbumFragmentListener
import app.music.listener.itemclick.AlbumFragmentItemClickListener
import app.music.model.entity.Album
import app.music.ui.screen.home.BaseHomeFragment
import app.music.ui.screen.onlinemusic.OnlineHomeActivity
import app.music.utils.ConstantUtil
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.sort.SortMethodUtils
import app.music.viewholder.AlbumViewHolder
import app.music.viewmodel.OnlineHomeActivityViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class OnlineAlbumFragment
    : BaseHomeFragment<Album,
        OnlineHomeActivityViewModel,
        FragmentAlbumBinding,
        AlbumViewHolder,
        AlbumAdapter>(),
        AlbumFragmentListener,
        AlbumFragmentItemClickListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as OnlineHomeActivity).mAlbumFragmentListener = this
    }

    override fun getRecyclerAdapter(): AlbumAdapter {
        return AlbumAdapter(
                WeakReference<Activity>(activity),
                this::onAlbumClick,
                this::onAlbumLongClick
        )
    }

    override fun getDataList(): List<Album> = LoadMusicUtil.sOnlineAlbumList

    override fun getRecyclerView(): RecyclerView = binding.recyclerview

    override fun getRefreshLayout(): SwipeRefreshLayout = binding.refreshlayout

    override fun getLayoutId() = R.layout.fragment_album

    override fun getLogTag() = TAG

    override fun initInject() {
        fragmentComponent?.inject(this)
    }

    override fun initView() {
        super.initView()
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = GridLayoutManager(context, ConstantUtil.SPAN_COUNT_THREE)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewOnlineAlbumList(
                activity as Activity,
                binding.refreshlayout,
                mRecyclerAdapter::updateItems
        )
    }

    override fun onSortAlbum(sortBy: String, orderBy: String) {
        SortMethodUtils.sortOnlineAlbumList(
                activity as Activity,
                sortBy,
                orderBy,
                mRecyclerAdapter::updateItems
        )
    }

    companion object {

        private const val TAG = "OnlineAlbumFragment"
    }
}
