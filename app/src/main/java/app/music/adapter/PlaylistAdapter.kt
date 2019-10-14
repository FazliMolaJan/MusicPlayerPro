package app.music.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemHomeFifthFragmentBinding
import app.music.diffcallback.FilterDiffCallBack
import app.music.diffcallback.PlaylistDiffCallBack
import app.music.model.Playlist
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.viewholder.PlaylistViewHolder
import java.lang.ref.WeakReference

class PlaylistAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (Playlist) -> Unit,
        private var itemLongClickListeners: (Playlist) -> Unit)
    : BaseRecyclerAdapter<Playlist, PlaylistViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_home_fifth_fragment

    override fun getViewHolder(binding: ViewDataBinding): PlaylistViewHolder {
        return PlaylistViewHolder(
                mActivityReference,
                binding as ItemHomeFifthFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : PlaylistFragmentItemClickListener {
//            override fun onPlaylistClick(playlist: Playlist, isLongClick: Boolean) {
//                (activity as PlaylistFragmentItemClickListener).onPlaylistClick(playlist, isLongClick)
//            }
//        }
//    }

    override fun getDiffResult(isFilter: Boolean, dataList: List<Playlist>, newItems: List<Playlist>)
            : DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                if (isFilter) FilterDiffCallBack(dataList, newItems)
                else PlaylistDiffCallBack(dataList, newItems),
                false)
    }

    override fun isContainingFilterPatternItem(item: Playlist, filterPattern: String): Boolean {
        return item.playlistName.toLowerCase().contains(filterPattern)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        RecyclerViewUtils.setToolbarScrollFlag(recyclerView, mActivityReference)
    }
}
