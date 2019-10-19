package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemHomeSixthFragmentBinding
import app.music.model.entity.Folder
import java.lang.ref.WeakReference

class FolderViewHolder(
        weakReference: WeakReference<Activity>,
        itemView: ItemHomeSixthFragmentBinding,
        itemClickListeners: (Folder) -> Unit,
        itemLongClickListeners: (Folder) -> Unit)
    : BaseViewHolder<Folder, ItemHomeSixthFragmentBinding>(
        weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var folder = ObservableField<String>()

    override fun bindData(dataObject: Folder) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, folder, it.folderName, defaultValue)
        }
    }
}
