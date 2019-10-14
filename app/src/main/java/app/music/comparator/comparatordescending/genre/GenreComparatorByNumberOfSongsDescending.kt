package app.music.comparator.comparatordescending.genre

import app.music.model.Genre
import java.util.*

class GenreComparatorByNumberOfSongsDescending : Comparator<Genre> {
    override fun compare(genre1: Genre, genre2: Genre): Int {
        val numberOfSongs1 = genre1.musicList.size
        val numberOfSongs2 = genre2.musicList.size
        return 0.compareTo(numberOfSongs1.compareTo(numberOfSongs2))
    }
}
