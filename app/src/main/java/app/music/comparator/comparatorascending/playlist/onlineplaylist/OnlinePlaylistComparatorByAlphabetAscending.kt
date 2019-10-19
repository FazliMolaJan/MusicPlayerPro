package app.music.comparator.comparatorascending.playlist.onlineplaylist

import app.music.model.entity.OnlinePlaylist
import java.util.*

class OnlinePlaylistComparatorByAlphabetAscending : Comparator<OnlinePlaylist> {

    override fun compare(playlist1: OnlinePlaylist, playlist2: OnlinePlaylist): Int {
        return playlist1.playlistName.compareTo(playlist2.playlistName).compareTo(0)
    }
}
