package app.music.comparator.comparatorascending.playlist.onlineplaylist

import app.music.model.OnlinePlaylist
import java.util.*

class OnlinePlaylistComparatorByCreatedTimeAscending : Comparator<OnlinePlaylist> {

    override fun compare(playlist1: OnlinePlaylist, playlist2: OnlinePlaylist): Int {
        val createdTime1 = playlist1.createdTime
        val createdTime2 = playlist2.createdTime
        return createdTime1.compareTo(createdTime2).compareTo(0)
    }
}
