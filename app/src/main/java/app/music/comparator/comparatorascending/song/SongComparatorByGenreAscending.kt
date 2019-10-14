package app.music.comparator.comparatorascending.song

import app.music.model.BaseMusik
import java.util.*

class SongComparatorByGenreAscending : Comparator<BaseMusik> {

    override fun compare(song1: BaseMusik, song2: BaseMusik): Int {
        return song1.genre.compareTo(song2.genre, true).compareTo(0)
    }
}
