package app.music.comparator.comparatorascending.artist

import app.music.model.entity.Artist
import java.util.*

class ArtistComparatorByAlphabetAscending : Comparator<Artist> {
    override fun compare(artist1: Artist, artist2: Artist): Int {
        return artist1.artistName.compareTo(artist2.artistName, true).compareTo(0)
    }
}
