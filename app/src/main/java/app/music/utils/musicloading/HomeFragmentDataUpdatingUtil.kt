package app.music.utils.musicloading

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.adapter.*
import app.music.base.BaseActivity
import app.music.model.*
import app.music.network.APIUtils
import app.music.utils.ListUtils
import app.music.utils.sort.SortMethodUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import kotlin.reflect.KFunction2

object HomeFragmentDataUpdatingUtil {

//    fun updateSongList(
//            activity: Activity, compositeDisposable: CompositeDisposable, songAdapter: SongAdapter<Music>,
//            refreshLayout: SwipeRefreshLayout, comparator: Comparator<BaseMusik>, isReloadTypeList: Boolean) {
//        updateHomeList(activity, compositeDisposable, refreshLayout, comparator,
//                null, null, songAdapter::updateItems, isReloadTypeList,
//                false, getNewDataList = { LoadMusicUtil.sMusicList })
//    }
//
//    fun updateOnlineSongList(
//            activity: Activity, compositeDisposable: CompositeDisposable, songAdapter: SongAdapter<OnlineMusik>,
//            refreshLayout: SwipeRefreshLayout, comparator: Comparator<BaseMusik>, retrofit: Retrofit?,
//            isReloadTypeList: Boolean) {
//        updateHomeList(activity, compositeDisposable, refreshLayout, comparator,
//                null, retrofit, songAdapter::updateItems, isReloadTypeList,
//                false, getNewDataList = { LoadMusicUtil.sOnlineMusicList })
//    }

    fun updateAlbumList(
            activity: Activity, compositeDisposable: CompositeDisposable, albumAdapter: AlbumAdapter,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<Album>, isReloadTypeList: Boolean) {
        updateHomeList(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getAlbum else null,
                null, albumAdapter::updateItems, false,
                getNewDataList = { LoadMusicUtil.sAlbumList })
    }

    fun updateOnlineAlbumList(
            activity: Activity, compositeDisposable: CompositeDisposable, albumAdapter: AlbumAdapter,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<Album>, retrofit: Retrofit?,
            isReloadTypeList: Boolean) {
        updateHomeList(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getOnlineAlbum else null,
                retrofit, albumAdapter::updateItems, true,
                getNewDataList = { LoadMusicUtil.sOnlineAlbumList })
    }

    fun updateArtistList(
            activity: Activity, compositeDisposable: CompositeDisposable, artistAdapter: ArtistAdapter,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<Artist>, isReloadTypeList: Boolean) {
        updateHomeListWithActivity(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getArtistWithContext else null, null,
                artistAdapter::updateItems, false,
                getNewDataList = { LoadMusicUtil.sArtistList })
    }

    fun updateOnlineArtistList(
            activity: Activity, compositeDisposable: CompositeDisposable, artistAdapter: ArtistAdapter,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<Artist>, retrofit: Retrofit?,
            isReloadTypeList: Boolean) {
        updateHomeList(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getOnlineArtist else null, retrofit,
                artistAdapter::updateItems, true,
                getNewDataList = { LoadMusicUtil.sOnlineArtistList })
    }

    fun updateGenreList(
            activity: Activity, compositeDisposable: CompositeDisposable, genreAdapter: GenreAdapter,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<Genre>,
            isReloadTypeList: Boolean) {
        updateHomeList(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getGenre else null,
                null, genreAdapter::updateItems, false,
                getNewDataList = { LoadMusicUtil.sGenreList })
    }

    fun updateOnlineGenreList(
            activity: Activity, compositeDisposable: CompositeDisposable, genreAdapter: GenreAdapter,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<Genre>, retrofit: Retrofit?,
            isReloadTypeList: Boolean) {
        updateHomeList(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getOnlineGenre else null,
                retrofit, genreAdapter::updateItems, true,
                getNewDataList = { LoadMusicUtil.sOnlineGenreList })
    }

    fun updatePlaylistList(
            activity: Activity, compositeDisposable: CompositeDisposable,
            playlistAdapter: PlaylistAdapter, refreshLayout: SwipeRefreshLayout,
            comparator: Comparator<Playlist>, isReloadTypeList: Boolean) {
        updateHomeListWithActivity(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getPlaylist else null,
                null, playlistAdapter::updateItems, false,
                getNewDataList = { LoadMusicUtil.sPlaylistList })
    }

    fun updateOnlinePlaylistList(
            activity: Activity, compositeDisposable: CompositeDisposable,
            playlistAdapter: OnlinePlaylistAdapter, refreshLayout: SwipeRefreshLayout,
            comparator: Comparator<OnlinePlaylist>, retrofit: Retrofit?, isReloadTypeList: Boolean) {
        updateHomeListWithActivity(activity, compositeDisposable, refreshLayout, comparator,
                if (isReloadTypeList) LoadMusicUtil::getOnlinePlaylist else null,
                retrofit, playlistAdapter::updateItems, false,
                getNewDataList = { LoadMusicUtil.sOnlinePlaylistList })
    }

