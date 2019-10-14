package app.music.comparator.comparatorascending.song

import app.music.model.BaseMusik
import java.util.*

class SongComparatorByAlbumAscending : Comparator<BaseMusik> {

    override fun compare(music1: BaseMusik, music2: BaseMusik): Int {
        return music1.album.compareTo(music2.album, true).compareTo(0)
    }
}
