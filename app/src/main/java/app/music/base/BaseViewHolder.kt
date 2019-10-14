package app.music.base

import android.app.Activity
import android.content.Context
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

abstract class BaseViewHolder<T, B : ViewDataBinding>(
        protected var mViewHolderWeakReference: WeakReference<Activity>,
        protected var mBinding: B,
        protected var itemClickListeners: (T) -> Unit,
        protected var itemLongClickListeners: (T) -> Unit)
    : RecyclerView.ViewHolder(mBinding.root) {

    protected lateinit var mDataList: MutableList<T>
    protected var mMetadataRetriever = MediaMetadataRetriever()

    open fun bindData(dataObject: T) {
        with(mBinding.root) {
            setOnClickListener {
                itemClickListeners(dataObject)
            }
            setOnLongClickListener {
                itemLongClickListeners(dataObject)
                true
            }
        }
    }

    open fun setStringObservableFieldValue(
            context: Context, observableField: ObservableField<String>, value: String,
            defaultValue: String) {
        observableField.set(
                if (TextUtils.isEmpty(value)) defaultValue
                else value
        )
    }

    companion object {
        private val TAG = "BaseViewHolder"
    }
}
