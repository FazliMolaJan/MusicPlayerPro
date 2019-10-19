package app.music.adapter.recycler

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemHomeFourthFragmentBinding
import app.music.diffcallback.FilterDiffCallBack
import app.music.diffcallback.GenreDiffCallBack
import app.music.model.entity.Genre
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.viewholder.GenreViewHolder
import java.lang.ref.WeakReference

class GenreAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (Genre) -> Unit,
        private var itemLongClickListeners: (Genre) -> Unit)
    : BaseRecyclerAdapter<Genre, GenreViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_home_fourth_fragment

    override fun getViewHolder(binding: ViewDataBinding): GenreViewHolder {
        return GenreViewHolder(
                mActivityReference,
                binding as ItemHomeFourthFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : GenreFragmentItemClickListener {
//            override fun onGenreClick(genre: Genre, isLongClick: Boolean) {
//                (activity as GenreFragmentItemClickListener).onGenreClick(genre, isLongClick)
//            }
//        }
//    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<Genre>, newItems: List<Genre>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                if (isFilter) FilterDiffCallBack(dataList, newItems)
                else GenreDiffCallBack(dataList, newItems),
                false)
    }

    override fun isContainingFilterPatternItem(item: Genre, filterPattern: String): Boolean {
        return item.genre.toLowerCase().contains(filterPattern)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        RecyclerViewUtils.setToolbarScrollFlag(recyclerView, mActivityReference)
    }
}
