package app.music.comparator.comparatordescending.folder

import app.music.model.entity.Folder
import java.util.*

class FolderComparatorByNumberOfSongsDescending : Comparator<Folder> {
    override fun compare(folder1: Folder, folder2: Folder): Int {
        val numberOfSongs1 = folder1.musicList.size
        val numberOfSongs2 = folder2.musicList.size
        return 0.compareTo(numberOfSongs1.compareTo(numberOfSongs2))
    }
}
