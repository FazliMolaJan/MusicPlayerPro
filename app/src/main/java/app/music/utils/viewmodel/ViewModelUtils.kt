package app.music.utils.viewmodel

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

object ViewModelUtils {

    inline fun <reified T : ViewModel> getViewModel(activity: Activity): T {
        return ViewModelProviders.of(activity as FragmentActivity).get(T::class.java)
    }

    inline fun <reified T : ViewModel> getViewModel(fragment: Fragment): T {
        return ViewModelProviders.of(fragment).get(T::class.java)
    }
}