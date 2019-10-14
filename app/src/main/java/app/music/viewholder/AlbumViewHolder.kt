package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemHomeFirstFragmentBinding
import app.music.model.Album
import app.music.utils.imageloading.ImageLoadingUtils
import com.bumptech.glide.request.RequestOptions
import java.lang.ref.WeakReference

class AlbumViewHolder(
        weakReference: WeakReference<Activity>,
        itemView: ItemHomeFirstFragmentBinding,
        itemClickListeners: (Album) -> Unit,
        itemLongClickListeners: (Album) -> Unit)
    : BaseViewHolder<Album, ItemHomeFirstFragmentBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var artist = ObservableField<String>()
    var album = ObservableField<String>()
    private val mRequestOptions = RequestOptions().error(R.drawable.ic_album)

    override fun bindData(dataObject: Album) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, album, it.albumName, defaultValue)
            val musicList = it.musicList
            if (musicList == null || musicList.size == 0) return
            val music = musicList[0] ?: return
            setStringObservableFieldValue(context, artist, music.artist, defaultValue)
            ImageLoadingUtils.loadMusicImage(music, mMetadataRetriever, mRequestOptions, mBinding.coverArt)
        }
    }
}
