package app.music.comparator.comparatordescending.song

import app.music.model.entity.BaseMusik
import java.util.*

class SongComparatorByDurationDescending : Comparator<BaseMusik> {

    override fun compare(music1: BaseMusik, music2: BaseMusik): Int {
        return 0.compareTo(music1.duration!!.compareTo(music2.duration!!))
    }
}
