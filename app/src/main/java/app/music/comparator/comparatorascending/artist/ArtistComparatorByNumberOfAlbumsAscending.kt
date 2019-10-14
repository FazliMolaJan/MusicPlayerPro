package app.music.comparator.comparatorascending.artist

import app.music.model.Artist
import java.util.*

class ArtistComparatorByNumberOfAlbumsAscending : Comparator<Artist> {
    override fun compare(artist1: Artist, artist2: Artist): Int {
        val numberOfAlbums1 = artist1.albumNameList.size
        val numberOfAlbums2 = artist2.albumNameList.size
        return numberOfAlbums1.compareTo(numberOfAlbums2).compareTo(0)
    }
}
