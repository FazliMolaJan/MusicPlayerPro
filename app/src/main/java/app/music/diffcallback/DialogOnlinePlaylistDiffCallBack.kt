package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.entity.OnlinePlaylist

class DialogOnlinePlaylistDiffCallBack(oldList: List<OnlinePlaylist>, newList: List<OnlinePlaylist>)
    : BaseDiffCallBack<OnlinePlaylist>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].playlistName == mNewList[newItemPosition].playlistName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].playlistName == mNewList[newItemPosition].playlistName
    }
}
