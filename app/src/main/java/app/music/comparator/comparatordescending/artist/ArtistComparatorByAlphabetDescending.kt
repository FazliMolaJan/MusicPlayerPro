package app.music.comparator.comparatordescending.artist

import app.music.model.entity.Artist
import java.util.*

class ArtistComparatorByAlphabetDescending : Comparator<Artist> {
    override fun compare(artist1: Artist, artist2: Artist): Int {
        return 0.compareTo(artist1.artistName.compareTo(artist2.artistName, true))
    }
}
