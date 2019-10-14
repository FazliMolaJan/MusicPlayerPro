package app.music.listener.itemclick

import android.os.SystemClock
import androidx.fragment.app.Fragment
import app.music.base.ContainListViewModel
import app.music.model.Album
import app.music.utils.DoubleClickUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils

interface AlbumFragmentItemClickListener : BaseRecyclerItemClickListener {

    fun onAlbumClick(album: Album) {
        checkDoubleClick {
            IntentMethodUtils.launchDetailAlbumActivity(getContainContext(), album)
        }
    }

    fun onAlbumLongClick(album: Album) {
        ToastUtil.showToast("Album Long click${album.albumName}")
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils
                .getViewModel<ContainListViewModel>(this@AlbumFragmentItemClickListener as Fragment)
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
