package app.music.ui.screen.detailplaymusic;


import android.animation.ObjectAnimator;
import android.content.Context;

import com.bumptech.glide.request.RequestOptions;
import com.squareup.leakcanary.RefWatcher;

import app.music.R;
import app.music.base.BaseApplication;
import app.music.base.BaseFragment;
import app.music.databinding.FragmentSecondDetailBinding;
import app.music.listener.DetailPlayMusicListener;

public class SecondDetailFragment
        extends BaseFragment<FragmentSecondDetailBinding> {

    private static final String TAG = "SecondDetailFragment";
    private ObjectAnimator mAnim;
    private DetailPlayMusicListener mDetailPlayMusicListener;
    private RequestOptions mRequestOptions = new RequestOptions()
            .error(R.drawable.ic_album);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailPlayMusicListener) {
            mDetailPlayMusicListener = (DetailPlayMusicListener) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_second_detail;
    }

    @Override
    public String getLogTag() {
        return TAG;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
