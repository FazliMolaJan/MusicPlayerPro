package app.music.listener.itemclick

import android.os.SystemClock
import androidx.fragment.app.Fragment
import app.music.base.ContainListViewModel
import app.music.model.entity.OnlinePlaylist
import app.music.utils.DoubleClickUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils

interface OnlinePlaylistItemClickListener : BaseRecyclerItemClickListener {

    fun onPlaylistClick(playlist: OnlinePlaylist) {
        checkDoubleClick {
            IntentMethodUtils.launchDetailOnlinePlaylistActivity(getContainContext(), playlist)
        }
    }

    fun onPlaylistLongClick(playlist: OnlinePlaylist) {
        ToastUtil.showToast("Album Long click${playlist.playlistName}")
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils
                .getViewModel<ContainListViewModel>(this@OnlinePlaylistItemClickListener as Fragment)
        with(mHomeActivityViewModel) {
            if (!DoubleClickUtils.isDoubleClick(getItemLastClickTime())) {
                setItemLastClickTime(SystemClock.elapsedRealtime())
                listener()
            } else {
                setItemLastClickTime(SystemClock.elapsedRealtime())
            }
        }
    }
}
