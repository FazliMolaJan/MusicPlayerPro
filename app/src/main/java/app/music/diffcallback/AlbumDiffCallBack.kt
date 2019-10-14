package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.Album

class AlbumDiffCallBack(oldList: List<Album>, newList: List<Album>)
    : BaseDiffCallBack<Album>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].albumName == mNewList[newItemPosition].albumName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[newItemPosition]
        //        return oldItem.getAlbumName().equals(newItem.getAlbumName()) &&
        //                oldItem.getMusicList().get(0).getArtistWithContext()
        //                        .equals(newItem.getMusicList().get(0).getArtistWithContext());
        return oldItem == newItem
    }
}
