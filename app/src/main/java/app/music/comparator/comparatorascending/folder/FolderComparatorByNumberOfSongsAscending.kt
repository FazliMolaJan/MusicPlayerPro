package app.music.comparator.comparatorascending.folder

import app.music.model.Folder
import java.util.*

class FolderComparatorByNumberOfSongsAscending : Comparator<Folder> {
    override fun compare(folder1: Folder, folder2: Folder): Int {
        val numberOfSongs1 = folder1.musicList.size
        val numberOfSongs2 = folder2.musicList.size
        return numberOfSongs1.compareTo(numberOfSongs2).compareTo(0)
    }
}
