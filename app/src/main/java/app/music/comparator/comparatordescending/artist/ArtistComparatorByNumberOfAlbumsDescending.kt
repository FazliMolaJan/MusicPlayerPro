package app.music.comparator.comparatordescending.artist

import app.music.model.entity.Artist
import java.util.*

class ArtistComparatorByNumberOfAlbumsDescending : Comparator<Artist> {
    override fun compare(artist1: Artist, artist2: Artist): Int {
        val numberOfAlbums1 = artist1.albumNameList.size
        val numberOfAlbums2 = artist2.albumNameList.size
        return 0.compareTo(numberOfAlbums1.compareTo(numberOfAlbums2))
    }
}
