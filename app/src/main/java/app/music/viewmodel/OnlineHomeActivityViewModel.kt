package app.music.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import app.music.adapter.DialogOnlinePlaylistAdapter
import app.music.model.OnlineMusik

class OnlineHomeActivityViewModel(application: Application) : BaseHomeActivityViewModel(application) {

    private var mLastAddToOnlinePlaylistObject = MutableLiveData<OnlineMusik>()
    private var mDialogOnlinePlaylistAdapter = MutableLiveData<DialogOnlinePlaylistAdapter>()

    fun setLastAddToOnlinePlaylistObject(music: OnlineMusik) {
        this.mLastAddToOnlinePlaylistObject.value = music
    }

    fun getLastAddToOnlinePlaylistObject(): OnlineMusik? {
        return mLastAddToOnlinePlaylistObject.value
    }

    fun setDialogOnlinePlaylistAdapter(adapter: DialogOnlinePlaylistAdapter) {
        this.mDialogOnlinePlaylistAdapter.value = adapter
    }

    fun getDialogOnlinePlaylistAdapter(): DialogOnlinePlaylistAdapter? {
        return mDialogOnlinePlaylistAdapter.value
    }
}
