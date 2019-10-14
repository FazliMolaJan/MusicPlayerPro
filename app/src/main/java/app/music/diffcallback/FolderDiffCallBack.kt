package app.music.diffcallback

import app.music.base.BaseDiffCallBack
import app.music.model.Folder

class FolderDiffCallBack(oldList: List<Folder>, newList: List<Folder>)
    : BaseDiffCallBack<Folder>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].folderName == mNewList[newItemPosition].folderName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMusicList = mOldList[oldItemPosition].musicList
        val newMusicList = mNewList[oldItemPosition].musicList
        return oldMusicList == newMusicList
        //        return oldMusicList.size() != newMusicList.size();
    }
}
