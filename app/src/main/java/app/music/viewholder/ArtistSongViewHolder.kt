package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemArtistSecondFragmentBinding
import app.music.model.entity.BaseMusik
import app.music.utils.imageloading.ImageLoadingUtils
import com.bumptech.glide.request.RequestOptions
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

class ArtistSongViewHolder(
        weakReference: WeakReference<Activity>, itemView: ItemArtistSecondFragmentBinding,
        itemClickListeners: (BaseMusik) -> Unit,
        itemLongClickListeners: (BaseMusik) -> Unit)
    : BaseViewHolder<BaseMusik, ItemArtistSecondFragmentBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var song = ObservableField<String>()
    var duration = ObservableField<String>()
    private val mSimpleDateFormat = SimpleDateFormat("mm:ss")
    private val mRequestOptions = RequestOptions()
            .centerCrop()
//            .placeholder(R.drawable.default_avatar)
            .error(R.drawable.ic_album)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .priority(Priority.HIGH)
//            .dontAnimate()
//            .dontTransform();

    override fun bindData(dataObject: BaseMusik) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, song, it.title, defaultValue)
            setStringObservableFieldValue(context, duration, mSimpleDateFormat.format(it.duration), defaultValue)
            ImageLoadingUtils.loadMusicImage(it, mMetadataRetriever, mRequestOptions, mBinding.coverArt)
        }
    }
}
