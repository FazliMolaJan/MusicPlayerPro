package app.music.comparator.comparatorascending.song

import app.music.model.BaseMusik
import java.util.*

class SongComparatorByArtistAscending : Comparator<BaseMusik> {

    override fun compare(song1: BaseMusik, song2: BaseMusik): Int {
        return song1.artist.compareTo(song2.artist, true).compareTo(0)
    }
}