    private fun updateSongList(retrofit: Retrofit?, activity: Activity, isReloadOnlineMusic: Boolean) {
        if (isReloadOnlineMusic) {
            updateOnlineSongList(retrofit)
        } else {
            LoadMusicUtil.getMusic(activity)
        }
    }

    private fun updateOnlineSongList(retrofit: Retrofit?) {
        ListUtils.clearList(LoadMusicUtil.sOnlineMusicList)
        val callOnlineMusic = if (retrofit == null) {
            APIUtils.getAllMusicCall()
        } else {
            APIUtils.getAllMusicCall(retrofit)
        }
        var responseList: Response<List<OnlineMusik>>? = null
        try {
            responseList = callOnlineMusic.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (responseList?.body() != null) {
            val tempList = ArrayList(responseList.body()!!)
            if (tempList.size > 0) {
                for (music in tempList) {
                    LoadMusicUtil.sOnlineMusicList.add(music)
                }
            }
        }
    }

    private fun <T> updateHomeList(
            activity: Activity, compositeDisposable: CompositeDisposable,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<T>,
            reloadTypeList: (() -> Unit)? = null, retrofit: Retrofit?,
            updateItems: KFunction2<Boolean, MutableList<T>, Unit>,
            isReloadOnlineMusic: Boolean, getNewDataList: () -> MutableList<T>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    reloadTypeList?.run {
                        updateSongList(retrofit, activity, isReloadOnlineMusic)
                        invoke()
                    }
                    Collections.sort(getNewDataList(), comparator)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.i("updateHomeList", "onNext")
                            if (!activity.isFinishing) {
                                updateItems(false, getNewDataList())
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = {
                            it.printStackTrace()
                            Log.i("updateHomeList", "onError")
                        },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                            Log.i("updateHomeList", "onComplete")
                        }
                )
        compositeDisposable.add(musicUpdateDisposable!!)
    }

