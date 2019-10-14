package app.music.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemDialogOnlinePlaylistBinding
import app.music.diffcallback.DialogOnlinePlaylistDiffCallBack
import app.music.model.OnlinePlaylist
import app.music.viewholder.DialogOnlinePlaylistViewHolder
import java.lang.ref.WeakReference

class DialogOnlinePlaylistAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (OnlinePlaylist) -> Unit,
        private var itemLongClickListeners: (OnlinePlaylist) -> Unit)
    : BaseRecyclerAdapter<OnlinePlaylist, DialogOnlinePlaylistViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_dialog_online_playlist

    override fun getViewHolder(binding: ViewDataBinding): DialogOnlinePlaylistViewHolder {
        return DialogOnlinePlaylistViewHolder(
                mActivityReference,
                binding as ItemDialogOnlinePlaylistBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : DialogAddToPlaylistListener {
//            override fun onDialogPlaylistClick(isOnlinePlaylist: Boolean,
//                                               playlistCreatedTime: Calendar) {
//                (activity as DialogAddToPlaylistListener)
//                        .onDialogPlaylistClick(isOnlinePlaylist, playlistCreatedTime)
//            }
//        }
//    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<OnlinePlaylist>, newItems: List<OnlinePlaylist>)
            : DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                DialogOnlinePlaylistDiffCallBack(dataList, newItems),
                false)
    }

    override fun isContainingFilterPatternItem(item: OnlinePlaylist, filterPattern: String): Boolean {
        return item.playlistName.toLowerCase().contains(filterPattern)
    }
}
