package app.music.listener.itemclick

import android.os.SystemClock
import androidx.fragment.app.Fragment
import app.music.base.ContainListViewModel
import app.music.model.Playlist
import app.music.utils.DoubleClickUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils

interface PlaylistFragmentItemClickListener : BaseRecyclerItemClickListener {

    fun onPlaylistClick(playlist: Playlist) {
        checkDoubleClick {
            IntentMethodUtils.launchDetailPlaylistActivity(getContainContext(), playlist)
        }
    }

    fun onPlaylistLongClick(playlist: Playlist) {
        ToastUtil.showToast("Album Long click${playlist.playlistName}")
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils
                .getViewModel<ContainListViewModel>(this@PlaylistFragmentItemClickListener as Fragment)
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
