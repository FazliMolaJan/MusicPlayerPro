package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.entity.Artist

class ArtistDiffCallBack(oldList: List<Artist>, newList: List<Artist>)
    : BaseDiffCallBack<Artist>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].artistName == mNewList[newItemPosition].artistName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[oldItemPosition]
        //        return mOldList.get(oldItemPosition).getMusicList().size()
        //                == mNewList.get(newItemPosition).getMusicList().size();
        return oldItem == newItem
    }
}
