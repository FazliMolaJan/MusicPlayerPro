package app.music.comparator.comparatordescending.playlist.offlineplaylist

import app.music.model.Playlist
import java.util.*

class PlaylistComparatorByNumberOfSongsDescending : Comparator<Playlist> {

    override fun compare(playlist1: Playlist, playlist2: Playlist): Int {
        val numberOfSongs1 = playlist1.musicList.size
        val numberOfSongs2 = playlist2.musicList.size
        return 0.compareTo(numberOfSongs1.compareTo(numberOfSongs2))
    }
}
