package app.music.utils.intent

import android.app.Activity
import android.content.Context
import android.content.Intent
import app.music.model.*
import app.music.ui.screen.detailalbum.DetailAlbumActivity
import app.music.ui.screen.detailartist.DetailArtistActivity
import app.music.ui.screen.detailfavorite.DetailFavoriteActivity
import app.music.ui.screen.detailfolder.DetailFolderActivity
import app.music.ui.screen.detailgenre.DetailGenreActivity
import app.music.ui.screen.detailplaylist.DetailOnlinePlaylistActivity
import app.music.ui.screen.detailplaylist.DetailPlaylistActivity
import app.music.ui.screen.detailplaymv.DetailPlayMvActivity
import app.music.ui.screen.home.HomeActivity
import app.music.ui.screen.onlinemusic.OnlineHomeActivity
import app.music.ui.screen.setting.ChooseThemeActivity

object IntentMethodUtils {

    fun launchChooseThemeActivity(context: Context) = startActivity<ChooseThemeActivity>(context)

    private inline fun <reified T : Activity> startActivity(context: Context) {
        context.startActivity(Intent(context, T::class.java))
    }

    private inline fun <reified T : Activity> startActivity(
            context: Context?, putData: (Intent) -> Unit) {
        context?.startActivity(Intent(context, T::class.java).apply(putData))
    }

    fun launchDetailAlbumActivity(context: Context?, album: Album) {
//        val intent = Intent(context, DetailAlbumActivity::class.java).apply {
//            putExtra(IntentConstantUtils.EXTRA_ALBUM_OBJECT_TO_DETAIL_ALBUM, album)
//        }
//        context.startActivity(intent)

        startActivity<DetailAlbumActivity>(context) { intent ->
            intent.putExtra(IntentConstantUtils.EXTRA_ALBUM_OBJECT_TO_DETAIL_ALBUM, album)
        }
    }

    fun launchDetailArtistActivity(context: Context?, artist: Artist) {
        startActivity<DetailArtistActivity>(context) { intent ->
            intent.putExtra(IntentConstantUtils.EXTRA_ARTIST_OBJECT_TO_DETAIL_ARTIST, artist)
        }
    }

    fun launchDetailGenreActivity(context: Context?, genre: Genre) {
        startActivity<DetailGenreActivity>(context) { intent ->
            intent.putExtra(IntentConstantUtils.EXTRA_GENRE_OBJECT_TO_DETAIL_GENRE, genre)
        }
    }

    fun launchDetailPlaylistActivity(context: Context?, playlist: Playlist) {
        startActivity<DetailPlaylistActivity>(context) { intent ->
            intent.putExtra(IntentConstantUtils.EXTRA_PLAYLIST_OBJECT_TO_DETAIL_PLAYLIST, playlist)
        }
    }

    fun launchDetailFolderActivity(context: Context?, folder: Folder) {
        startActivity<DetailFolderActivity>(context) { intent ->
            intent.putExtra(IntentConstantUtils.EXTRA_FOLDER_OBJECT_TO_DETAIL_FOLDER, folder)
        }
    }

    fun launchOnlineHomeActivity(context: Context, isFinish: Boolean) {
        launchOnlineHomeActivity(context)
        if (isFinish) {
            (context as Activity).finish()
        }
    }

    fun launchHomeActivity(context: Context, isFinish: Boolean) {
        launchHomeActivity(context)
        if (isFinish) {
            (context as Activity).finish()
        }
    }

    fun launchDetailFavoriteActivity(context: Context) = startActivity<DetailFavoriteActivity>(context)

    fun launchDetailOnlinePlaylistActivity(context: Context?, playlist: OnlinePlaylist) {
        startActivity<DetailOnlinePlaylistActivity>(context) { intent ->
            intent.putExtra(IntentConstantUtils.EXTRA_PLAYLIST_OBJECT_TO_DETAIL_PLAYLIST, playlist)
        }
    }

    fun launchDetailPlayMvActivity(context: Context, music: BaseMusik) {
        startActivity<DetailPlayMvActivity>(context) { intent ->
            intent.putExtra(IntentConstantUtils.EXTRA_MUSIC_OBJECT_TO_DETAIL_PLAY_MV, music)
        }
    }

    private fun launchOnlineHomeActivity(context: Context) = startActivity<OnlineHomeActivity>(context)

    private fun launchHomeActivity(context: Context) = startActivity<HomeActivity>(context)
}
