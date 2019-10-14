package app.music.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.music.base.ContainListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import app.music.utils.toast.ToastUtil
import com.rengwuxian.materialedittext.MaterialEditText

open class BaseHomeActivityViewModel(application: Application) : ContainListViewModel(application) {

    var mIsSearching = MutableLiveData<Boolean>()
    private var mSearchingText = MutableLiveData<String>()
    private var mPlaylistDialog = MutableLiveData<BottomSheetDialog>()
    private var mNewPlaylistDialog = MutableLiveData<BottomSheetDialog>()
    private var mEditNewPlaylistName = MutableLiveData<MaterialEditText>()

    fun setPlaylistDialog(playlistDialog: BottomSheetDialog) {
        this.mPlaylistDialog.value = playlistDialog
    }

    fun getPlaylistDialog(): BottomSheetDialog? {
        return mPlaylistDialog.value
    }

    fun setNewPlaylistDialog(newPlaylistDialog: BottomSheetDialog) {
        this.mNewPlaylistDialog.value = newPlaylistDialog
    }

    fun getNewPlaylistDialog(): BottomSheetDialog? {
        return mNewPlaylistDialog.value
    }

    fun setEditNewPlaylistName(newPlaylistDialog: MaterialEditText) {
        this.mEditNewPlaylistName.value = newPlaylistDialog
    }

    fun getEditNewPlaylistName(): MaterialEditText? {
        return mEditNewPlaylistName.value
    }

    fun setSearchingText(searchingText: String?) {
        this.mSearchingText.value = searchingText
    }

    fun getSearchingText(): String? {
        return mSearchingText.value?.toString() ?: null
    }

    fun setSearching(searching: Boolean) {
        this.mIsSearching.value = searching
    }

    fun getSearching(): Boolean {
        return mIsSearching.value ?: false
    }

    override fun onCleared() {
        super.onCleared()
        ToastUtil.showToast("on clear view model")
    }
}
