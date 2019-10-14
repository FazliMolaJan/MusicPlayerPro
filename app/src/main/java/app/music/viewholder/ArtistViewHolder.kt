package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemHomeThirdFragmentBinding
import app.music.model.Artist
import java.lang.ref.WeakReference

class ArtistViewHolder(
        weakReference: WeakReference<Activity>, itemView: ItemHomeThirdFragmentBinding,
        itemClickListeners: (Artist) -> Unit,
        itemLongClickListeners: (Artist) -> Unit)
    : BaseViewHolder<Artist, ItemHomeThirdFragmentBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var artist = ObservableField<String>()
    var trackAndAlbumInfo = ObservableField<String>()

    override fun bindData(dataObject: Artist) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, artist, it.artistName, defaultValue)
            val musicList = it.musicList
            val trackCount = musicList?.size ?: 0
            val albumNameList = it.albumNameList
            val albumCount = albumNameList?.size ?: 0
            trackAndAlbumInfo.set(
                    trackCount.toString() + (if (trackCount > 1) context?.getString(R.string.tracks) else context?.getString(R.string.track))
                            + albumCount + (if (albumCount > 1) context?.getString(R.string.albums) else context?.getString(R.string.album))
            )
        }
    }
}
