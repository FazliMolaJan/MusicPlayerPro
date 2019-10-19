package app.music.comparator.comparatorascending.genre

import app.music.model.entity.Genre
import java.util.*

class GenreComparatorByAlphabetAscending : Comparator<Genre> {
    override fun compare(genre1: Genre, genre2: Genre): Int {
        return genre1.genre.compareTo(genre2.genre).compareTo(0)
    }
}
