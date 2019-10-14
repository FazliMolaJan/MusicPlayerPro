package app.music.comparator.comparatordescending.album

import app.music.model.Album
import java.util.*

class AlbumComparatorByNumberOfSongsDescending : Comparator<Album> {
    override fun compare(album1: Album, album2: Album): Int {
        val album1Size = album1.musicList.size
        val album2Size = album2.musicList.size
        return 0.compareTo(album1Size.compareTo(album2Size))
    }
}
