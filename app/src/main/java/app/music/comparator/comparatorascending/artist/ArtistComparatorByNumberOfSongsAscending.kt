package app.music.comparator.comparatorascending.artist

import app.music.model.entity.Artist
import java.util.*

class ArtistComparatorByNumberOfSongsAscending : Comparator<Artist> {
    override fun compare(artist1: Artist, artist2: Artist): Int {
        val numberOfSongs1 = artist1.musicList.size
        val numberOfSongs2 = artist2.musicList.size
        return numberOfSongs1.compareTo(numberOfSongs2).compareTo(0)
    }
}
