package app.music.listener.dialoglistener

import android.content.Context
import com.google.android.material.button.MaterialButton
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import app.music.R
import app.music.listener.homefragmentlistener.*
import app.music.utils.InflaterUtils
import app.music.utils.dialog.BottomSheetDialogUtils
import app.music.utils.sort.SortConstantUtils
import app.music.utils.sort.SortMethodUtils
import java.lang.ref.WeakReference

interface DialogHomeItemSortListener {

    var mMenuSortDialog: BottomSheetDialog?
    var mAlbumSortDialog: BottomSheetDialog?
    var mSongSortDialog: BottomSheetDialog?
    var mArtistSortDialog: BottomSheetDialog?
    var mGenreSortDialog: BottomSheetDialog?
    var mPlaylistSortDialog: BottomSheetDialog?

    var mAlbumFragmentListener: AlbumFragmentListener?
    var mSongFragmentListener: SongFragmentListener?
    var mArtistFragmentListener: ArtistFragmentListener?
    var mGenreFragmentListener: GenreFragmentListener?
    var mPlaylistFragmentListener: PlaylistFragmentListener?

    // Menu Sort Dialog
    fun initMenuSortDialog() {
        val context = this as Context
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_menu_sort)
        mMenuSortDialog = BottomSheetDialog(context, R.style.DialogStyle)
        mMenuSortDialog?.setContentView(view)
        BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 500f)
        with(view) {
            findViewById<TextView>(R.id.text_album).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mMenuSortDialog)
                showAlbumSortDialog()
            }
            findViewById<TextView>(R.id.text_song).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mMenuSortDialog)
                showSongSortDialog()
            }
            findViewById<TextView>(R.id.text_artist).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mMenuSortDialog)
                showArtistSortDialog()
            }
            findViewById<TextView>(R.id.text_genre).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mMenuSortDialog)
                showGenreSortDialog()
            }
            findViewById<TextView>(R.id.text_playlist).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mMenuSortDialog)
                showPlaylistSortDialog()
            }
            findViewById<MaterialButton>(R.id.button_ok).setOnClickListener {
                BottomSheetDialogUtils.dismissDialog(mMenuSortDialog)
            }
        }
    }

    fun showMenuSortDialog() {
        if (null == mMenuSortDialog) {
            initMenuSortDialog()
        }
        mMenuSortDialog!!.show()
    }

    // Album Sort Dialog
    fun initAlbumSortDialog() {
        val context = this as Context
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_album_sort)
        mAlbumSortDialog = BottomSheetDialog(context, R.style.DialogStyle)
        mAlbumSortDialog!!.setContentView(view)
        BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 600f)
        with(view) {
            findViewById<MaterialButton>(R.id.button_back).setOnClickListener {
                setAlbumSortState(findViewById(R.id.radio_group_sort_by))
                showMenuSortDialog()
            }
            findViewById<MaterialButton>(R.id.button_done).setOnClickListener {
                setAlbumSortState(findViewById(R.id.radio_group_sort_by))
            }
        }
    }

    fun updateAlbumSortDialogData() {
        val context = this as Context
        val sortState = SortMethodUtils.getAlbumSortState(WeakReference(context))
        mAlbumSortDialog?.run {
            if (sortState[1] == SortConstantUtils.PREF_ORDER_BY_ASCENDING) {
                findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked = true
            }

            when (sortState[0]) {
                SortConstantUtils.PREF_ALBUM_SORT_BY_ALPHABET -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_alphabet)!!.isChecked = true
                }
                SortConstantUtils.PREF_ALBUM_SORT_BY_NUMBER_OF_SONGS -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_number_of_songs)!!.isChecked = true
                }
                else -> {
                }
            }
        }
    }

    fun setAlbumSortState(radioGroup: RadioGroup) {
        var sortBy = ""
        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_sort_by_alphabet -> {
                sortBy = SortConstantUtils.PREF_ALBUM_SORT_BY_ALPHABET
            }
            R.id.radio_sort_by_number_of_songs -> {
                sortBy = SortConstantUtils.PREF_ALBUM_SORT_BY_NUMBER_OF_SONGS
            }
            else -> {
            }
        }
        if (mAlbumSortDialog!!.findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked) {
            sortAlbumList(sortBy, SortConstantUtils.PREF_ORDER_BY_ASCENDING)
        } else {
            sortAlbumList(sortBy, SortConstantUtils.PREF_ORDER_BY_DESCENDING)
        }
        BottomSheetDialogUtils.dismissDialog(mAlbumSortDialog)
    }

    fun sortAlbumList(sortBy: String, orderBy: String) {
        SortMethodUtils.saveAlbumSortState(WeakReference(this as Context), sortBy, orderBy)
        mAlbumFragmentListener!!.onSortAlbum(sortBy, orderBy)
    }

    fun showAlbumSortDialog() {
        if (null == mAlbumSortDialog) {
            initAlbumSortDialog()
        }
        updateAlbumSortDialogData()
        mAlbumSortDialog!!.show()
    }

    // Song Sort Dialog
    fun initSongSortDialog() {
        val context = this as Context
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_song_sort)
        mSongSortDialog = BottomSheetDialog(context, R.style.DialogStyle)
        mSongSortDialog!!.setContentView(view)
        BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 600f)
        with(view) {
            findViewById<MaterialButton>(R.id.button_back).setOnClickListener {
                setSongSortState(findViewById(R.id.radio_group_sort_by))
                showMenuSortDialog()
            }
            findViewById<MaterialButton>(R.id.button_done).setOnClickListener {
                setSongSortState(findViewById(R.id.radio_group_sort_by))
            }
        }
    }

    fun updateSongSortDialogData() {
        val context = this as Context
        val sortState = SortMethodUtils.getSongSortState(WeakReference(context))
        mSongSortDialog?.run {
            if (sortState[1] == SortConstantUtils.PREF_ORDER_BY_ASCENDING) {
                findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked = true
            }

            when (sortState[0]) {
                SortConstantUtils.PREF_SONG_SORT_BY_ALBUM -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_album)!!.isChecked = true
                }
                SortConstantUtils.PREF_SONG_SORT_BY_ALPHABET -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_alphabet)!!.isChecked = true
                }
                SortConstantUtils.PREF_SONG_SORT_BY_ARTIST -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_artist)!!.isChecked = true
                }
                SortConstantUtils.PREF_SONG_SORT_BY_DURATION -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_duration)!!.isChecked = true
                }
                SortConstantUtils.PREF_SONG_SORT_BY_GENRE -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_genre)!!.isChecked = true
                }
                SortConstantUtils.PREF_SONG_SORT_BY_RELEASE_YEAR -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_year)!!.isChecked = true
                }
                SortConstantUtils.PREF_SONG_SORT_BY_DATE_MODIFIED -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_date_modified)!!.isChecked = true
                }
                else -> {
                }
            }
        }
    }

    fun setSongSortState(radioGroup: RadioGroup) {
        var sortBy = ""
        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_sort_by_alphabet -> sortBy = SortConstantUtils.PREF_SONG_SORT_BY_ALPHABET
            R.id.radio_sort_by_artist -> sortBy = SortConstantUtils.PREF_SONG_SORT_BY_ARTIST
            R.id.radio_sort_by_year -> sortBy = SortConstantUtils.PREF_SONG_SORT_BY_RELEASE_YEAR
            R.id.radio_sort_by_album -> sortBy = SortConstantUtils.PREF_SONG_SORT_BY_ALBUM
            R.id.radio_sort_by_genre -> sortBy = SortConstantUtils.PREF_SONG_SORT_BY_GENRE
            R.id.radio_sort_by_duration -> sortBy = SortConstantUtils.PREF_SONG_SORT_BY_DURATION
            R.id.radio_sort_by_date_modified -> sortBy = SortConstantUtils.PREF_SONG_SORT_BY_DATE_MODIFIED
            else -> {
            }
        }
        if (mSongSortDialog!!.findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked) {
            sortSongList(sortBy, SortConstantUtils.PREF_ORDER_BY_ASCENDING)
        } else {
            sortSongList(sortBy, SortConstantUtils.PREF_ORDER_BY_DESCENDING)
        }
        BottomSheetDialogUtils.dismissDialog(mSongSortDialog)
    }

    fun sortSongList(sortBy: String, orderBy: String) {
        mSongFragmentListener!!.onSortMusic(sortBy, orderBy)
        SortMethodUtils.saveSongSortState(WeakReference(this as Context), sortBy, orderBy)
    }

    fun showSongSortDialog() {
        if (null == mSongSortDialog) {
            initSongSortDialog()
        }
        updateSongSortDialogData()
        mSongSortDialog!!.show()
    }

    // Artist Sort Dialog
    fun initArtistSortDialog() {
        val context = this as Context
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_artist_sort)
        mArtistSortDialog = BottomSheetDialog(context, R.style.DialogStyle)
        mArtistSortDialog!!.setContentView(view)
        BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 600f)
        with(view) {
            findViewById<MaterialButton>(R.id.button_back).setOnClickListener {
                setArtistSortState(findViewById(R.id.radio_group_sort_by))
                showMenuSortDialog()
            }
            findViewById<MaterialButton>(R.id.button_done).setOnClickListener {
                setArtistSortState(findViewById(R.id.radio_group_sort_by))
            }
        }
    }

    fun updateArtistSortDialogData() {
        val context = this as Context
        val sortState = SortMethodUtils.getArtistSortState(WeakReference(context))
        mArtistSortDialog?.run {
            if (sortState[1] == SortConstantUtils.PREF_ORDER_BY_ASCENDING) {
                findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked = true
            }

            when (sortState[0]) {
                SortConstantUtils.PREF_ARTIST_SORT_BY_ALPHABET -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_alphabet)!!.isChecked = true
                }
                SortConstantUtils.PREF_ARTIST_SORT_BY_NUMBER_OF_SONGS -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_number_of_songs)!!.isChecked = true
                }
                SortConstantUtils.PREF_ARTIST_SORT_BY_NUMBER_OF_ALBUMS -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_number_of_albums)!!.isChecked = true
                }
                else -> {
                }
            }
        }
    }

    fun setArtistSortState(radioGroup: RadioGroup) {
        var sortBy = ""
        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_sort_by_alphabet -> {
                sortBy = SortConstantUtils.PREF_ARTIST_SORT_BY_ALPHABET
            }
            R.id.radio_sort_by_number_of_albums -> {
                sortBy = SortConstantUtils.PREF_ARTIST_SORT_BY_NUMBER_OF_ALBUMS
            }
            R.id.radio_sort_by_number_of_songs -> {
                sortBy = SortConstantUtils.PREF_ARTIST_SORT_BY_NUMBER_OF_SONGS
            }
            else -> {
            }
        }
        if (mArtistSortDialog!!.findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked) {
            sortArtistList(sortBy, SortConstantUtils.PREF_ORDER_BY_ASCENDING)
        } else {
            sortArtistList(sortBy, SortConstantUtils.PREF_ORDER_BY_DESCENDING)
        }
        BottomSheetDialogUtils.dismissDialog(mArtistSortDialog)
    }

    fun sortArtistList(sortBy: String, orderBy: String) {
        mArtistFragmentListener!!.onSortArtist(sortBy, orderBy)
        SortMethodUtils.saveArtistSortState(WeakReference(this as Context), sortBy, orderBy)
    }

    fun showArtistSortDialog() {
        if (null == mArtistSortDialog) {
            initArtistSortDialog()
        }
        updateArtistSortDialogData()
        mArtistSortDialog!!.show()
    }

    // Genre Sort Dialog
    fun initGenreSortDialog() {
        val context = this as Context
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_genre_sort)
        mGenreSortDialog = BottomSheetDialog(context, R.style.DialogStyle)
        mGenreSortDialog!!.setContentView(view)
        BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 600f)
        with(view) {
            findViewById<MaterialButton>(R.id.button_back).setOnClickListener {
                setGenreSortState(findViewById(R.id.radio_group_sort_by))
                showMenuSortDialog()
            }
            findViewById<MaterialButton>(R.id.button_done).setOnClickListener {
                setGenreSortState(findViewById(R.id.radio_group_sort_by))
            }
        }
    }

    fun updateGenreSortDialogData() {
        val context = this as Context
        val sortState = SortMethodUtils.getGenreSortState(WeakReference(context))
        mGenreSortDialog?.run {
            if (sortState[1] == SortConstantUtils.PREF_ORDER_BY_ASCENDING) {
                findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked = true
            }

            when (sortState[0]) {
                SortConstantUtils.PREF_GENRE_SORT_BY_ALPHABET -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_alphabet)!!.isChecked = true
                }
                SortConstantUtils.PREF_GENRE_SORT_BY_NUMBER_OF_SONGS -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_number_of_songs)!!.isChecked = true
                }
                else -> {
                }
            }
        }
    }

    fun setGenreSortState(radioGroup: RadioGroup) {
        var sortBy = ""
        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_sort_by_alphabet -> {
                sortBy = SortConstantUtils.PREF_GENRE_SORT_BY_ALPHABET
            }
            R.id.radio_sort_by_number_of_songs -> {
                sortBy = SortConstantUtils.PREF_GENRE_SORT_BY_NUMBER_OF_SONGS
            }
            else -> {
            }
        }
        if (mGenreSortDialog!!.findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked) {
            sortGenreList(sortBy, SortConstantUtils.PREF_ORDER_BY_ASCENDING)
        } else {
            sortGenreList(sortBy, SortConstantUtils.PREF_ORDER_BY_DESCENDING)
        }
        BottomSheetDialogUtils.dismissDialog(mGenreSortDialog)
    }

    fun sortGenreList(sortBy: String, orderBy: String) {
        mGenreFragmentListener!!.onSortGenre(sortBy, orderBy)
        SortMethodUtils.saveGenreSortState(WeakReference(this as Context), sortBy, orderBy)
    }

    fun showGenreSortDialog() {
        if (null == mGenreSortDialog) {
            initGenreSortDialog()
        }
        updateGenreSortDialogData()
        mGenreSortDialog!!.show()
    }

    // Playlist Sort Dialog
    fun initPlaylistSortDialog() {
        val context = this as Context
        val view = InflaterUtils.getInflatedView(WeakReference(context), R.layout.dialog_playlist_sort)
        mPlaylistSortDialog = BottomSheetDialog(context, R.style.DialogStyle)
        mPlaylistSortDialog!!.setContentView(view)
        BottomSheetDialogUtils.setBottomSheetBehaviorPeekHeight(WeakReference(context), view, 600f)
        with(view) {
            findViewById<MaterialButton>(R.id.button_back).setOnClickListener {
                setPlaylistSortState(findViewById(R.id.radio_group_sort_by))
                showMenuSortDialog()
            }
            findViewById<MaterialButton>(R.id.button_done).setOnClickListener {
                setPlaylistSortState(findViewById(R.id.radio_group_sort_by))
            }
        }
    }

    fun updatePlaylistSortDialogData() {
        val context = this as Context
        val sortState = SortMethodUtils.getPlaylistSortState(WeakReference(context))
        mPlaylistSortDialog?.run {
            if (sortState[1] == SortConstantUtils.PREF_ORDER_BY_ASCENDING) {
                findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked = true
            }

            when (sortState[0]) {
                SortConstantUtils.PREF_PLAYLIST_SORT_BY_ALPHABET -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_alphabet)!!.isChecked = true
                }
                SortConstantUtils.PREF_PLAYLIST_SORT_BY_NUMBER_OF_SONGS -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_number_of_songs)!!.isChecked = true
                }
                SortConstantUtils.PREF_PLAYLIST_SORT_BY_CREATED_TIME -> {
                    findViewById<RadioButton>(R.id.radio_sort_by_created_time)!!.isChecked = true
                }
                else -> {
                }
            }
        }
    }

    fun setPlaylistSortState(radioGroup: RadioGroup) {
        var sortBy = ""
        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_sort_by_alphabet -> {
                sortBy = SortConstantUtils.PREF_PLAYLIST_SORT_BY_ALPHABET
            }
            R.id.radio_sort_by_created_time -> {
                sortBy = SortConstantUtils.PREF_PLAYLIST_SORT_BY_CREATED_TIME
            }
            R.id.radio_sort_by_number_of_songs -> {
                sortBy = SortConstantUtils.PREF_PLAYLIST_SORT_BY_NUMBER_OF_SONGS
            }
            else -> {
            }
        }
        if (mPlaylistSortDialog!!.findViewById<CheckBox>(R.id.checkbox_ascending)!!.isChecked) {
            sortPlaylistList(sortBy, SortConstantUtils.PREF_ORDER_BY_ASCENDING)
        } else {
            sortPlaylistList(sortBy, SortConstantUtils.PREF_ORDER_BY_DESCENDING)
        }
        BottomSheetDialogUtils.dismissDialog(mPlaylistSortDialog)
    }

    fun sortPlaylistList(sortBy: String, orderBy: String) {
        mPlaylistFragmentListener!!.onSortPlaylist(sortBy, orderBy)
        SortMethodUtils.savePlaylistSortState(WeakReference(this as Context), sortBy, orderBy)
    }

    fun showPlaylistSortDialog() {
        if (null == mPlaylistSortDialog) {
            initPlaylistSortDialog()
        }
        updatePlaylistSortDialogData()
        mPlaylistSortDialog!!.show()
    }
}