package app.music.comparator.comparatorascending.playlist.offlineplaylist

import app.music.model.Playlist
import java.util.*

class PlaylistComparatorByNumberOfSongsAscending : Comparator<Playlist> {

    override fun compare(playlist1: Playlist, playlist2: Playlist): Int {
        val numberOfSongs1 = playlist1.musicList.size
        val numberOfSongs2 = playlist2.musicList.size
        return numberOfSongs1.compareTo(numberOfSongs2).compareTo(0)
    }
}
