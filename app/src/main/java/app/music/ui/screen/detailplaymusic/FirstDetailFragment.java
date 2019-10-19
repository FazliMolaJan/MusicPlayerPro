package app.music.ui.screen.detailplaymusic;


import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.squareup.leakcanary.RefWatcher;

import app.music.R;
import app.music.base.BaseApplication;
import app.music.databinding.FragmentFirstDetailBinding;
import app.music.listener.DetailPlayMusicListener;
import app.music.listener.FirstDetailFragmentListener;
import app.music.utils.imageloading.ImageLoadingUtils;

public class FirstDetailFragment extends Fragment
        implements FirstDetailFragmentListener,
        View.OnClickListener {

    private static final String TAG = "FirstDetailFragment";
    //    private ObjectAnimator mObjectAnimator;
    private DetailPlayMusicListener mDetailPlayMusicListener;
    private RequestOptions mRequestOptions = new RequestOptions()
            .error(R.drawable.ic_disc);
    private FragmentFirstDetailBinding mBinding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailPlayMusicListener) {
            mDetailPlayMusicListener = (DetailPlayMusicListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_first_detail, container, false);
        View rootView = mBinding.getRoot();
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_center_play_pause:
//                mDetailPlayMusicListener.changeExoPlayerState();
//                break;
//        }
    }

    @Override
    public void changeCenterCoverArt(byte[] byteArray) {
        ImageLoadingUtils.INSTANCE.loadImage(mBinding.imgCenterCoverArt, byteArray, mRequestOptions);
    }

    @Override
    public void changeCenterCoverArt(String coverArtLink) {
        ImageLoadingUtils.INSTANCE.loadImage(mBinding.imgCenterCoverArt, coverArtLink, mRequestOptions);
    }

    @Override
    public void resumeAnimation() {
        Log.e("my log", "resume anim");
    }

    @Override
    public void pauseAnimation() {
        Log.e("my log", "pause anim");
    }

    @Override
    public void startAnimation() {
        Log.e("my log", "start anim");
    }

    @Override
    public void changePlayButtonActivate(boolean buttonActivate) {
        Log.e("my log", "change play button activate");
//        mBinding.btnCenterPlayPause.setActivated(buttonActivate);
    }

    public void initView() {

    }

    public void initData() {
//        mBinding.btnCenterPlayPause.setOnClickListener(this);
    }
}
