package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.Playlist

class PlaylistDiffCallBack(oldList: List<Playlist>, newList: List<Playlist>)
    : BaseDiffCallBack<Playlist>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].createdTime == mNewList[newItemPosition].createdTime
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[oldItemPosition]
        //        return mOldList.get(oldItemPosition).getMusicList().size()
        //                == (mNewList.get(oldItemPosition).getMusicList().size());
        return oldItem == newItem
    }
}
