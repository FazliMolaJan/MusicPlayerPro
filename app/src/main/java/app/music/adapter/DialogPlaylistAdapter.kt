package app.music.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemDialogPlaylistBinding
import app.music.diffcallback.DialogPlaylistDiffCallBack
import app.music.model.Playlist
import app.music.viewholder.DialogPlaylistViewHolder
import java.lang.ref.WeakReference

class DialogPlaylistAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (Playlist) -> Unit,
        private var itemLongClickListeners: (Playlist) -> Unit)
    : BaseRecyclerAdapter<Playlist, DialogPlaylistViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_dialog_playlist

    override fun getViewHolder(binding: ViewDataBinding): DialogPlaylistViewHolder {
        return DialogPlaylistViewHolder(
                mActivityReference,
                binding as ItemDialogPlaylistBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : DialogAddToPlaylistListener {
//            override fun onDialogPlaylistClick(
//                    isOnlinePlaylist: Boolean, playlistCreatedTime: Calendar) {
//                (activity as DialogAddToPlaylistListener)
//                        .onDialogPlaylistClick(isOnlinePlaylist, playlistCreatedTime)
//            }
//        }
//    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<Playlist>, newItems: List<Playlist>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                DialogPlaylistDiffCallBack(dataList, newItems),
                false)
    }

    override fun isContainingFilterPatternItem(item: Playlist, filterPattern: String): Boolean {
        return item.playlistName.toLowerCase().contains(filterPattern)
    }
}
