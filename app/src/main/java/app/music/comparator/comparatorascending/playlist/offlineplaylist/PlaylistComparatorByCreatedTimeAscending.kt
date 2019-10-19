package app.music.comparator.comparatorascending.playlist.offlineplaylist

import app.music.model.entity.Playlist
import java.util.*

class PlaylistComparatorByCreatedTimeAscending : Comparator<Playlist> {

    override fun compare(playlist1: Playlist, playlist2: Playlist): Int {
        val createdTime1 = playlist1.createdTime
        val createdTime2 = playlist2.createdTime
        return createdTime1.compareTo(createdTime2).compareTo(0)
    }
}
