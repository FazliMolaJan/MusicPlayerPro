package app.music.listener.homefragmentlistener

import app.music.listener.RecyclerScrollToTopListener

interface FolderFragmentListener : RecyclerScrollToTopListener {

    fun onSortFolder(sortBy: String, isAscending: String)
}
