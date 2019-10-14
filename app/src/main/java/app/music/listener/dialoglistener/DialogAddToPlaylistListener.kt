package app.music.listener.dialoglistener

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import app.music.R
import app.music.adapter.DialogOnlinePlaylistAdapter
import app.music.adapter.DialogPlaylistAdapter
import app.music.model.BaseMusik
import app.music.model.OnlinePlaylist
import app.music.model.Playlist
import app.music.utils.ConstantUtil
import app.music.utils.InflaterUtils
import app.music.utils.dialog.BottomSheetDialogUtils
import app.music.utils.playlist.PlaylistMethodUtils
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.toast.ToastUtil
import app.music.viewmodel.BaseHomeActivityViewModel
import app.music.viewmodel.HomeActivityViewModel
import app.music.viewmodel.OnlineHomeActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.ref.WeakReference
import java.util.*

interface DialogAddToPlaylistListener {

    fun initPlaylistDialog(music: BaseMusik, viewModelProvider: ViewModelProvider) {
        val context = this as Context
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_playlist)
        val activityViewModel = viewModelProvider.get(BaseHomeActivityViewModel::class.java)
        with(activityViewModel) {
            if (getPlaylistDialog() == null) {
                setPlaylistDialog(BottomSheetDialog(context, R.style.DialogStyle))
            }
            getPlaylistDialog()!!.setContentView(view)
            BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 300f)
            view.findViewById<Button>(R.id.button_add_playlist).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(getPlaylistDialog())
                showNewPlaylistDialog(music, viewModelProvider)
            }
        }
    }

    fun updatePlaylistDialogData(music: BaseMusik, viewModelProvider: ViewModelProvider) {
        val context = this as Context
        with(viewModelProvider) {
            setPlaylistDialogRecyclerAdapter(music, this)
            when (music.type) {
                ConstantUtil.OFFLINE_MUSIC -> {
                    val dataList = PlaylistMethodUtils.getAllPlaylist(context)
                    val allPlaylists = if (dataList.isNotEmpty()) {
                        ArrayList(dataList)
                    } else {
                        ArrayList()
                    }
                    val activityViewModel = get(HomeActivityViewModel::class.java)
                    activityViewModel.getDialogPlaylistAdapter()!!.updateItems(false, allPlaylists)
                }
                else -> {
                    val dataList = PlaylistMethodUtils.getAllOnlinePlaylist(context)
                    val allPlaylists = if (dataList.isNotEmpty()) {
                        ArrayList(dataList)
                    } else {
                        ArrayList()
                    }
                    val activityViewModel = get(OnlineHomeActivityViewModel::class.java)
                    activityViewModel.getDialogOnlinePlaylistAdapter()!!.updateItems(false, allPlaylists)
                }
            }
        }
    }

    fun setPlaylistDialogRecyclerAdapter(music: BaseMusik, viewModelProvider: ViewModelProvider) {
        val context = this as Context
        with(viewModelProvider) {
            val activityViewModel = get(BaseHomeActivityViewModel::class.java)
            activityViewModel
                    .getPlaylistDialog()!!
                    .findViewById<RecyclerView>(R.id.recycler_playlist)?.apply {
                        RecyclerViewUtils.setVerticalLinearLayout(this, context, true)
                        itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
                        adapter = when (music.type) {
                            ConstantUtil.OFFLINE_MUSIC -> {
                                val homeActivityViewModel = get(HomeActivityViewModel::class.java)
                                with(homeActivityViewModel) {
                                    if (getDialogPlaylistAdapter() == null) {
                                        setDialogPlaylistAdapter(
                                                DialogPlaylistAdapter(
                                                        WeakReference(context as Activity),
                                                        this@DialogAddToPlaylistListener::onDialogPlaylistClick,
                                                        this@DialogAddToPlaylistListener::onDialogPlaylistLongClick))
                                    }
                                    getDialogPlaylistAdapter()
                                }
                            }
                            else -> {
                                val homeActivityViewModel = get(OnlineHomeActivityViewModel::class.java)
                                with(homeActivityViewModel) {
                                    if (getDialogOnlinePlaylistAdapter() == null) {
                                        setDialogOnlinePlaylistAdapter(
                                                DialogOnlinePlaylistAdapter(
                                                        WeakReference(context as Activity),
                                                        this@DialogAddToPlaylistListener::onDialogOnlinePlaylistClick,
                                                        this@DialogAddToPlaylistListener::onDialogOnlinePlaylistLongClick))
                                    }
                                    getDialogOnlinePlaylistAdapter()
                                }
                            }
                        }
                    }
        }
    }

    fun showPlaylistDialog(music: BaseMusik, viewModelProvider: ViewModelProvider) {
        initPlaylistDialog(music, viewModelProvider)
        updatePlaylistDialogData(music, viewModelProvider)
        val activityViewModel = viewModelProvider.get(BaseHomeActivityViewModel::class.java)
        activityViewModel.getPlaylistDialog()!!.show()
    }

    fun initNewPlaylistDialog(music: BaseMusik, viewModelProvider: ViewModelProvider) {
        val activityViewModel = viewModelProvider.get(BaseHomeActivityViewModel::class.java)
        with(activityViewModel) {
            val context = this@DialogAddToPlaylistListener as Context
            if (getNewPlaylistDialog() == null) {
                setNewPlaylistDialog(BottomSheetDialog(context, R.style.DialogEditTextStyle))
            }
            val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_new_playlist)
            getNewPlaylistDialog()!!.setContentView(view)
            BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 300f)
            with(view) {
                setEditNewPlaylistName(findViewById(R.id.edit_playlist_name))
                findViewById<Button>(R.id.button_create).setOnClickListener {
                    val playlistName = getEditNewPlaylistName()!!.text!!.toString()
                    if (!TextUtils.isEmpty(playlistName)) {
                        when (music.type) {
                            ConstantUtil.OFFLINE_MUSIC -> {
                                val homeActivityViewModel = viewModelProvider.get(HomeActivityViewModel::class.java)
                                PlaylistMethodUtils.addSongToNewPlaylist(
                                        WeakReference(context),
                                        homeActivityViewModel.getLastAddToPlaylistObject(),
                                        playlistName)
                            }
                            else -> {
                                val homeActivityViewModel = viewModelProvider.get(OnlineHomeActivityViewModel::class.java)
                                PlaylistMethodUtils.addSongToNewOnlinePlaylist(
                                        WeakReference(context),
                                        homeActivityViewModel.getLastAddToOnlinePlaylistObject(),
                                        playlistName)
                            }
                        }
                        getNewPlaylistDialog()!!.dismiss()
                    } else {
                        ToastUtil.showToast("Input playlist name")
                    }
                }
                findViewById<Button>(R.id.button_cancel).setOnClickListener {
                    BottomSheetDialogUtils.dismissDialog(getNewPlaylistDialog())
                }
            }

        }
    }

    fun updateNewPlaylistDialogData(viewModelProvider: ViewModelProvider) {
        val activityViewModel = viewModelProvider.get(BaseHomeActivityViewModel::class.java)
        with(activityViewModel) {
            if (getEditNewPlaylistName() != null
                    && !TextUtils.isEmpty(getEditNewPlaylistName()!!.text!!.toString())) {
                getEditNewPlaylistName()!!.setText("")
            }
        }
    }

    fun showNewPlaylistDialog(music: BaseMusik, viewModelProvider: ViewModelProvider) {
        initNewPlaylistDialog(music, viewModelProvider)
        updateNewPlaylistDialogData(viewModelProvider)
        val activityViewModel = viewModelProvider.get(BaseHomeActivityViewModel::class.java)
        activityViewModel.getNewPlaylistDialog()!!.show()
    }

    fun onDialogPlaylistClick(playlist: Playlist) {
        val context = this as Context
        val viewModelProvider = ViewModelProviders.of(this as FragmentActivity)
        with(viewModelProvider) {
            val activityViewModel = get(HomeActivityViewModel::class.java)
            PlaylistMethodUtils.addSongToExistedPlaylist(
                    WeakReference(context),
                    activityViewModel.getLastAddToPlaylistObject(),
                    playlist.createdTime)
        }
    }

    fun onDialogPlaylistLongClick(playlist: Playlist) {
        val context = this as Context
        val viewModelProvider = ViewModelProviders.of(this as FragmentActivity)
        with(viewModelProvider) {
            val activityViewModel = get(HomeActivityViewModel::class.java)
            PlaylistMethodUtils.addSongToExistedPlaylist(
                    WeakReference(context),
                    activityViewModel.getLastAddToPlaylistObject(),
                    playlist.createdTime)
        }
    }

    fun onDialogOnlinePlaylistClick(onlinePlaylist: OnlinePlaylist) {
        val context = this as Context
        val viewModelProvider = ViewModelProviders.of(this as FragmentActivity)
        with(viewModelProvider) {
            val activityViewModel = get(OnlineHomeActivityViewModel::class.java)
            PlaylistMethodUtils.addSongToExistedOnlinePlaylist(
                    WeakReference(context),
                    activityViewModel.getLastAddToOnlinePlaylistObject(),
                    onlinePlaylist.createdTime)
        }
    }

    fun onDialogOnlinePlaylistLongClick(onlinePlaylist: OnlinePlaylist) {
        val context = this as Context
        val viewModelProvider = ViewModelProviders.of(this as FragmentActivity)
        with(viewModelProvider) {
            val activityViewModel = get(OnlineHomeActivityViewModel::class.java)
            PlaylistMethodUtils.addSongToExistedOnlinePlaylist(
                    WeakReference(context),
                    activityViewModel.getLastAddToOnlinePlaylistObject(),
                    onlinePlaylist.createdTime)
        }
    }
}
