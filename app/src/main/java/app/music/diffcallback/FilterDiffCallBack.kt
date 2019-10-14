package app.music.diffcallback

import app.music.base.BaseDiffCallBack

class FilterDiffCallBack<T>(oldList: List<T>, newList: List<T>)
    : BaseDiffCallBack<T>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return false
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
