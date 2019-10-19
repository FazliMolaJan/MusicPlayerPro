package app.music.ui.screen.detailplaymv

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.MediaController
import app.music.R
import app.music.model.entity.BaseMusik
import app.music.model.entity.OnlineMusik
import app.music.utils.intent.IntentConstantUtils
import kotlinx.android.synthetic.main.activity_detail_play_mv.*

class DetailPlayMvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_play_mv)

        setSupportActionBar(toolbar_title)
        supportActionBar?.let {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        intent?.let {
            val music = intent.getParcelableExtra<BaseMusik>(IntentConstantUtils.EXTRA_MUSIC_OBJECT_TO_DETAIL_PLAY_MV)
            music?.let {
                val mvLink = (music as OnlineMusik).mvLink
                if (!TextUtils.isEmpty(mvLink)) {
                    val uri = Uri.parse(mvLink)
                    video_mv?.run {
                        setVideoURI(uri)
                        setMediaController(MediaController(this@DetailPlayMvActivity))
                        start()
                        visibility = View.VISIBLE
                    }
                    progress_load!!.visibility = View.GONE
                }
                val mvTitle = music.title
                if (!TextUtils.isEmpty(mvTitle)) {
                    text_song_title!!.text = mvTitle
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
