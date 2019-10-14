package app.music.listener.homefragmentlistener

import app.music.listener.RecyclerScrollToTopListener

interface GenreFragmentListener : RecyclerScrollToTopListener {
    fun onSortGenre(sortBy: String, isAscending: String)
}
