package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import android.text.TextUtils
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemArtistFirstFragmentBinding
import app.music.model.entity.Album
import app.music.utils.imageloading.ImageLoadingUtils
import com.bumptech.glide.request.RequestOptions
import java.lang.ref.WeakReference

class ArtistAlbumViewHolder(
        weakReference: WeakReference<Activity>, itemView: ItemArtistFirstFragmentBinding,
        itemClickListeners: (Album) -> Unit,
        itemLongClickListeners: (Album) -> Unit)
    : BaseViewHolder<Album, ItemArtistFirstFragmentBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var album = ObservableField<String>()
    var releaseYear = ObservableField<String>()
    private val mRequestOptions = RequestOptions().error(R.drawable.ic_album)

    override fun bindData(dataObject: Album) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val musicList = it.musicList
            if (musicList == null || musicList.size == 0) return
            val music = musicList[0] ?: return
            val yearValue = music.year
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            if (TextUtils.isEmpty(yearValue) || yearValue == context?.getString(R.string.zero_number)) {
                releaseYear.set(context?.getString(R.string.Unknown))
            } else {
                releaseYear.set(yearValue)
            }
            setStringObservableFieldValue(context, album, music.album, defaultValue)
            ImageLoadingUtils.loadMusicImage(music, mMetadataRetriever, mRequestOptions, mBinding.coverArt)
        }
    }
}
