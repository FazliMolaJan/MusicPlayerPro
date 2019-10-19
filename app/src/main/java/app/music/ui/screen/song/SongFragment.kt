package app.music.ui.screen.song

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.recycler.SongAdapter
import app.music.base.ContainListViewModel
import app.music.databinding.FragmentSongBinding
import app.music.listener.dialoglistener.DialogSongOptionListener
import app.music.listener.homefragmentlistener.SongFragmentListener
import app.music.model.entity.BaseMusik
import app.music.ui.screen.home.BaseHomeFragment
import app.music.ui.screen.home.HomeActivity
import app.music.utils.DoubleClickUtils
import app.music.utils.dialog.songoption.DialogSongOptionMethodUtils
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils
import app.music.viewholder.MusicViewHolder
import app.music.viewmodel.HomeActivityViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class SongFragment
    : BaseHomeFragment<BaseMusik,
        HomeActivityViewModel,
        FragmentSongBinding,
        MusicViewHolder,
        SongAdapter>(),
        SongFragmentListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).mSongFragmentListener = this
    }

    override fun getRecyclerAdapter(): SongAdapter {
        return SongAdapter(
                WeakReference<Activity>(activity),
                this::onMusicClick,
                this::onMusicLongClick
        )
    }

    override fun getDataList(): List<BaseMusik> = LoadMusicUtil.sMusicList

    override fun getRecyclerView(): RecyclerView = binding.recyclerview

    override fun getRefreshLayout(): SwipeRefreshLayout = binding.refreshlayout

    override fun getLayoutId() = R.layout.fragment_song

    override fun getLogTag() = TAG

    override fun initInject() {
        fragmentComponent?.inject(this)
    }

    override fun initView() {
        super.initView()
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true)
        binding.recyclerview.itemAnimator = DefaultItemAnimator()
        //        String folder = new File(new File(LoadMusicUtil.sMusicList.get(0).getLocation()).getParent()).getName();
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewSongList(
                activity as Activity,
                binding.refreshlayout,
                mRecyclerAdapter::updateItems
        )
    }

    override fun onSortMusic(sortBy: String, isAscending: String) {
        SortMethodUtils.sortSongList(
                activity as Activity,
                sortBy,
                isAscending,
                mRecyclerAdapter::updateItems
        )
    }

    private fun onMusicClick(music: BaseMusik) {
        checkDoubleClick {
            with(activity as HomeActivity) {
                mMusicService.setList(LoadMusicUtil.sMusicList)
                playPickedSong(music)
            }
        }
    }

    private fun onMusicLongClick(music: BaseMusik) {
        DialogSongOptionMethodUtils.showSongOption(activity as DialogSongOptionListener, music)
        ToastUtil.showToast(music.title)
    }

    private fun checkDoubleClick(listener: () -> Unit) {
        val mHomeActivityViewModel = ViewModelUtils.getViewModel<ContainListViewModel>(activity as Activity)
        with(mHomeActivityViewModel) {
            if (!DoubleClickUtils.isDoubleClick(getItemLastClickTime())) {
                setItemLastClickTime(SystemClock.elapsedRealtime())
                listener()
            } else {
                setItemLastClickTime(SystemClock.elapsedRealtime())
            }
        }
    }

    companion object {

        private const val TAG = "SongFragment"
    }
}
