package app.music.comparator.comparatordescending.song

import app.music.model.entity.BaseMusik
import java.util.*

class SongComparatorByArtistDescending : Comparator<BaseMusik> {

    override fun compare(music1: BaseMusik, music2: BaseMusik): Int {
        return 0.compareTo(music1.artist.compareTo(music2.artist, true))
    }
}
