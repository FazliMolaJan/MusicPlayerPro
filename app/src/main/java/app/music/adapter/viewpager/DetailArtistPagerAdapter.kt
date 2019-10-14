package app.music.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import app.music.ui.screen.detailartist.DetailArtistAlbumFragment
import app.music.ui.screen.detailartist.DetailArtistTrackFragment
import app.music.utils.TabTitleUtils

class DetailArtistPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DetailArtistTrackFragment()
            1 -> DetailArtistAlbumFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int = TabTitleUtils.DETAIL_ARTIST_TAB_TITLE.size

    override fun getPageTitle(position: Int): CharSequence? {
        return TabTitleUtils.DETAIL_ARTIST_TAB_TITLE[position]
    }
}
