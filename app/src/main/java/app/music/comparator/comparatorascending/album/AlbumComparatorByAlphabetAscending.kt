package app.music.comparator.comparatorascending.album

import app.music.model.Album
import java.util.*

class AlbumComparatorByAlphabetAscending : Comparator<Album> {
    override fun compare(album1: Album, album2: Album): Int {
        return album1.albumName.compareTo(album2.albumName).compareTo(0)
    }
}
