package app.music.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemArtistSecondFragmentBinding
import app.music.diffcallback.SongDiffCallBack
import app.music.model.BaseMusik
import app.music.viewholder.ArtistSongViewHolder
import java.lang.ref.WeakReference

class ArtistSongAdapter(
        mActivityWeakReference: WeakReference<Activity>,
        private var itemClickListeners: (BaseMusik) -> Unit,
        private var itemLongClickListeners: (BaseMusik) -> Unit)
    : BaseRecyclerAdapter<BaseMusik, ArtistSongViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_artist_second_fragment

    override fun isContainingFilterPatternItem(item: BaseMusik, filterPattern: String): Boolean {
        return item.title.toLowerCase().contains(filterPattern)
    }

    override fun getViewHolder(binding: ViewDataBinding): ArtistSongViewHolder {
        return ArtistSongViewHolder(
                mActivityReference,
                binding as ItemArtistSecondFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : SongItemClickListener {
//            override fun <MusicType : BaseMusik> onSongClick(position: Int, musicList: List<MusicType>, isLongClick: Boolean) {
//                (activity as SongItemClickListener).onSongClick(position, musicList, isLongClick)
//            }
//        }
//    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<BaseMusik>, newItems: List<BaseMusik>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(SongDiffCallBack(dataList, newItems), false)
    }
}
