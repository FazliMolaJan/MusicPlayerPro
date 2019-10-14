package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.Playlist

class DialogPlaylistDiffCallBack(oldList: List<Playlist>, newList: List<Playlist>)
    : BaseDiffCallBack<Playlist>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].playlistName == mNewList[newItemPosition].playlistName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].playlistName == mNewList[newItemPosition].playlistName
    }
}
