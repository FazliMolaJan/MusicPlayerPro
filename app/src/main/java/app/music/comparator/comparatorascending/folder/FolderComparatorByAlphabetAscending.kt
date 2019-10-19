package app.music.comparator.comparatorascending.folder

import app.music.model.entity.Folder
import java.util.*

class FolderComparatorByAlphabetAscending : Comparator<Folder> {
    override fun compare(folder1: Folder, folder2: Folder): Int {
        return folder1.folderName.compareTo(folder2.folderName).compareTo(0)
    }
}
