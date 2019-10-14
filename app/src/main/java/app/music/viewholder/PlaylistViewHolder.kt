package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemHomeFifthFragmentBinding
import app.music.model.Playlist
import app.music.utils.imageloading.ImageLoadingUtils
import com.bumptech.glide.request.RequestOptions
import java.lang.ref.WeakReference

class PlaylistViewHolder(
        weakReference: WeakReference<Activity>, itemView: ItemHomeFifthFragmentBinding,
        itemClickListeners: (Playlist) -> Unit,
        itemLongClickListeners: (Playlist) -> Unit)
    : BaseViewHolder<Playlist, ItemHomeFifthFragmentBinding>
//    : BaseViewHolder<Playlist, ItemHomeFifthFragmentBinding, PlaylistFragmentItemClickListener>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var playlistName = ObservableField<String>()
    var songCount = ObservableField<String>()
    private val mRequestOptions = RequestOptions().error(R.drawable.ic_album)

    override fun bindData(dataObject: Playlist) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            val musicList = it.musicList ?: return
            val musicListSize = musicList.size
            songCount.set(
                    musicListSize.toString() +
                            if (musicListSize > 1) context?.getString(R.string.tracks_without_separator)
                            else context?.getString(R.string.track_without_separator)
            )
            setStringObservableFieldValue(context, playlistName, it.playlistName, defaultValue)
            ImageLoadingUtils.loadMusicImage(
                    musicList[0], mMetadataRetriever, mRequestOptions, mBinding.coverArt)
        }
    }
}
