package app.music.ui.screen.genre

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.recycler.GenreAdapter
import app.music.databinding.FragmentGenreBinding
import app.music.listener.homefragmentlistener.GenreFragmentListener
import app.music.listener.itemclick.GenreFragmentItemClickListener
import app.music.model.entity.Genre
import app.music.ui.screen.home.BaseHomeFragment
import app.music.ui.screen.home.HomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewholder.GenreViewHolder
import app.music.viewmodel.HomeActivityViewModel
import java.lang.ref.WeakReference

class GenreFragment
    : BaseHomeFragment<Genre,
        HomeActivityViewModel,
        FragmentGenreBinding,
        GenreViewHolder,
        GenreAdapter>(),
        GenreFragmentListener,
        GenreFragmentItemClickListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).mGenreFragmentListener = this
    }

    override fun getRecyclerAdapter(): GenreAdapter {
        return GenreAdapter(
                WeakReference<Activity>(activity),
                this::onGenreClick,
                this::onGenreLongClick
        )
    }

    override fun getDataList(): List<Genre> = LoadMusicUtil.sGenreList

    override fun getRecyclerView(): RecyclerView = binding.recyclerview

    override fun getRefreshLayout(): SwipeRefreshLayout = binding.refreshlayout

    override fun initInject() {
        fragmentComponent?.inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_genre

    override fun getLogTag() = TAG

    override fun initView() {
        super.initView()
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true, true)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewGenreList(
                activity as Activity,
                binding.refreshlayout,
                mRecyclerAdapter::updateItems
        )
    }

    override fun onSortGenre(sortBy: String, isAscending: String) {
        SortMethodUtils.sortGenreList(
                activity as Activity,
                sortBy,
                isAscending,
                mRecyclerAdapter::updateItems
        )
    }

    companion object {

        private const val TAG = "GenreFragment"
    }
}
