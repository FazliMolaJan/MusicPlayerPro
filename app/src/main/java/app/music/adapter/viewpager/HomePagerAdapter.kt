package app.music.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import app.music.ui.screen.album.AlbumFragment
import app.music.ui.screen.artist.ArtistFragment
import app.music.ui.screen.folder.FolderFragment
import app.music.ui.screen.genre.GenreFragment
import app.music.ui.screen.playlist.PlaylistFragment
import app.music.ui.screen.song.SongFragment
import app.music.utils.TabTitleUtils

class HomePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AlbumFragment()
            1 -> SongFragment()
            2 -> ArtistFragment()
            3 -> GenreFragment()
            4 -> PlaylistFragment()
            5 -> FolderFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int = TabTitleUtils.HOME_TAB_TITLE.size

    override fun getPageTitle(position: Int): CharSequence? = TabTitleUtils.HOME_TAB_TITLE[position]
}
