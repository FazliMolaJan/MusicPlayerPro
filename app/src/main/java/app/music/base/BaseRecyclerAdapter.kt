package app.music.base

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Handler
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable

import java.lang.ref.WeakReference
import java.util.ArrayDeque
import java.util.ArrayList
import java.util.Queue

abstract class BaseRecyclerAdapter<T, H : BaseViewHolder<T, *>>
(protected var mActivityReference: WeakReference<Activity>)
    : RecyclerView.Adapter<H>(), Filterable {

    protected var mDataList: MutableList<T> = ArrayList()
    private val mDataListFull = ArrayList<T>()
    private val mPendingUpdates = ArrayDeque<List<T>>()

    private val dataFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            var filteredList: MutableList<T> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList = ArrayList(mDataListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in mDataListFull) {
                    if (isContainingFilterPatternItem(item, filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = filteredList

            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            updateItems(true, results.values as List<T>)
        }
    }

    protected abstract val layoutId: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false)
        return getViewHolder(binding)
    }

    override fun onBindViewHolder(holder: H, bindPosition: Int) {
        Log.i(TAG, "on bind view holder")
        val activity = mActivityReference.get()
        if (activity == null || mDataList == null || mDataList!!.size < bindPosition) return
        holder.bindData(mDataList!![bindPosition])
    }

    override fun getItemCount(): Int {
        return if (mDataList != null) mDataList!!.size else 0
    }

    override fun getFilter(): Filter {
        return dataFilter
        //        return null;
    }

    fun updateItems(isFilter: Boolean, newItems: List<T>) {
        mPendingUpdates.add(newItems)
        if (mPendingUpdates.size > 1) return
        updateItemsInternal(isFilter, newItems)
    }

    protected abstract fun getViewHolder(binding: ViewDataBinding): H

    protected abstract fun getDiffResult(isFilter: Boolean, dataList: List<T>, newItems: List<T>): DiffUtil.DiffResult

    protected abstract fun isContainingFilterPatternItem(item: T, filterPattern: String): Boolean

    private fun updateItemsInternal(isFilter: Boolean, newItems: List<T>) {
        val handler = Handler()
        Thread {
            val diffResult = getDiffResult(isFilter, mDataList, newItems)
            handler.post { applyDiffResult(isFilter, newItems, diffResult) }
        }.start()
    }

    private fun applyDiffResult(isFilter: Boolean, newItems: List<T>?, diffResult: DiffUtil.DiffResult) {
        mPendingUpdates.remove()
        dispatchUpdate(isFilter, newItems, diffResult)
        if (mPendingUpdates.size > 0) {
            updateItemsInternal(isFilter, mPendingUpdates.peek())
        }
    }

    private fun dispatchUpdate(isFilter: Boolean, newItems: List<T>?, diffResult: DiffUtil.DiffResult?) {
        diffResult?.dispatchUpdatesTo(this@BaseRecyclerAdapter)
        if (!isFilter) {
            mDataListFull.clear()
            mDataListFull.addAll(newItems!!)
        }
        mDataList.clear()
        mDataList.addAll(newItems!!)
        //        mDataList = new ArrayList<>(newItems);
    }

    companion object {
        private val TAG = "BaseRecyclerAdapter"
    }
}
