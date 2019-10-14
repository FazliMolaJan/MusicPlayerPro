package app.music.comparator.comparatorascending.genre

import app.music.model.Genre
import java.util.*

class GenreComparatorByNumberOfSongsAscending : Comparator<Genre> {
    override fun compare(genre1: Genre, genre2: Genre): Int {
        val numberOfSongs1 = genre1.musicList.size
        val numberOfSongs2 = genre2.musicList.size
        return numberOfSongs1.compareTo(numberOfSongs2).compareTo(0)
    }
}
