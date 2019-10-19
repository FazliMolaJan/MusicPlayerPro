package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.entity.Genre

class GenreDiffCallBack(oldList: List<Genre>, newList: List<Genre>)
    : BaseDiffCallBack<Genre>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].genre == mNewList[newItemPosition].genre
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMusicList = mOldList[oldItemPosition].musicList
        val newMusicList = mNewList[oldItemPosition].musicList
        return oldMusicList == newMusicList
        //        return oldMusicList.size() != newMusicList.size();
    }
}
