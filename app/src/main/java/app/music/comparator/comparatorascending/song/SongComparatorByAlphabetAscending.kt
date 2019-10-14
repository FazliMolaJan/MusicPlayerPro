package app.music.comparator.comparatorascending.song

import app.music.model.BaseMusik
import java.util.*

class SongComparatorByAlphabetAscending : Comparator<BaseMusik> {

    override fun compare(song1: BaseMusik, song2: BaseMusik): Int {
        return song1.title.compareTo(song2.title, true).compareTo(0)
    }
}
