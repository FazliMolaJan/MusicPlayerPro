package app.music.comparator.comparatorascending.playlist.offlineplaylist

import app.music.model.entity.Playlist
import java.util.*

class PlaylistComparatorByAlphabetAscending : Comparator<Playlist> {

    override fun compare(playlist1: Playlist, playlist2: Playlist): Int {
        return playlist1.playlistName.compareTo(playlist2.playlistName).compareTo(0)
    }
}
