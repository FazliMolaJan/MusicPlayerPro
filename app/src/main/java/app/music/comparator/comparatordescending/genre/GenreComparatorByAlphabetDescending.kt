package app.music.comparator.comparatordescending.genre

import app.music.model.Genre
import java.util.*

class GenreComparatorByAlphabetDescending : Comparator<Genre> {
    override fun compare(genre1: Genre, genre2: Genre): Int {
        return 0.compareTo(genre1.genre.compareTo(genre2.genre))
    }
}
