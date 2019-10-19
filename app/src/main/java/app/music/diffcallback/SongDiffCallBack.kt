package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.entity.BaseMusik

class SongDiffCallBack<MusicType : BaseMusik>(oldList: List<MusicType>, newList: List<MusicType>)
    : BaseDiffCallBack<MusicType>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].location == mNewList[newItemPosition].location
        //        return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
        //        return false;
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //        return mOldList.get(oldItemPosition).getLocation()
        //                .equals(mNewList.get(newItemPosition).getLocation());
        //        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
        //        return false;

        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[newItemPosition]
        //        return oldItem.getTitle().equals(newItem.getTitle()) &&
        //                oldItem.getArtistWithContext().equals(newItem.getArtistWithContext()) &&
        //                oldItem.getDuration().equals(newItem.getDuration());
        return oldItem == newItem
    }
}
