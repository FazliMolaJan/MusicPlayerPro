package app.music.listener.itemclick

import android.os.SystemClock
import androidx.fragment.app.Fragment
import app.music.base.ContainListViewModel
import app.music.model.entity.Artist
import app.music.utils.DoubleClickUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils

interface ArtistFragmentItemClickListener : BaseRecyclerItemClickListener {

    fun onArtistClick(artist: Artist) {
        checkDoubleClick {
            IntentMethodUtils.launchDetailArtistActivity(getContainContext(), artist)
        }
    }

    fun onArtistLongClick(artist: Artist) {
        ToastUtil.showToast("Album Long click${artist.artistName}")
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils
                .getViewModel<ContainListViewModel>(this@ArtistFragmentItemClickListener as Fragment)
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
