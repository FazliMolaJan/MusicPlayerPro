package app.music.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import app.music.adapter.DialogPlaylistAdapter
import app.music.model.Music

class HomeActivityViewModel(application: Application) : BaseHomeActivityViewModel(application) {

    private var mLastAddToPlaylistObject = MutableLiveData<Music>()
    private var mDialogPlaylistAdapter = MutableLiveData<DialogPlaylistAdapter>()

    fun setLastAddToPlaylistObject(music: Music) {
        this.mLastAddToPlaylistObject.value = music
    }

    fun getLastAddToPlaylistObject(): Music? = mLastAddToPlaylistObject.value

    fun setDialogPlaylistAdapter(adapter: DialogPlaylistAdapter) {
        this.mDialogPlaylistAdapter.value = adapter
    }

    fun getDialogPlaylistAdapter(): DialogPlaylistAdapter? = mDialogPlaylistAdapter.value
}
