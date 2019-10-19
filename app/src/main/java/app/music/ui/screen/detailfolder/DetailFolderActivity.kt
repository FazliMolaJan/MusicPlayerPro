package app.music.ui.screen.detailfolder

import android.content.Intent
import android.view.Menu
import app.music.R
import app.music.base.BaseSingleRecyclerActivity
import app.music.model.entity.BaseMusik
import app.music.model.entity.Folder
import app.music.utils.intent.IntentConstantUtils
import app.music.utils.log.InformationLogUtils

class DetailFolderActivity : BaseSingleRecyclerActivity<Folder>() {

    override fun logServiceConnected() = InformationLogUtils.logServiceConnected(TAG)

    override fun logServiceDisconnected() = InformationLogUtils.logServiceDisconnected(TAG)

    override fun getLogTag(): String = TAG

    override fun getOptionMenuId(): Int = R.menu.activity_detail_folder

    override fun createOptionMenu(menu: Menu) {

    }

    override fun initInject() = activityComponent.inject(this)

    override fun getDataObject(intent: Intent): Folder? {
        return if (!intent.hasExtra(IntentConstantUtils.EXTRA_FOLDER_OBJECT_TO_DETAIL_FOLDER)) {
            null
        } else {
            intent.getParcelableExtra(IntentConstantUtils.EXTRA_FOLDER_OBJECT_TO_DETAIL_FOLDER)
        }
    }

    override fun getToolbarTitle(`object`: Folder?): String? = `object`?.folderName

    override fun getDataList(`object`: Folder?): List<BaseMusik> {
        return `object`?.musicList ?: ArrayList()
    }

    companion object {

        private const val TAG = "DetailFolderActivity"
    }
}
