package app.music.listener.homefragmentlistener

import app.music.listener.RecyclerScrollToTopListener

interface PlaylistFragmentListener : RecyclerScrollToTopListener {
    fun onSortPlaylist(sortBy: String, isAscending: String)
}
