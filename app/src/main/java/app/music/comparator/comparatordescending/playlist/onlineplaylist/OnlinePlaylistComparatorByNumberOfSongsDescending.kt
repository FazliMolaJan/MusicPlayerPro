package app.music.comparator.comparatordescending.playlist.onlineplaylist

import app.music.model.entity.OnlinePlaylist
import java.util.*

class OnlinePlaylistComparatorByNumberOfSongsDescending : Comparator<OnlinePlaylist> {

    override fun compare(playlist1: OnlinePlaylist, playlist2: OnlinePlaylist): Int {
        val numberOfSongs1 = playlist1.musicList.size
        val numberOfSongs2 = playlist2.musicList.size
        return 0.compareTo(numberOfSongs1.compareTo(numberOfSongs2))
    }
}
