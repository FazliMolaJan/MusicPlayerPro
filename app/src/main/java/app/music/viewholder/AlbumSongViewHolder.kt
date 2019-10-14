package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemMusicListBinding
import app.music.model.BaseMusik
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

class AlbumSongViewHolder(
        weakReference: WeakReference<Activity>,
        itemView: ItemMusicListBinding,
        itemClickListeners: (BaseMusik) -> Unit,
        itemLongClickListeners: (BaseMusik) -> Unit)
    : BaseViewHolder<BaseMusik, ItemMusicListBinding>(
        weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var song = ObservableField<String>()
    var artist = ObservableField<String>()
    var duration = ObservableField<String>()
    private val mSimpleDateFormat = SimpleDateFormat("mm:ss")

    override fun bindData(dataObject: BaseMusik) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, song, it.title, defaultValue)
            setStringObservableFieldValue(context, artist, it.artist, defaultValue)
            setStringObservableFieldValue(
                    context, duration, mSimpleDateFormat.format(it.duration), defaultValue)
        }
    }
}
