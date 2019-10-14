package app.music.viewholder

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemHomeSecondFragmentBinding
import app.music.listener.dialoglistener.DialogSongOptionListener
import app.music.model.BaseMusik
import app.music.utils.dialog.songoption.DialogSongOptionMethodUtils
import app.music.utils.imageloading.ImageLoadingUtils
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_home_second_fragment.view.*
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

class MusicViewHolder(
        weakReference: WeakReference<Activity>,
        itemView: ItemHomeSecondFragmentBinding,
        itemClickListeners: (BaseMusik) -> Unit,
        itemLongClickListeners: (BaseMusik) -> Unit)
    : BaseViewHolder<BaseMusik, ItemHomeSecondFragmentBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var song = ObservableField<String>()
    var artist = ObservableField<String>()
    var duration = ObservableField<String>()
    private val mSimpleDateFormat = SimpleDateFormat("mm:ss")
    private val mRequestOptions = RequestOptions()
            .error(R.drawable.ic_disc_56dp)
            .override(100, 100)
            .centerCrop()

    override fun bindData(dataObject: BaseMusik) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        mBinding.root.btn_option.setOnClickListener {
            itemLongClickListeners(dataObject)
        }
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, song, it.title, defaultValue)
            setStringObservableFieldValue(context, artist, it.artist, defaultValue)
            setStringObservableFieldValue(context, duration, mSimpleDateFormat.format(it.duration), defaultValue)
            ImageLoadingUtils.loadMusicImage(it, mMetadataRetriever, mRequestOptions, mBinding.coverArt)
        }
    }
}
