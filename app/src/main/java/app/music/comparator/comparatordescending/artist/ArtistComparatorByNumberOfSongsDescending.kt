package app.music.comparator.comparatordescending.artist

import app.music.model.entity.Artist
import java.util.*

class ArtistComparatorByNumberOfSongsDescending : Comparator<Artist> {
    override fun compare(artist1: Artist, artist2: Artist): Int {
        val numberOfSongs1 = artist1.musicList.size
        val numberOfSongs2 = artist2.musicList.size
        return 0.compareTo(numberOfSongs1.compareTo(numberOfSongs2))
    }
}
