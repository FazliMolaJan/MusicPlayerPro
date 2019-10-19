package app.music.listener.itemclick

import android.content.Context
import android.os.SystemClock
import androidx.fragment.app.Fragment
import app.music.base.ContainListViewModel
import app.music.model.entity.Folder
import app.music.ui.screen.home.HomeActivity
import app.music.utils.DoubleClickUtils
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.snackbar.SnackbarUtils
import app.music.utils.viewmodel.ViewModelUtils
import kotlinx.android.synthetic.main.activity_home.*

interface FolderFragmentItemClickListener : BaseRecyclerItemClickListener {

    fun onFolderClick(folder: Folder) {
        checkDoubleClick {
            IntentMethodUtils.launchDetailFolderActivity(getContainContext(), folder)
        }
    }

    fun onFolderLongClick(folder: Folder) {
        with(getContainContext()) {
            SnackbarUtils.showLengthShortSnackbar(
                    this as Context,
                    (this as HomeActivity).drawer_layout,
                    folder.folderName
            )
        }
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils
                .getViewModel<ContainListViewModel>(this@FolderFragmentItemClickListener as Fragment)
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
