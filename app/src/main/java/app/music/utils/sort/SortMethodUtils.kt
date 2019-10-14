package app.music.utils.sort

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import app.music.base.BaseActivity
import app.music.comparator.comparatorascending.album.AlbumComparatorByAlphabetAscending
import app.music.comparator.comparatorascending.album.AlbumComparatorByNumberOfSongsAscending
import app.music.comparator.comparatorascending.artist.ArtistComparatorByAlphabetAscending
import app.music.comparator.comparatorascending.artist.ArtistComparatorByNumberOfAlbumsAscending
import app.music.comparator.comparatorascending.artist.ArtistComparatorByNumberOfSongsAscending
import app.music.comparator.comparatorascending.folder.FolderComparatorByAlphabetAscending
import app.music.comparator.comparatorascending.folder.FolderComparatorByNumberOfSongsAscending
import app.music.comparator.comparatorascending.genre.GenreComparatorByAlphabetAscending
import app.music.comparator.comparatorascending.genre.GenreComparatorByNumberOfSongsAscending
import app.music.comparator.comparatorascending.playlist.offlineplaylist.PlaylistComparatorByAlphabetAscending
import app.music.comparator.comparatorascending.playlist.offlineplaylist.PlaylistComparatorByCreatedTimeAscending
import app.music.comparator.comparatorascending.playlist.offlineplaylist.PlaylistComparatorByNumberOfSongsAscending
import app.music.comparator.comparatorascending.playlist.onlineplaylist.OnlinePlaylistComparatorByAlphabetAscending
import app.music.comparator.comparatorascending.playlist.onlineplaylist.OnlinePlaylistComparatorByCreatedTimeAscending
import app.music.comparator.comparatorascending.playlist.onlineplaylist.OnlinePlaylistComparatorByNumberOfSongsAscending
import app.music.comparator.comparatorascending.song.*
import app.music.comparator.comparatordescending.album.AlbumComparatorByAlphabetDescending
import app.music.comparator.comparatordescending.album.AlbumComparatorByNumberOfSongsDescending
import app.music.comparator.comparatordescending.artist.ArtistComparatorByAlphabetDescending
import app.music.comparator.comparatordescending.artist.ArtistComparatorByNumberOfAlbumsDescending
import app.music.comparator.comparatordescending.artist.ArtistComparatorByNumberOfSongsDescending
import app.music.comparator.comparatordescending.folder.FolderComparatorByAlphabetDescending
import app.music.comparator.comparatordescending.folder.FolderComparatorByNumberOfSongsDescending
import app.music.comparator.comparatordescending.genre.GenreComparatorByAlphabetDescending
import app.music.comparator.comparatordescending.genre.GenreComparatorByNumberOfSongsDescending
import app.music.comparator.comparatordescending.playlist.offlineplaylist.PlaylistComparatorByAlphabetDescending
import app.music.comparator.comparatordescending.playlist.offlineplaylist.PlaylistComparatorByCreatedTimeDescending
import app.music.comparator.comparatordescending.playlist.offlineplaylist.PlaylistComparatorByNumberOfSongsDescending
import app.music.comparator.comparatordescending.playlist.onlineplaylist.OnlinePlaylistComparatorByAlphabetDescending
import app.music.comparator.comparatordescending.playlist.onlineplaylist.OnlinePlaylistComparatorByCreatedTimeDescending
import app.music.comparator.comparatordescending.playlist.onlineplaylist.OnlinePlaylistComparatorByNumberOfSongsDescending
import app.music.comparator.comparatordescending.song.*
import app.music.model.*
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.sharepreferences.SharedPrefMethodUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.*
import kotlin.reflect.KFunction2

object SortMethodUtils {

    private var sEditor: SharedPreferences.Editor? = null

    private fun saveSortState(contextReference: WeakReference<Context>,
                              sortKey: String, sortValue: String,
                              orderKey: String, orderValue: String) {
        if (null == sEditor) {
            sEditor = SharedPrefMethodUtils.getSharedPreferences(contextReference).edit()
        }
        sEditor?.run {
            putString(sortKey, sortValue)
            putString(orderKey, orderValue)
            apply()
        }
    }

    fun saveAlbumSortState(
            contextReference: WeakReference<Context>, sortValue: String, orderValue: String) {
        saveSortState(
                contextReference,
                SortConstantUtils.PREF_ALBUM_SORT,
                sortValue,
                SortConstantUtils.PREF_ALBUM_ORDER_BY,
                orderValue)
    }

