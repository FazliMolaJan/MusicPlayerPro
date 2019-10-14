package app.music.comparator.comparatordescending.playlist.offlineplaylist

import app.music.model.Playlist
import java.util.*

class PlaylistComparatorByAlphabetDescending : Comparator<Playlist> {

    override fun compare(playlist1: Playlist, playlist2: Playlist): Int {
        return 0.compareTo(playlist1.playlistName.compareTo(playlist2.playlistName))
    }
}
