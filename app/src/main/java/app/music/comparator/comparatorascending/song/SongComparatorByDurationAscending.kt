package app.music.comparator.comparatorascending.song

import app.music.model.entity.BaseMusik
import java.util.*

class SongComparatorByDurationAscending : Comparator<BaseMusik> {

    override fun compare(song1: BaseMusik, song2: BaseMusik): Int {
        return song1.duration!!.compareTo(song2.duration!!).compareTo(0)
    }
}
