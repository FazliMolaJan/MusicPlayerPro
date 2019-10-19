package app.music.adapter.recycler

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemMusicListBinding
import app.music.diffcallback.SongDiffCallBack
import app.music.model.entity.BaseMusik
import app.music.viewholder.AlbumSongViewHolder
import java.lang.ref.WeakReference

class AlbumSongAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (BaseMusik) -> Unit,
        private var itemLongClickListeners: (BaseMusik) -> Unit)
    : BaseRecyclerAdapter<BaseMusik, AlbumSongViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_music_list

    override fun isContainingFilterPatternItem(item: BaseMusik, filterPattern: String): Boolean {
        return item.title.toLowerCase().contains(filterPattern)
    }

    override fun getViewHolder(binding: ViewDataBinding): AlbumSongViewHolder {
        return AlbumSongViewHolder(
                mActivityReference,
                binding as ItemMusicListBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

    override fun getDiffResult(isFilter: Boolean, dataList: List<BaseMusik>, newItems: List<BaseMusik>)
            : DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(SongDiffCallBack(dataList, newItems), false)
    }
}
