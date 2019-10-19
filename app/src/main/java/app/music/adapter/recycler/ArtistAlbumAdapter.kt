package app.music.adapter.recycler

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import app.music.R
import app.music.base.BaseRecyclerAdapter
import app.music.databinding.ItemArtistFirstFragmentBinding
import app.music.diffcallback.AlbumDiffCallBack
import app.music.model.entity.Album
import app.music.viewholder.ArtistAlbumViewHolder
import java.lang.ref.WeakReference

class ArtistAlbumAdapter(mActivityWeakReference: WeakReference<Activity>,
                         private var itemClickListeners: (Album) -> Unit,
                         private var itemLongClickListeners: (Album) -> Unit)
    : BaseRecyclerAdapter<Album, ArtistAlbumViewHolder>(mActivityWeakReference) {

    override val layoutId = R.layout.item_artist_first_fragment

    override fun getViewHolder(binding: ViewDataBinding): ArtistAlbumViewHolder {
        return ArtistAlbumViewHolder(
                mActivityReference,
                binding as ItemArtistFirstFragmentBinding,
                itemClickListeners,
                itemLongClickListeners
        )
    }

//    override fun getItemClickListener(activity: Activity): Any {
//        return object : AlbumFragmentItemClickListener {
//            override fun onAlbumClick(album: Album, isLongClick: Boolean) {
//                (activity as AlbumFragmentItemClickListener).onAlbumClick(album, isLongClick)
//            }
//        }
//    }

    override fun getDiffResult(
            isFilter: Boolean, dataList: List<Album>, newItems: List<Album>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(AlbumDiffCallBack(dataList, newItems), false)
    }

    override fun isContainingFilterPatternItem(item: Album, filterPattern: String): Boolean {
        return item.albumName.toLowerCase().contains(filterPattern)
    }
}