    fun saveSongSortState(
            contextReference: WeakReference<Context>, sortValue: String, orderValue: String) {
        saveSortState(
                contextReference,
                SortConstantUtils.PREF_SONG_SORT,
                sortValue,
                SortConstantUtils.PREF_SONG_ORDER_BY,
                orderValue)
    }

    fun saveArtistSortState(
            contextReference: WeakReference<Context>, sortValue: String, orderValue: String) {
        saveSortState(
                contextReference,
                SortConstantUtils.PREF_ARTIST_SORT,
                sortValue,
                SortConstantUtils.PREF_ARTIST_ORDER_BY,
                orderValue)
    }

    fun saveGenreSortState(
            contextReference: WeakReference<Context>, sortValue: String, orderValue: String) {
        saveSortState(
                contextReference,
                SortConstantUtils.PREF_GENRE_SORT,
                sortValue,
                SortConstantUtils.PREF_GENRE_ORDER_BY,
                orderValue)
    }

    fun savePlaylistSortState(
            contextReference: WeakReference<Context>, sortValue: String, orderValue: String) {
        saveSortState(
                contextReference,
                SortConstantUtils.PREF_PLAYLIST_SORT,
                sortValue,
                SortConstantUtils.PREF_PLAYLIST_ORDER_BY,
                orderValue)
    }

    fun getAlbumSortState(contextReference: WeakReference<Context>): Array<String> {
        with(SharedPrefMethodUtils.getSharedPreferences(contextReference)) {
            val sortState = getString(
                    SortConstantUtils.PREF_ALBUM_SORT, SortConstantUtils.PREF_ALBUM_SORT_BY_ALPHABET).toString()
            val orderState = getString(
                    SortConstantUtils.PREF_ALBUM_ORDER_BY, SortConstantUtils.PREF_ORDER_BY_ASCENDING).toString()
            return arrayOf(sortState, orderState)
        }
    }

    fun getSongSortState(contextReference: WeakReference<Context>): Array<String> {
        with(SharedPrefMethodUtils.getSharedPreferences(contextReference)) {
            val sortState = getString(
                    SortConstantUtils.PREF_SONG_SORT, SortConstantUtils.PREF_SONG_SORT_BY_ALPHABET).toString()
            val orderState = getString(
                    SortConstantUtils.PREF_SONG_ORDER_BY, SortConstantUtils.PREF_ORDER_BY_ASCENDING).toString()
            return arrayOf(sortState, orderState)
        }
    }

    fun getArtistSortState(contextReference: WeakReference<Context>): Array<String> {
        with(SharedPrefMethodUtils.getSharedPreferences(contextReference)) {
            val sortState = getString(
                    SortConstantUtils.PREF_ARTIST_SORT, SortConstantUtils.PREF_ARTIST_SORT_BY_ALPHABET).toString()
            val orderState = getString(
                    SortConstantUtils.PREF_ARTIST_ORDER_BY, SortConstantUtils.PREF_ORDER_BY_ASCENDING).toString()
            return arrayOf(sortState, orderState)
        }
    }

    fun getGenreSortState(contextReference: WeakReference<Context>): Array<String> {
        with(SharedPrefMethodUtils.getSharedPreferences(contextReference)) {
            val sortState = getString(
                    SortConstantUtils.PREF_GENRE_SORT, SortConstantUtils.PREF_GENRE_SORT_BY_ALPHABET).toString()
            val orderState = getString(
                    SortConstantUtils.PREF_GENRE_ORDER_BY, SortConstantUtils.PREF_ORDER_BY_ASCENDING).toString()
            return arrayOf(sortState, orderState)
        }
    }

    fun getFolderSortState(contextReference: WeakReference<Context>): Array<String> {
        with(SharedPrefMethodUtils.getSharedPreferences(contextReference)) {
            val sortState = getString(
                    SortConstantUtils.PREF_FOLDER_SORT, SortConstantUtils.PREF_FOLDER_SORT_BY_ALPHABET).toString()
            val orderState = getString(
                    SortConstantUtils.PREF_FOLDER_ORDER_BY, SortConstantUtils.PREF_ORDER_BY_ASCENDING).toString()
            return arrayOf(sortState, orderState)
        }
    }

