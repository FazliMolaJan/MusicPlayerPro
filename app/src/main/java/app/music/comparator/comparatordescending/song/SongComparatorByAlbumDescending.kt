package app.music.comparator.comparatordescending.song

import app.music.model.BaseMusik
import java.util.*

class SongComparatorByAlbumDescending : Comparator<BaseMusik> {

    override fun compare(music1: BaseMusik, music2: BaseMusik): Int {
        return 0.compareTo(music1.album.compareTo(music2.album, true))
    }
}
