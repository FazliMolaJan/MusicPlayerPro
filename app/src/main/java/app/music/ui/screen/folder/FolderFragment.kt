package app.music.ui.screen.folder

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.music.R
import app.music.adapter.recycler.FolderAdapter
import app.music.databinding.FragmentFolderBinding
import app.music.listener.homefragmentlistener.FolderFragmentListener
import app.music.listener.itemclick.FolderFragmentItemClickListener
import app.music.model.entity.Folder
import app.music.ui.screen.home.BaseHomeFragment
import app.music.ui.screen.home.HomeActivity
import app.music.utils.musicloading.HomeFragmentDataUpdatingUtil
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.recyclerview.RecyclerViewUtils
import app.music.utils.sort.SortMethodUtils
import app.music.viewholder.FolderViewHolder
import app.music.viewmodel.HomeActivityViewModel
import java.lang.ref.WeakReference

class FolderFragment
    : BaseHomeFragment<Folder,
        HomeActivityViewModel,
        FragmentFolderBinding,
        FolderViewHolder,
        FolderAdapter>(),
        FolderFragmentListener,
        FolderFragmentItemClickListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).setFolderFragmentListener(this)
    }

    override fun getRecyclerAdapter(): FolderAdapter {
        return FolderAdapter(
                WeakReference<Activity>(activity),
                this::onFolderClick,
                this::onFolderLongClick
        )
    }

    override fun getDataList(): List<Folder> = LoadMusicUtil.sFolderList

    override fun getRecyclerView(): RecyclerView = binding.recyclerview

    override fun getRefreshLayout(): SwipeRefreshLayout = binding.refreshlayout

    override fun initInject() {
        fragmentComponent?.inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_folder

    override fun getLogTag() = TAG

    override fun initView() {
        super.initView()
        RecyclerViewUtils.setVerticalLinearLayout(binding.recyclerview, context!!, true, true)
    }

    override fun onRefresh() {
        HomeFragmentDataUpdatingUtil.getNewFolderList(
                activity as Activity,
                binding.refreshlayout,
                mRecyclerAdapter::updateItems
        )
    }

    override fun onSortFolder(sortBy: String, isAscending: String) {
        SortMethodUtils.sortFolderList(
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
