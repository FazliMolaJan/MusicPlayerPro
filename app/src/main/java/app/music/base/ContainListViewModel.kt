package app.music.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.music.utils.toast.ToastUtil

open class ContainListViewModel (application: Application) : AndroidViewModel(application) {

    private var mItemLastClickTime = MutableLiveData<Long>()

    fun setItemLastClickTime(itemLastClickTime: Long) {
        this.mItemLastClickTime.value = itemLastClickTime
    }

    fun getItemLastClickTime(): Long {
        return mItemLastClickTime.value?.toLong() ?: 0
    }

    override fun onCleared() {
        super.onCleared()
        ToastUtil.showToast("on clear view model")
    }
}