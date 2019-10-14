package app.music.ui.screen.detailartist


import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import app.music.R
import app.music.adapter.ArtistAlbumAdapter
import app.music.base.BaseFragment
import app.music.comparator.comparatorascending.album.AlbumComparatorByAlphabetAscending
import app.music.databinding.FragmentDetailArtistAlbumBinding
import app.music.listener.itemclick.AlbumFragmentItemClickListener
import app.music.listener.RecyclerScrollToTopListener
import app.music.model.Album
import app.music.model.Artist
import app.music.utils.ConstantUtil
import app.music.utils.musicloading.LoadMusicUtil
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DetailArtistAlbumFragment
    : BaseFragment<FragmentDetailArtistAlbumBinding>(),
        RecyclerScrollToTopListener,
        AlbumFragmentItemClickListener {

    override val logTag: String = TAG
    override val layoutId: Int = R.layout.fragment_detail_artist_album
    private var mArtist: Artist? = null
    private var mRecyclerAdapter: ArtistAlbumAdapter? = null
    private val albumList = ArrayList<Album>()
    private var albumCount = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mArtist = (activity as? DetailArtistActivity)?.mArtist
        (activity as? DetailArtistActivity)?.mDetailArtistAlbumFragment = this
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recycler.adapter = null
    }

    override fun initView() {
        with(binding.recycler) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, ConstantUtil.SPAN_COUNT_THREE)
        }
    }

    override fun initData() {
        mRecyclerAdapter = ArtistAlbumAdapter(
                WeakReference(activity as Activity),
                this::onAlbumClick,
                this::onAlbumLongClick
        )
        binding.recycler.adapter = mRecyclerAdapter
        val albumNameList = mArtist!!.albumNameList
        val music = mArtist!!.musicList[0]
        when (music.type) {
            ConstantUtil.OFFLINE_MUSIC -> {
                //            for (albumName in albumNameList) {
//                for (album in LoadMusicUtil.sAlbumList) {
//                    if (album.albumName == albumName) {
//                        albumList.add(album)
//                        albumCount += 1
//                    }
//                    if (albumCount == albumNameList.size) break
//                }
//            }

                addAlbumData(albumNameList, LoadMusicUtil.sAlbumList)
            }
            else -> {
                addAlbumData(albumNameList, LoadMusicUtil.sOnlineAlbumList)
            }
        }
        Collections.sort(albumList, AlbumComparatorByAlphabetAscending())
        //        albumAdapter.updateItems(albumList);
        mRecyclerAdapter!!.updateItems(false, albumList)
    }

    override fun onScrollToTop() {
        binding.recycler.scrollToPosition(0)
    }

    private fun addAlbumData(albumNameList: List<String>, allAlbumList: List<Album>) {
//        run loop@{
//            albumNameList.forEach { albumName ->
//                allAlbumList.forEach { album ->
//                    if (album.albumName == albumName) {
//                        albumList.add(album)
//                        albumCount += 1
//                    }
//                    if (albumCount == albumNameList.size) return@loop
//                }
////                albumList.addAll(
////                        allAlbumList.filter { album ->
////                            album.albumName == albumName
////                        }
////                )
////                albumCount = albumList.size
//            }
//        }

        run loop@{
            allAlbumList.forEach { album ->
                if (albumNameList.contains(album.albumName)) {
                    albumList.add(album)
                    albumCount += 1
                }
                if (albumCount == albumNameList.size) return@loop
            }
        }
    }

    companion object {

        private const val TAG = "DetailArtistAlbumFragment"
    }
}
