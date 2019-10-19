package app.music.adapter.recycler

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemHomeThirdFragmentBinding
import app.music.diffcallback.ArtistDiffCallBack
import app.music.diffcallback.FilterDiffCallBack
import app.music.model.entity.Artist
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.viewholder.ArtistViewHolder
import java.lang.ref.WeakReference

class ArtistAdapter(mActivityWeakReference: WeakReference<Activity>,
                    private var itemClickListeners: (Artist) -> Unit,
                    private var itemLongClickListeners: (Artist) -> Unit)
    : BaseRecyclerAdapter<Artist, ArtistViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_home_third_fragment

    override fun getViewHolder(binding: ViewDataBinding): ArtistViewHolder {
        return ArtistViewHolder(
                mActivityReference,
                binding as ItemHomeThirdFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<Artist>, newItems: List<Artist>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                if (isFilter) FilterDiffCallBack(dataList, newItems)
                else ArtistDiffCallBack(dataList, newItems),
                false)
    }

    override fun isContainingFilterPatternItem(item: Artist, filterPattern: String): Boolean {
        return item.artistName.toLowerCase().contains(filterPattern)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        RecyclerViewUtils.setToolbarScrollFlag(recyclerView, mActivityReference)
    }
}
