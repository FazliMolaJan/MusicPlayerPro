package app.music.comparator.comparatordescending.playlist.offlineplaylist

import app.music.model.entity.Playlist
import java.util.*

class PlaylistComparatorByCreatedTimeDescending : Comparator<Playlist> {

    override fun compare(playlist1: Playlist, playlist2: Playlist): Int {
        val createdTime1 = playlist1.createdTime
        val createdTime2 = playlist2.createdTime
        return 0.compareTo(createdTime1.compareTo(createdTime2))
    }
}
