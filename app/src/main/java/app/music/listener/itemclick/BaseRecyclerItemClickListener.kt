package app.music.listener.itemclick

import android.content.Context
import android.os.SystemClock
import androidx.fragment.app.Fragment
import app.music.base.ContainListViewModel
import app.music.model.Album
import app.music.utils.DoubleClickUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils

interface BaseRecyclerItemClickListener {

    fun getContainContext(): Context? {
        return (
                if (this is Fragment) (this as Fragment).context
                else this as Context
                )
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils
                .getViewModel<ContainListViewModel>(this@BaseRecyclerItemClickListener as Fragment)
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
