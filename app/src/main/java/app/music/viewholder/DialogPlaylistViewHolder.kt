package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemDialogPlaylistBinding
import app.music.model.entity.Playlist
import java.lang.ref.WeakReference

class DialogPlaylistViewHolder(
        weakReference: WeakReference<Activity>, itemView: ItemDialogPlaylistBinding,
        itemClickListeners: (Playlist) -> Unit,
        itemLongClickListeners: (Playlist) -> Unit)
//    : BaseViewHolder<Playlist, ItemDialogPlaylistBinding, DialogAddToPlaylistListener>
    : BaseViewHolder<Playlist, ItemDialogPlaylistBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var playlistName = ObservableField<String>()

    override fun bindData(dataObject: Playlist) {
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
