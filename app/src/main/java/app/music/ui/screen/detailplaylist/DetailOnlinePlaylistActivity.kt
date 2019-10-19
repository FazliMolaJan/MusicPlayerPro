package app.music.ui.screen.detailplaylist

import android.content.Intent
import android.view.Menu
import app.music.R
import app.music.base.BaseSingleRecyclerActivity
import app.music.model.entity.BaseMusik
import app.music.model.entity.OnlinePlaylist
import app.music.utils.intent.IntentConstantUtils
import app.music.utils.log.InformationLogUtils
import java.util.*

class DetailOnlinePlaylistActivity : BaseSingleRecyclerActivity<OnlinePlaylist>() {

    override fun logServiceConnected() = InformationLogUtils.logServiceConnected(TAG)

    override fun logServiceDisconnected() = InformationLogUtils.logServiceDisconnected(TAG)

    override fun getLogTag(): String = TAG

    override fun getOptionMenuId(): Int = R.menu.activity_detail_playlist

    override fun createOptionMenu(menu: Menu) {

    }

    override fun initInject() = activityComponent.inject(this)

    override fun getDataObject(intent: Intent): OnlinePlaylist? {
        return if (!intent.hasExtra(IntentConstantUtils.EXTRA_PLAYLIST_OBJECT_TO_DETAIL_PLAYLIST)) {
            null
        } else {
            intent.getParcelableExtra(IntentConstantUtils.EXTRA_PLAYLIST_OBJECT_TO_DETAIL_PLAYLIST)
        }
    }

    override fun getToolbarTitle(`object`: OnlinePlaylist?): String? = `object`?.playlistName

    override fun getDataList(`object`: OnlinePlaylist?): List<BaseMusik> {
        return `object`?.musicList ?: ArrayList()
    }

    companion object {

        private const val TAG = "DetailOnlinePlaylistActivity"
    }
}
