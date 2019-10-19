package app.music.ui.screen.detailplaymusic;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.request.RequestOptions;
import com.squareup.leakcanary.RefWatcher;

import app.music.R;
import app.music.base.BaseApplication;
import app.music.base.BaseFragment;
import app.music.databinding.FragmentThirdDetailBinding;
import app.music.listener.DetailPlayMusicListener;
import app.music.listener.ThirdDetailFragmentListener;

public class ThirdDetailFragment
        extends BaseFragment<FragmentThirdDetailBinding>
        implements ThirdDetailFragmentListener {

    private static final String TAG = "ThirdDetailFragment";
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
    protected void initInject() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_third_detail;
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

    @Override
    public void updateLyrics(String lyrics) {
        binding.textLyrics.setText(TextUtils.isEmpty(lyrics) ? getString(R.string.empty_string) : lyrics);
    }
}