    private fun <T> updateHomeListWithActivity(
            activity: Activity, compositeDisposable: CompositeDisposable,
            refreshLayout: SwipeRefreshLayout, comparator: Comparator<T>,
            reloadTypeList: ((WeakReference<Context>) -> Unit)? = null, retrofit: Retrofit?,
            updateItems: KFunction2<Boolean, MutableList<T>, Unit>,
            isReloadOnlineMusic: Boolean, getNewDataList: () -> MutableList<T>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    reloadTypeList?.let {
                        updateSongList(retrofit, activity, isReloadOnlineMusic)
                        reloadTypeList(WeakReference(activity))
                    }
                    Collections.sort(getNewDataList(), comparator)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (!activity.isFinishing) {
                                updateItems(false, getNewDataList())
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                        }
                )
        compositeDisposable.add(musicUpdateDisposable!!)
    }

    ///////////////////////////////////

    fun getNewSongList(
            activity: Activity, refreshLayout: SwipeRefreshLayout,
            updateItems: KFunction2<Boolean, MutableList<Music>, Unit>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    LoadMusicUtil.getMusic(activity)
                    SortMethodUtils.sortSongList(activity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (!activity.isFinishing) {
                                updateItems(false, LoadMusicUtil.sMusicList)
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                        }
                )
        (activity as BaseActivity<*>).mCompositeDisposable.add(musicUpdateDisposable!!)
    }

    fun getNewAlbumList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                        updateItems: KFunction2<Boolean, MutableList<Album>, Unit>) {
        getNewOfflineTypeList(
                activity, refreshLayout, LoadMusicUtil::getAlbum, SortMethodUtils::sortAlbumList,
                { LoadMusicUtil.sAlbumList }, updateItems)
    }

    fun getNewGenreList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                        updateItems: KFunction2<Boolean, MutableList<Genre>, Unit>) {
        getNewOfflineTypeList(
                activity, refreshLayout, LoadMusicUtil::getGenre, SortMethodUtils::sortGenreList,
                { LoadMusicUtil.sGenreList }, updateItems)
    }

    fun getNewArtistList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                         updateItems: KFunction2<Boolean, MutableList<Artist>, Unit>) {
        getNewOfflineTypeList(
                activity, refreshLayout, LoadMusicUtil::getArtistWithContext,
                SortMethodUtils::sortArtistList, { LoadMusicUtil.sArtistList }, updateItems)
    }

    fun getNewPlaylistList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                           updateItems: KFunction2<Boolean, MutableList<Playlist>, Unit>) {
        getNewOfflineTypeList(
                activity, refreshLayout, LoadMusicUtil::getPlaylist,
                SortMethodUtils::sortPlaylistList, { LoadMusicUtil.sPlaylistList }, updateItems)
    }

    fun getNewFolderList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                         updateItems: KFunction2<Boolean, MutableList<Folder>, Unit>) {
        getNewOfflineTypeList(
                activity, refreshLayout, LoadMusicUtil::getFolder,
                SortMethodUtils::sortFolderList, { LoadMusicUtil.sFolderList }, updateItems)
    }

    private fun <T> getNewOfflineTypeList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                                          getTypeList: () -> Unit, sortTypeList: (Activity) -> Unit,
                                          getSourceList: () -> MutableList<T>,
                                          updateItems: KFunction2<Boolean, MutableList<T>, Unit>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    LoadMusicUtil.getMusic(activity)
                    getTypeList()
                    sortTypeList(activity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.i("updateHomeList", "onNext")
                            if (!activity.isFinishing) {
                                updateItems(false, getSourceList())
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = {
                            it.printStackTrace()
                            Log.i("updateHomeList", "onError")
                        },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                            Log.i("updateHomeList", "onComplete")
                        }
                )
        (activity as BaseActivity<*>).mCompositeDisposable.add(musicUpdateDisposable!!)
    }

    private fun <T> getNewOfflineTypeList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                                          getTypeList: (WeakReference<Context>) -> Unit,
                                          sortTypeList: (Activity) -> Unit,
                                          getSourceList: () -> MutableList<T>,
                                          updateItems: KFunction2<Boolean, MutableList<T>, Unit>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    LoadMusicUtil.getMusic(activity)
                    getTypeList(WeakReference(activity))
                    sortTypeList(activity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.i("updateHomeList", "onNext")
                            if (!activity.isFinishing) {
                                updateItems(false, getSourceList())
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = {
                            it.printStackTrace()
                            Log.i("updateHomeList", "onError")
                        },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                            Log.i("updateHomeList", "onComplete")
                        }
                )
        (activity as BaseActivity<*>).mCompositeDisposable.add(musicUpdateDisposable!!)
    }

    fun getNewOnlineSongList(
            activity: Activity, refreshLayout: SwipeRefreshLayout,
            updateItems: KFunction2<Boolean, MutableList<Music>, Unit>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    updateOnlineSongList(null)
                    SortMethodUtils.sortOnlineSongList(activity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (!activity.isFinishing) {
                                updateItems(false, LoadMusicUtil.sMusicList)
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                        }
                )
        (activity as BaseActivity<*>).mCompositeDisposable.add(musicUpdateDisposable!!)
    }

    fun getNewOnlineAlbumList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                              updateItems: KFunction2<Boolean, MutableList<Album>, Unit>) {

        getNewOnlineTypeList(activity, refreshLayout, LoadMusicUtil::getOnlineAlbum,
                SortMethodUtils::sortOnlineAlbumList, { LoadMusicUtil.sOnlineAlbumList },
                updateItems)
    }

    fun getNewOnlineArtistList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                               updateItems: KFunction2<Boolean, MutableList<Artist>, Unit>) {

        getNewOnlineTypeList(activity, refreshLayout, LoadMusicUtil::getOnlineArtist,
                SortMethodUtils::sortOnlineArtistList, { LoadMusicUtil.sOnlineArtistList },
                updateItems)
    }

    fun getNewOnlineGenreList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                              updateItems: KFunction2<Boolean, MutableList<Genre>, Unit>) {

        getNewOnlineTypeList(activity, refreshLayout, LoadMusicUtil::getOnlineGenre,
                SortMethodUtils::sortOnlineGenreList, { LoadMusicUtil.sOnlineGenreList },
                updateItems)
    }

    fun getNewOnlinePlaylistList(activity: Activity, refreshLayout: SwipeRefreshLayout,
                                 updateItems: KFunction2<Boolean, MutableList<OnlinePlaylist>, Unit>) {

        getNewOnlineTypeList(activity, refreshLayout, LoadMusicUtil::getOnlinePlaylist,
                SortMethodUtils::sortOnlinePlaylistList, { LoadMusicUtil.sOnlinePlaylistList },
                updateItems)
    }

    private fun <T> getNewOnlineTypeList(
            activity: Activity, refreshLayout: SwipeRefreshLayout,
            getTypeList: () -> Unit,
            sortTypeList: (Activity) -> Unit,
            getSourceList: () -> MutableList<T>,
            updateItems: KFunction2<Boolean, MutableList<T>, Unit>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    updateOnlineSongList(null)
                    getTypeList()
                    sortTypeList(activity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (!activity.isFinishing) {
                                updateItems(false, getSourceList())
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                        }
                )
        (activity as BaseActivity<*>).mCompositeDisposable.add(musicUpdateDisposable!!)
    }

    private fun <T> getNewOnlineTypeList(
            activity: Activity, refreshLayout: SwipeRefreshLayout,
            getTypeList: (WeakReference<Context>) -> Unit,
            sortTypeList: (Activity) -> Unit,
            getSourceList: () -> MutableList<T>,
            updateItems: KFunction2<Boolean, MutableList<T>, Unit>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    updateOnlineSongList(null)
                    getTypeList(WeakReference(activity))
                    sortTypeList(activity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (!activity.isFinishing) {
                                updateItems(false, getSourceList())
                                refreshLayout.isRefreshing = false
                            }
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {
                            musicUpdateDisposable?.dispose()
                            musicUpdateDisposable = null
                        }
                )
        (activity as BaseActivity<*>).mCompositeDisposable.add(musicUpdateDisposable!!)
    }
}
