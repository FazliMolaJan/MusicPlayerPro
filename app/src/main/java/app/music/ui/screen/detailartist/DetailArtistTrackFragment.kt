package app.music.ui.screen.detailartist


import android.app.Activity
import android.content.Context
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import app.music.R
import app.music.adapter.ArtistSongAdapter
import app.music.base.BaseFragment
import app.music.base.ContainListViewModel
import app.music.comparator.comparatorascending.song.SongComparatorByAlphabetAscending
import app.music.databinding.FragmentDetailArtistTrackBinding
import app.music.listener.RecyclerScrollToTopListener
import app.music.listener.dialoglistener.DialogSongOptionListener
import app.music.model.Artist
import app.music.model.BaseMusik
import app.music.utils.DoubleClickUtils
import app.music.utils.dialog.songoption.DialogSongOptionMethodUtils
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.viewmodel.ViewModelUtils
import java.lang.ref.WeakReference
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailArtistTrackFragment
    : BaseFragment<FragmentDetailArtistTrackBinding>(),
        RecyclerScrollToTopListener {

    private var mArtist: Artist? = null
    private var mRecyclerAdapter: ArtistSongAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = activity
        if (activity != null) {
            mArtist = (activity as DetailArtistActivity).mArtist
            activity.mDetailArtistTrackFragment = this
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recycler.adapter = null
    }

    override fun getLayoutId() = R.layout.fragment_detail_artist_track

    override fun getLogTag() = TAG

    override fun initView() {
        RecyclerViewUtils.setVerticalLinearLayout(binding.recycler, context!!, true)
        binding.recycler.itemAnimator = DefaultItemAnimator()
    }

    override fun initData() {
        mRecyclerAdapter = ArtistSongAdapter(
                WeakReference<Activity>(activity),
                this::onMusicClick,
                this::onMusicLongClick
        )
        binding.recycler.adapter = mRecyclerAdapter
        //        ArtistSongAdapter songAdapter = new ArtistSongAdapter((DetailArtistActivity) getActivity());
        //        mBinding.recycler.setAdapter(songAdapter);
        val musicList = mArtist!!.musicList
        Collections.sort(musicList, SongComparatorByAlphabetAscending())
        //        songAdapter.updateItems(musicList);
        mRecyclerAdapter!!.updateItems(false, musicList)
    }

    override fun onScrollToTop() {
        binding.recycler.scrollToPosition(0)
    }

    private fun onMusicClick(music: BaseMusik) {
        checkDoubleClick {
            with(activity as DetailArtistActivity) {
                mMusicService.setList(mArtist?.musicList)
                playPickedSong(music)
            }
        }
    }

    private fun onMusicLongClick(music: BaseMusik) {
        DialogSongOptionMethodUtils.showSongOption(activity as DialogSongOptionListener, music)
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

        private val TAG = "DetailArtistTrackFragment"
    }
}