    fun getPlaylistSortState(contextReference: WeakReference<Context>): Array<String> {
        with(SharedPrefMethodUtils.getSharedPreferences(contextReference)) {
            val sortState = getString(
                    SortConstantUtils.PREF_PLAYLIST_SORT, SortConstantUtils.PREF_PLAYLIST_SORT_BY_ALPHABET).toString()
            val orderState = getString(
                    SortConstantUtils.PREF_PLAYLIST_ORDER_BY, SortConstantUtils.PREF_ORDER_BY_ASCENDING).toString()
            return arrayOf(sortState, orderState)
        }
    }

    // sort music

    fun sortAlbumList(activity: Activity, sortBy: String, orderBy: String,
                      updateItems: KFunction2<Boolean, MutableList<Album>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortAlbumList, { LoadMusicUtil.sAlbumList },
                updateItems)
    }

    fun sortOnlineAlbumList(activity: Activity, sortBy: String, orderBy: String,
                            updateItems: KFunction2<Boolean, MutableList<Album>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortAlbumList,
                { LoadMusicUtil.sOnlineAlbumList }, updateItems)
    }

    fun sortSongList(activity: Activity, sortBy: String, orderBy: String,
                     updateItems: KFunction2<Boolean, MutableList<Music>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortSongList, { LoadMusicUtil.sMusicList },
                updateItems)
    }

    fun sortOnlineSongList(activity: Activity, sortBy: String, orderBy: String,
                           updateItems: KFunction2<Boolean, MutableList<OnlineMusik>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortSongList,
                { LoadMusicUtil.sOnlineMusicList }, updateItems)
    }

    fun sortGenreList(activity: Activity, sortBy: String, orderBy: String,
                      updateItems: KFunction2<Boolean, MutableList<Genre>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortGenreList, { LoadMusicUtil.sGenreList },
                updateItems)
    }

    fun sortOnlineGenreList(activity: Activity, sortBy: String, orderBy: String,
                      updateItems: KFunction2<Boolean, MutableList<Genre>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortGenreList,
                { LoadMusicUtil.sOnlineGenreList }, updateItems)
    }

    fun sortArtistList(activity: Activity, sortBy: String, orderBy: String,
                       updateItems: KFunction2<Boolean, MutableList<Artist>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortArtistList, { LoadMusicUtil.sArtistList },
                updateItems)
    }

    fun sortOnlineArtistList(activity: Activity, sortBy: String, orderBy: String,
                       updateItems: KFunction2<Boolean, MutableList<Artist>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortArtistList,
                { LoadMusicUtil.sOnlineArtistList }, updateItems)
    }

    fun sortPlaylistList(activity: Activity, sortBy: String, orderBy: String,
                         updateItems: KFunction2<Boolean, MutableList<Playlist>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortPlaylistList,
                { LoadMusicUtil.sPlaylistList }, updateItems)
    }

    fun sortOnlinePlaylistList(activity: Activity, sortBy: String, orderBy: String,
                               updateItems: KFunction2<Boolean, MutableList<OnlinePlaylist>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortOnlinePlaylistList,
                { LoadMusicUtil.sOnlinePlaylistList }, updateItems)
    }

    fun sortFolderList(activity: Activity, sortBy: String, orderBy: String,
                       updateItems: KFunction2<Boolean, MutableList<Folder>, Unit>) {
        sortMusicList(activity, sortBy, orderBy, this::sortFolderList, { LoadMusicUtil.sFolderList },
                updateItems)
    }

    private fun <T> sortMusicList(activity: Activity, sortBy: String, orderBy: String,
                                  sortList: (() -> MutableList<T>, String, String) -> Unit,
                                  getSourceList: () -> MutableList<T>,
                                  updateItems: KFunction2<Boolean, MutableList<T>, Unit>) {

        var musicUpdateDisposable: Disposable? = null
        musicUpdateDisposable = Observable.defer { Observable.just("") }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    sortList(getSourceList, sortBy, orderBy)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.i("updateHomeList", "onNext")
                            if (!activity.isFinishing) {
                                updateItems(false, getSourceList())
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

    fun sortAlbumList(activity: Context) {
        val sortData = getAlbumSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortAlbumList({ LoadMusicUtil.sAlbumList }, sortBy, isAscending)
    }

    fun sortOnlineAlbumList(activity: Context) {
        val sortData = getAlbumSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortAlbumList({ LoadMusicUtil.sOnlineAlbumList }, sortBy, isAscending)
    }

    private fun sortAlbumList(getSourceList: () -> MutableList<Album>, sortBy: String,
                              isAscending: String) {
        Collections.sort(getSourceList(),
                with(SortConstantUtils) {
                    when (sortBy) {
                        PREF_ALBUM_SORT_BY_ALPHABET -> {
                            if (isAscending == PREF_ORDER_BY_ASCENDING) {
                                AlbumComparatorByAlphabetAscending()
                            } else {
                                AlbumComparatorByAlphabetDescending()
                            }
                        }
                        PREF_ALBUM_SORT_BY_NUMBER_OF_SONGS -> {
                            if (isAscending == PREF_ORDER_BY_ASCENDING) {
                                AlbumComparatorByNumberOfSongsAscending()
                            } else {
                                AlbumComparatorByNumberOfSongsDescending()
                            }
                        }
                        else -> AlbumComparatorByAlphabetDescending()
                    }
                }
        )
    }

    fun sortSongList(activity: Context) {
        val sortData = getSongSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortSongList<Music>({ LoadMusicUtil.sMusicList }, sortBy, isAscending)
    }

    fun sortOnlineSongList(activity: Context) {
        val sortData = getSongSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortSongList<OnlineMusik>({ LoadMusicUtil.sOnlineMusicList }, sortBy, isAscending)
    }

    private fun <T : BaseMusik> sortSongList(getSourceList: () -> MutableList<T>, sortBy: String,
                                             isAscending: String) {
        Collections.sort(getSourceList(),
                with(SortConstantUtils) {
                    when (sortBy) {
                        PREF_SONG_SORT_BY_ALBUM ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                SongComparatorByAlbumAscending()
                            else
                                SongComparatorByAlbumDescending()
                        PREF_SONG_SORT_BY_ALPHABET ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                SongComparatorByAlphabetAscending()
                            else
                                SongComparatorByAlphabetDescending()
                        PREF_SONG_SORT_BY_ARTIST ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                SongComparatorByArtistAscending()
                            else
                                SongComparatorByArtistDescending()
                        PREF_SONG_SORT_BY_DURATION ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                SongComparatorByDurationAscending()
                            else
                                SongComparatorByDurationDescending()
                        PREF_SONG_SORT_BY_GENRE ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                SongComparatorByGenreAscending()
                            else
                                SongComparatorByGenreDescending()
                        PREF_SONG_SORT_BY_RELEASE_YEAR ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                SongComparatorByReleaseYearAscending()
                            else
                                SongComparatorByReleaseYearDescending()
                        PREF_SONG_SORT_BY_DATE_MODIFIED ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                SongComparatorByDateModifiedAscending()
                            else
                                SongComparatorByDateModifiedDescending()
                        else -> SongComparatorByAlbumDescending()
                    }
                })
    }

    fun sortGenreList(activity: Context) {
        val sortData = getGenreSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortGenreList({ LoadMusicUtil.sGenreList }, sortBy, isAscending)
    }

    fun sortOnlineGenreList(activity: Context) {
        val sortData = getGenreSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortGenreList({ LoadMusicUtil.sOnlineGenreList }, sortBy, isAscending)
    }

    private fun sortGenreList(getSourceList: () -> MutableList<Genre>, sortBy: String,
                              isAscending: String) {
        Collections.sort(getSourceList(),
                with(SortConstantUtils) {
                    when (sortBy) {
                        PREF_GENRE_SORT_BY_ALPHABET ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                GenreComparatorByAlphabetAscending()
                            else
                                GenreComparatorByAlphabetDescending()
                        PREF_GENRE_SORT_BY_NUMBER_OF_SONGS ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                GenreComparatorByNumberOfSongsAscending()
                            else
                                GenreComparatorByNumberOfSongsDescending()
                        else -> GenreComparatorByAlphabetDescending()
                    }
                }
        )
    }

    fun sortArtistList(activity: Context) {
        val sortData = getArtistSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortArtistList({ LoadMusicUtil.sArtistList }, sortBy, isAscending)
    }

    fun sortOnlineArtistList(activity: Context) {
        val sortData = getArtistSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortArtistList({ LoadMusicUtil.sOnlineArtistList }, sortBy, isAscending)
    }

    private fun sortArtistList(getSourceList: () -> MutableList<Artist>, sortBy: String,
                               isAscending: String) {
        Collections.sort(getSourceList(),
                with(SortConstantUtils) {
                    when (sortBy) {
                        PREF_ARTIST_SORT_BY_ALPHABET ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                ArtistComparatorByAlphabetAscending()
                            else
                                ArtistComparatorByAlphabetDescending()
                        PREF_ARTIST_SORT_BY_NUMBER_OF_ALBUMS ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                ArtistComparatorByNumberOfAlbumsAscending()
                            else
                                ArtistComparatorByNumberOfAlbumsDescending()
                        PREF_ARTIST_SORT_BY_NUMBER_OF_SONGS ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                ArtistComparatorByNumberOfSongsAscending()
                            else
                                ArtistComparatorByNumberOfSongsDescending()
                        else -> ArtistComparatorByAlphabetDescending()
                    }
                }
        )
    }

    fun sortPlaylistList(activity: Context) {
        val sortData = getPlaylistSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortPlaylistList({ LoadMusicUtil.sPlaylistList }, sortBy, isAscending)
    }

    private fun sortPlaylistList(getSourceList: () -> MutableList<Playlist>, sortBy: String,
                                 isAscending: String) {
        Collections.sort(getSourceList(),
                with(SortConstantUtils) {
                    when (sortBy) {
                        PREF_PLAYLIST_SORT_BY_ALPHABET ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                PlaylistComparatorByAlphabetAscending()
                            else
                                PlaylistComparatorByAlphabetDescending()
                        PREF_PLAYLIST_SORT_BY_CREATED_TIME ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                PlaylistComparatorByCreatedTimeAscending()
                            else
                                PlaylistComparatorByCreatedTimeDescending()
                        PREF_PLAYLIST_SORT_BY_NUMBER_OF_SONGS ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                PlaylistComparatorByNumberOfSongsAscending()
                            else
                                PlaylistComparatorByNumberOfSongsDescending()
                        else -> PlaylistComparatorByAlphabetDescending()
                    }
                }
        )
    }

    fun sortOnlinePlaylistList(activity: Context) {
        val sortData = getPlaylistSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortOnlinePlaylistList({ LoadMusicUtil.sOnlinePlaylistList }, sortBy, isAscending)
    }

    private fun sortOnlinePlaylistList(getSourceList: () -> MutableList<OnlinePlaylist>,
                                       sortBy: String, isAscending: String) {
        Collections.sort(getSourceList(),
                with(SortConstantUtils) {
                    when (sortBy) {
                        PREF_PLAYLIST_SORT_BY_ALPHABET ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                OnlinePlaylistComparatorByAlphabetAscending()
                            else
                                OnlinePlaylistComparatorByAlphabetDescending()
                        PREF_PLAYLIST_SORT_BY_CREATED_TIME ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                OnlinePlaylistComparatorByCreatedTimeAscending()
                            else
                                OnlinePlaylistComparatorByCreatedTimeDescending()
                        PREF_PLAYLIST_SORT_BY_NUMBER_OF_SONGS ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                OnlinePlaylistComparatorByNumberOfSongsAscending()
                            else
                                OnlinePlaylistComparatorByNumberOfSongsDescending()
                        else -> OnlinePlaylistComparatorByAlphabetDescending()
                    }
                }
        )
    }

    fun sortFolderList(activity: Context) {
        val sortData = getFolderSortState(WeakReference(activity))
        val sortBy = sortData[0]
        val isAscending = sortData[1]
        sortFolderList({ LoadMusicUtil.sFolderList }, sortBy, isAscending)
    }

    private fun sortFolderList(getSourceList: () -> MutableList<Folder>, sortBy: String, isAscending: String) {
        Collections.sort(getSourceList(),
                with(SortConstantUtils) {
                    when (sortBy) {
                        PREF_FOLDER_SORT_BY_ALPHABET ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                FolderComparatorByAlphabetAscending()
                            else
                                FolderComparatorByAlphabetDescending()
                        PREF_FOLDER_SORT_BY_NUMBER_OF_SONGS ->
                            if (isAscending == PREF_ORDER_BY_ASCENDING)
                                FolderComparatorByNumberOfSongsAscending()
                            else
                                FolderComparatorByNumberOfSongsDescending()
                        else -> FolderComparatorByAlphabetDescending()
                    }
                }
        )
    }
}
