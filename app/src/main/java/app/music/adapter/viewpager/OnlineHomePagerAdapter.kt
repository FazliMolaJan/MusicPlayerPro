package app.music.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import app.music.ui.screen.album.OnlineAlbumFragment
import app.music.ui.screen.artist.OnlineArtistFragment
import app.music.ui.screen.genre.OnlineGenreFragment
import app.music.ui.screen.playlist.OnlinePlaylistFragment
import app.music.ui.screen.song.OnlineSongFragment
import app.music.utils.TabTitleUtils

class OnlineHomePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnlineAlbumFragment()
            1 -> OnlineSongFragment()
            2 -> OnlineArtistFragment()
            3 -> OnlineGenreFragment()
            4 -> OnlinePlaylistFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int = TabTitleUtils.ONLINE_HOME_TAB_TITLE.size

    override fun getPageTitle(position: Int): CharSequence? {
        return TabTitleUtils.ONLINE_HOME_TAB_TITLE[position]
    }
}
