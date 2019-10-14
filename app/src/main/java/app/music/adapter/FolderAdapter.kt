package app.music.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemHomeSixthFragmentBinding
import app.music.diffcallback.FilterDiffCallBack
import app.music.diffcallback.FolderDiffCallBack
import app.music.model.Folder
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.viewholder.FolderViewHolder
import java.lang.ref.WeakReference

class FolderAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (Folder) -> Unit,
        private var itemLongClickListeners: (Folder) -> Unit)
    : BaseRecyclerAdapter<Folder, FolderViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_home_sixth_fragment

    override fun getViewHolder(binding: ViewDataBinding): FolderViewHolder {
        return FolderViewHolder(
                mActivityReference,
                binding as ItemHomeSixthFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : FolderItemClickListener {
//            override fun onFolderClick(folder: Folder, isLongClick: Boolean) {
//                (activity as FolderItemClickListener).onFolderClick(folder, isLongClick)
//            }
//        }
//    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<Folder>, newItems: List<Folder>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(
                if (isFilter) FilterDiffCallBack(dataList, newItems)
                else FolderDiffCallBack(dataList, newItems),
                false)
    }

    override fun isContainingFilterPatternItem(item: Folder, filterPattern: String): Boolean {
        return item.folderName.toLowerCase().contains(filterPattern)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        RecyclerViewUtils.setToolbarScrollFlag(recyclerView, mActivityReference)
    }
}
