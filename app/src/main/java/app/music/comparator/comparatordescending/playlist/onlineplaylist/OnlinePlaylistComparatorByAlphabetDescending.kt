package app.music.comparator.comparatordescending.playlist.onlineplaylist

import app.music.model.OnlinePlaylist
import java.util.*

class OnlinePlaylistComparatorByAlphabetDescending : Comparator<OnlinePlaylist> {

    override fun compare(playlist1: OnlinePlaylist, playlist2: OnlinePlaylist): Int {
        return 0.compareTo(playlist1.playlistName.compareTo(playlist2.playlistName))
    }
}
