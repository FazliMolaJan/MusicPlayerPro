package app.music.listener.homefragmentlistener

import app.music.listener.RecyclerScrollToTopListener

interface SongFragmentListener : RecyclerScrollToTopListener {

    fun onSortMusic(sortBy: String, isAscending: String)

//    fun onFilterList(filterPattern: String)
}
