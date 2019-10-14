package app.music.listener.homefragmentlistener

import app.music.listener.RecyclerScrollToTopListener

interface AlbumFragmentListener : RecyclerScrollToTopListener {
    fun onSortAlbum(sortBy: String, orderBy: String)
}
