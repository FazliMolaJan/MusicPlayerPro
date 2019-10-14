package app.music.comparator.comparatorascending.playlist.onlineplaylist

import app.music.model.OnlinePlaylist
import java.util.*

class OnlinePlaylistComparatorByNumberOfSongsAscending : Comparator<OnlinePlaylist> {

    override fun compare(playlist1: OnlinePlaylist, playlist2: OnlinePlaylist): Int {
        val numberOfSongs1 = playlist1.musicList.size
        val numberOfSongs2 = playlist2.musicList.size
        return numberOfSongs1.compareTo(numberOfSongs2).compareTo(0)
    }
}
