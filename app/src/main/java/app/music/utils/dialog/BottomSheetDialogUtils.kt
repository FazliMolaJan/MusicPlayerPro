package app.music.utils.dialog

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.util.TypedValue
import android.view.View
import java.lang.ref.WeakReference

object BottomSheetDialogUtils {

    fun setBottomSheetBehaviorPeekHeight(
            contextReference: WeakReference<Context>, view: View, value: Float) {
        with(BottomSheetBehavior.from(view.parent as View)) {
            peekHeight = TypedValue
                    .applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            value,
                            contextReference.get()?.resources?.displayMetrics)
                    .toInt()
        }
    }

    fun dismissDialog(bottomSheetDialog: BottomSheetDialog?) {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss()
        }
    }

    fun showDialog(bottomSheetDialog: BottomSheetDialog?, updateData: () -> Unit) {
        updateData()
        bottomSheetDialog?.show()
    }
}