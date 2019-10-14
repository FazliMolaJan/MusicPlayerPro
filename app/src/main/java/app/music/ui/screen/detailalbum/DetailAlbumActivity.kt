package app.music.ui.screen.detailalbum

import android.content.Intent
import android.view.Menu
import app.music.R
import app.music.base.BaseSingleRecyclerActivity
import app.music.model.Album
import app.music.model.BaseMusik
import app.music.utils.intent.IntentConstantUtils
import app.music.utils.log.InformationLogUtils

class DetailAlbumActivity : BaseSingleRecyclerActivity<Album>() {

    override fun initInject() = activityComponent.inject(this)

    override fun logServiceConnected() = InformationLogUtils.logServiceConnected(TAG)

    override fun logServiceDisconnected() = InformationLogUtils.logServiceDisconnected(TAG)

    override fun getLogTag(): String = TAG

    override fun getOptionMenuId(): Int = R.menu.activity_detail_album

    override fun createOptionMenu(menu: Menu) {

    }

    override fun getDataObject(intent: Intent): Album {
        return intent.getParcelableExtra(IntentConstantUtils.EXTRA_ALBUM_OBJECT_TO_DETAIL_ALBUM)
    }

    override fun getToolbarTitle(`object`: Album?): String? = `object`?.albumName

    override fun getDataList(`object`: Album?): List<BaseMusik> = `object`?.musicList ?: ArrayList()

    companion object {

        private const val TAG = "DetailAlbumActivity"
    }
}
