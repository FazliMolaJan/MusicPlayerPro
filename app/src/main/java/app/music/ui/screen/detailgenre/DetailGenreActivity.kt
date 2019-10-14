package app.music.ui.screen.detailgenre

import android.content.Intent
import android.view.Menu
import app.music.R
import app.music.base.BaseSingleRecyclerActivity
import app.music.model.BaseMusik
import app.music.model.Genre
import app.music.utils.intent.IntentConstantUtils
import app.music.utils.log.InformationLogUtils

class DetailGenreActivity : BaseSingleRecyclerActivity<Genre>() {

    override fun logServiceConnected() = InformationLogUtils.logServiceConnected(TAG)

    override fun logServiceDisconnected() = InformationLogUtils.logServiceDisconnected(TAG)

    override fun getLogTag(): String = TAG

    override fun getOptionMenuId(): Int = R.menu.activity_detail_genre

    override fun createOptionMenu(menu: Menu) {

    }

    override fun initInject() = activityComponent.inject(this)

    override fun getDataObject(intent: Intent): Genre? {
        return if (!intent.hasExtra(IntentConstantUtils.EXTRA_GENRE_OBJECT_TO_DETAIL_GENRE)) {
            null
        } else {
            intent.getParcelableExtra(IntentConstantUtils.EXTRA_GENRE_OBJECT_TO_DETAIL_GENRE)
        }
    }

    override fun getToolbarTitle(`object`: Genre?): String? = `object`?.genre

    override fun getDataList(`object`: Genre?): List<BaseMusik> {
        return `object`?.musicList ?: ArrayList()
    }

    companion object {

        private const val TAG = "DetailGenreActivity"
    }
}
