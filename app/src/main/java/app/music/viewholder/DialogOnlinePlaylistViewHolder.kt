package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemDialogOnlinePlaylistBinding
import app.music.model.entity.OnlinePlaylist
import java.lang.ref.WeakReference

class DialogOnlinePlaylistViewHolder(
        weakReference: WeakReference<Activity>, itemView: ItemDialogOnlinePlaylistBinding,
        itemClickListeners: (OnlinePlaylist) -> Unit,
        itemLongClickListeners: (OnlinePlaylist) -> Unit)
//    : BaseViewHolder<OnlinePlaylist, ItemDialogOnlinePlaylistBinding, DialogAddToPlaylistListener>
    : BaseViewHolder<OnlinePlaylist, ItemDialogOnlinePlaylistBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var playlistName = ObservableField<String>()

    override fun bindData(dataObject: OnlinePlaylist) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, playlistName, it.playlistName, defaultValue)
        }
    }
}
