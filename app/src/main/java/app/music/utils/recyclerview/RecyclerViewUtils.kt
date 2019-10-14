package app.music.utils.recyclerview

import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.listener.ToolbarScrollFlagListener
import java.lang.ref.WeakReference

object RecyclerViewUtils {

    fun setVerticalLinearLayout(recyclerView: RecyclerView?, context: Context) {
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun setVerticalLinearLayout(recyclerView: RecyclerView?, context: Context, hasFixedSize: Boolean) {
        recyclerView?.setHasFixedSize(hasFixedSize)
        setVerticalLinearLayout(recyclerView, context)
    }

    fun setVerticalLinearLayout(recyclerView: RecyclerView, context: Context, hasFixedSize: Boolean,
                                hasDividerItemDecoration: Boolean) {
        setVerticalLinearLayout(recyclerView, context, hasFixedSize)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.custom_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    fun setToolbarScrollFlag(recyclerView: RecyclerView, activityReference: WeakReference<Activity>) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visiblePosition = layoutManager!!.findFirstVisibleItemPosition()
                if (visiblePosition > -1) {
                    with((activityReference.get() as ToolbarScrollFlagListener)) {
                        when (visiblePosition) {
                            0 -> setPinToolbar()
                            else -> setScrollToolBar()
                        }
                    }
                }
            }
        })
    }
}
