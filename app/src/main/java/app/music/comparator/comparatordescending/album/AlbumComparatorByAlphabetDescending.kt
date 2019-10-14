package app.music.comparator.comparatordescending.album

import app.music.model.Album
import java.util.*

class AlbumComparatorByAlphabetDescending : Comparator<Album> {
    override fun compare(album1: Album, album2: Album): Int {
        return 0.compareTo(album1.albumName.compareTo(album2.albumName))
    }
}
