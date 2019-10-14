package app.music.listener.homefragmentlistener

import app.music.listener.RecyclerScrollToTopListener

interface ArtistFragmentListener : RecyclerScrollToTopListener {
    fun onSortArtist(sortBy: String, isAscending: String)
}
