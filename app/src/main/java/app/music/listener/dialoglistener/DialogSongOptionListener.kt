package app.music.listener.dialoglistener

import android.content.Context
import android.media.MediaMetadataRetriever
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.fragment.app.FragmentActivity
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import app.music.R
import app.music.model.entity.BaseMusik
import app.music.model.entity.Music
import app.music.model.entity.OnlineMusik
import app.music.utils.ConstantUtil
import app.music.utils.InflaterUtils
import app.music.utils.dialog.BottomSheetDialogUtils
import app.music.utils.imageloading.ImageLoadingUtils
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.toast.ToastUtil
import app.music.viewmodel.HomeActivityViewModel
import app.music.viewmodel.OnlineHomeActivityViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.ref.WeakReference

interface DialogSongOptionListener {

    var mSongOptionDialog: BottomSheetDialog?

    fun initSongOptionDialog(song: BaseMusik) {
        val viewModelProvider = ViewModelProviders.of(this as FragmentActivity)
        with(song) {
            when (type) {
                ConstantUtil.OFFLINE_MUSIC -> {
                    val homeActivityViewModel = viewModelProvider.get(HomeActivityViewModel::class.java)
                    homeActivityViewModel.setLastAddToPlaylistObject(Music(
                            title,
                            artist,
                            year,
                            album,
                            genre,
                            location,
                            duration,
                            ConstantUtil.OFFLINE_MUSIC,
                            lyrics,
                            dateModified,
                            (song as Music).fileName,
                            (song as Music).trackNumber))
                }
                else -> {
                    val homeActivityViewModel = viewModelProvider.get(OnlineHomeActivityViewModel::class.java)
                    homeActivityViewModel.setLastAddToOnlinePlaylistObject(OnlineMusik(
                            title,
                            artist,
                            year,
                            album,
                            genre,
                            location,
                            duration,
                            ConstantUtil.ONLINE_MUSIC,
                            lyrics,
                            dateModified,
                            (song as OnlineMusik).musicId,
                            (song as OnlineMusik).coverArt,
                            (song as OnlineMusik).mvLink))
                }
            }
        }
        val context = this as Context
        if (mSongOptionDialog == null) {
            mSongOptionDialog = BottomSheetDialog(context, R.style.DialogStyle)
        }
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_option_song)
        mSongOptionDialog!!.setContentView(view)
        BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 600f)

        with(view) {
            findViewById<TextView>(R.id.textAddToPlaylist).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mSongOptionDialog)
                (this@DialogSongOptionListener as DialogAddToPlaylistListener)
                        .showPlaylistDialog(song, viewModelProvider)
            }
            findViewById<TextView>(R.id.textAddToQueue).setOnClickListener {
                LoadMusicUtil.sQueueList.add(song)
            }
            findViewById<TextView>(R.id.textSongInfo).setOnClickListener {
                ToastUtil.showToast("song info" + song.title)
            }
            findViewById<Button>(R.id.buttonCancel).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mSongOptionDialog)
            }
        }
    }

    fun updateSongOptionDialogData(music: BaseMusik) {
        mSongOptionDialog?.run {
            findViewById<CircleImageView>(R.id.imageCoverArt)?.let {
                ImageLoadingUtils.loadMusicImage(
                        music,
                        MediaMetadataRetriever(),
                        ImageLoadingUtils.getCoverArtRequestOption(),
                        it)
            }
            findViewById<TextView>(R.id.textSongTitle)!!.text = music.title
            findViewById<TextView>(R.id.textArtistName)!!.text = music.artist
        }
    }

    fun showSongOptionDialog(music: BaseMusik) {
        initSongOptionDialog(music)
        updateSongOptionDialogData(music)
        mSongOptionDialog!!.show()
    }
}