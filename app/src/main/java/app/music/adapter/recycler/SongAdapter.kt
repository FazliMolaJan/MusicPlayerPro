package app.music.adapter.recycler

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemHomeSecondFragmentBinding
import app.music.diffcallback.FilterDiffCallBack
import app.music.diffcallback.SongDiffCallBack
import app.music.model.entity.BaseMusik
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.viewholder.MusicViewHolder
import java.lang.ref.WeakReference

class SongAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (BaseMusik) -> Unit,
        private var itemLongClickListeners: (BaseMusik) -> Unit)
    : BaseRecyclerAdapter<BaseMusik, MusicViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_home_second_fragment

    override fun getViewHolder(binding: ViewDataBinding): MusicViewHolder {
        return MusicViewHolder(
                mActivityReference,
                binding as ItemHomeSecondFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<BaseMusik>, newItems: List<BaseMusik>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                if (isFilter)
                    FilterDiffCallBack(dataList, newItems)
                else
                    SongDiffCallBack(dataList, newItems),
                false
        )
    }

    override fun isContainingFilterPatternItem(item: BaseMusik, filterPattern: String): Boolean {
        return item.title.toLowerCase().contains(filterPattern)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            RecyclerViewUtils.setToolbarScrollFlag(recyclerView, mActivityReference)
        }
    }
}
