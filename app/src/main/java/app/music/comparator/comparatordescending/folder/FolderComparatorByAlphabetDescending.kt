package app.music.comparator.comparatordescending.folder

import app.music.model.entity.Folder
import java.util.*

class FolderComparatorByAlphabetDescending : Comparator<Folder> {
    override fun compare(folder1: Folder, folder2: Folder): Int {
        return 0.compareTo(folder1.folderName.compareTo(folder2.folderName))
    }
}
