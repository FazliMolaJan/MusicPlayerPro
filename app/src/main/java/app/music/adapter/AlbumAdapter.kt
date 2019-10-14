package app.music.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemHomeFirstFragmentBinding
import app.music.diffcallback.AlbumDiffCallBack
import app.music.diffcallback.FilterDiffCallBack
import app.music.model.Album
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.viewholder.AlbumViewHolder
import java.lang.ref.WeakReference


class AlbumAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (Album) -> Unit,
        private var itemLongClickListeners: (Album) -> Unit)
    : BaseRecyclerAdapter<Album, AlbumViewHolder>(mActivityWeakReference) {

    override val layoutId: Int = R.layout.item_home_first_fragment

    override fun getDiffResult(isFilter: Boolean, dataList: List<Album>, newItems: List<Album>)
            : DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                if (isFilter) FilterDiffCallBack(dataList, newItems)
                else AlbumDiffCallBack(dataList, newItems),
                false)
    }

    override fun getViewHolder(binding: ViewDataBinding): AlbumViewHolder {
        return AlbumViewHolder(
                mActivityReference,
                binding as ItemHomeFirstFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

    override fun isContainingFilterPatternItem(item: Album, filterPattern: String): Boolean {
        return item.albumName.toLowerCase().contains(filterPattern)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        RecyclerViewUtils.setToolbarScrollFlag(recyclerView, mActivityReference)
    }
}
