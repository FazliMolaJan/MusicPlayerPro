package app.music.base;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.music.model.entity.OnlineMusik;
import app.music.network.APIUtils;
import app.music.utils.ListUtils;
import app.music.utils.musicloading.LoadMusicUtil;
import retrofit2.Call;
import retrofit2.Response;

public abstract class BaseReloadListAsyncTask
        <T, C extends Comparator<? super T>, A extends BaseRecyclerAdapter>
        extends AsyncTask<Boolean, Void, Void> {

    protected WeakReference<Fragment> mFragmentReference;
    private A mRecyclerAdapter;
    private WeakReference<SwipeRefreshLayout> mRefreshLayoutReference;
    private C mComparator;
    private static final String TAG = "BaseReloadListAsyncTask";

    public BaseReloadListAsyncTask(WeakReference<Fragment> fragmentReference,
                                   A recyclerAdapter,
                                   WeakReference<SwipeRefreshLayout> refreshLayoutReference,
                                   C comparator) {
        this.mFragmentReference = fragmentReference;
        this.mRecyclerAdapter = recyclerAdapter;
        this.mRefreshLayoutReference = refreshLayoutReference;
        this.mComparator = comparator;
    }

    @Override
    protected Void doInBackground(Boolean... booleans) {
        if (isCancelled()) return null;
        Activity activity = mFragmentReference.get().getActivity();
        Boolean isReloadTypeList = booleans[0];
        if (activity != null) {
            if (isReloadTypeList) {
                //boolens.length > 1 is reload online music
                if (booleans.length > 1) {
                    ListUtils.INSTANCE.clearList(LoadMusicUtil.sOnlineMusicList);
                    Call<List<OnlineMusik>> callOnlineMusic = APIUtils.INSTANCE.getAllMusicCall();
                    Response<List<OnlineMusik>> responseList = null;
                    if (callOnlineMusic != null) {
                        try {
                            responseList = callOnlineMusic.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (responseList != null && responseList.body() != null) {
                        List<OnlineMusik> tempList = new ArrayList<>(responseList.body());
                        if (tempList.size() > 0) {
                            for (OnlineMusik music : tempList) {
                                Log.i(TAG, music.toString());
                                LoadMusicUtil.sOnlineMusicList.add(music);
                            }
                        }
                    }
                } else {
                    LoadMusicUtil.getMusic(activity);
                }
                Log.i(TAG, "about reload type list");
                reloadTypeList();
            }
            Log.i(TAG, "do in background about end");
            Collections.sort(getTypeList(), mComparator);
        }
        if (isCancelled()) return null;
        else return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (getTypeList() == null) return;
        Fragment fragment = mFragmentReference.get();
        if (fragment == null || fragment.isDetached()) return;
        Activity activity = fragment.getActivity();
        if (activity == null || activity.isFinishing()) return;
        mRecyclerAdapter.updateItems(false, getTypeList());
        mRefreshLayoutReference.get().setRefreshing(false);
        super.onPostExecute(aVoid);
    }

    protected abstract void reloadTypeList();

    protected abstract List<T> getTypeList();
}
