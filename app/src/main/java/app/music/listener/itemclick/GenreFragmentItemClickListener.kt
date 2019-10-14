package app.music.listener.itemclick

import android.os.SystemClock
import androidx.fragment.app.Fragment
import app.music.base.ContainListViewModel
import app.music.model.Genre
import app.music.utils.DoubleClickUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils

interface GenreFragmentItemClickListener : BaseRecyclerItemClickListener {

    fun onGenreClick(genre: Genre) {
        checkDoubleClick {
            IntentMethodUtils.launchDetailGenreActivity(getContainContext(), genre)
        }
    }

    fun onGenreLongClick(genre: Genre) {
        ToastUtil.showToast("Album Long click${genre.genre}")
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils
                .getViewModel<ContainListViewModel>(this@GenreFragmentItemClickListener as Fragment)
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
