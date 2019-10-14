package app.music.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemOnlineHomeFifthFragmentBinding
import app.music.diffcallback.FilterDiffCallBack
import app.music.diffcallback.OnlinePlaylistDiffCallBack
import app.music.model.OnlinePlaylist
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.viewholder.OnlinePlaylistViewHolder
import java.lang.ref.WeakReference

class OnlinePlaylistAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (OnlinePlaylist) -> Unit,
        private var itemLongClickListeners: (OnlinePlaylist) -> Unit)
    : BaseRecyclerAdapter<OnlinePlaylist, OnlinePlaylistViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_online_home_fifth_fragment

    override fun getViewHolder(binding: ViewDataBinding): OnlinePlaylistViewHolder {
        return OnlinePlaylistViewHolder(
                mActivityReference,
                binding as ItemOnlineHomeFifthFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : OnlinePlaylistItemClickListener {
//            override fun onOnlinePlaylistClick(playlist: OnlinePlaylist, isLongClick: Boolean) {
//                (activity as OnlinePlaylistItemClickListener).onOnlinePlaylistClick(playlist, isLongClick)
//            }
//        }
//    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<OnlinePlaylist>, newItems: List<OnlinePlaylist>)
            : DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                if (isFilter) FilterDiffCallBack(dataList, newItems)
                else OnlinePlaylistDiffCallBack(dataList, newItems),
                false)
    }

    override fun isContainingFilterPatternItem(item: OnlinePlaylist, filterPattern: String): Boolean {
        return item.playlistName.toLowerCase().contains(filterPattern)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        RecyclerViewUtils.setToolbarScrollFlag(recyclerView, mActivityReference)
    }
}
