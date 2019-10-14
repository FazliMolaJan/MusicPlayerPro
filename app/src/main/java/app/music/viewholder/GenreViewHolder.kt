package app.music.viewholder

import android.app.Activity
import android.content.Context
import androidx.databinding.ObservableField
import app.music.R
import app.music.base.BaseViewHolder
import app.music.databinding.ItemHomeFourthFragmentBinding
import app.music.model.Genre
import java.lang.ref.WeakReference

class GenreViewHolder(
        weakReference: WeakReference<Activity>, itemView: ItemHomeFourthFragmentBinding,
        itemClickListeners: (Genre) -> Unit,
        itemLongClickListeners: (Genre) -> Unit)
//    : BaseViewHolder<Genre, ItemHomeFourthFragmentBinding, GenreFragmentItemClickListener>
    : BaseViewHolder<Genre, ItemHomeFourthFragmentBinding>
(weakReference, itemView, itemClickListeners, itemLongClickListeners) {

    var genre = ObservableField<String>()

    override fun bindData(dataObject: Genre) {
        if (mBinding.itemview == null) {
            mBinding.itemview = this
        }
        super.bindData(dataObject)
        dataObject.let {
            val context by lazy { mViewHolderWeakReference.get() as Context }
            val defaultValue by lazy { context.getString(R.string.empty_string) }
            setStringObservableFieldValue(context, genre, it.genre, defaultValue)
        }
    }
}
