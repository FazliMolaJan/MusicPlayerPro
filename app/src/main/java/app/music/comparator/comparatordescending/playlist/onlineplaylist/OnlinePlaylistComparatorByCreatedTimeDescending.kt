package app.music.comparator.comparatordescending.playlist.onlineplaylist

import app.music.model.OnlinePlaylist
import java.util.*

class OnlinePlaylistComparatorByCreatedTimeDescending : Comparator<OnlinePlaylist> {

    override fun compare(playlist1: OnlinePlaylist, playlist2: OnlinePlaylist): Int {
        val createdTime1 = playlist1.createdTime
        val createdTime2 = playlist2.createdTime
        return 0.compareTo(createdTime1.compareTo(createdTime2))
    }
}
