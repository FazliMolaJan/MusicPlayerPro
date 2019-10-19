package app.music.comparator.comparatorascending.album

import app.music.model.entity.Album
import java.util.*

class AlbumComparatorByNumberOfSongsAscending : Comparator<Album> {
    override fun compare(album1: Album, album2: Album): Int {
        val album1Size = album1.musicList.size
        val album2Size = album2.musicList.size
        return album1Size.compareTo(album2Size).compareTo(0)
    }
}
