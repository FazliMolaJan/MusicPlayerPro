package app.music.ui.screen.detailplaymusic;

import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import app.music.R;
import app.music.base.BaseMusicServiceActivity;
import app.music.databinding.ActivityDetailPlayMusicBinding;
import app.music.listener.DetailPlayMusicListener;
import app.music.model.BaseMusik;
import app.music.model.OnlineMusik;
import app.music.utils.ConstantUtil;
import app.music.utils.blur.BlurImageUtils;
import app.music.utils.favorite.FavoriteMethodUtils;
import app.music.utils.imageloading.ImageLoadingUtils;
import app.music.utils.intent.IntentMethodUtils;
import app.music.utils.log.InformationLogUtils;
import app.music.utils.musicloading.LoadMusicUtil;
import app.music.utils.musicstate.MusicStateMethodUtils;
import app.music.utils.toast.ToastUtil;

public class DetailPlayMusicActivity
        extends BaseMusicServiceActivity<ActivityDetailPlayMusicBinding>
        implements DetailPlayMusicListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "DetailPlayMusicActivity";
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");
    private RequestOptions mRequestOptions = new RequestOptions().error(R.drawable.bg_main_color);
    private RequestOptions mDiscRequestOptions = new RequestOptions().error(R.drawable.ic_disc);
    private boolean mIsSeeking = false;

    @Inject
//    @RotationObjectAnimator
            ObjectAnimator mObjectAnimator;

    @Override
    protected void onStart() {
        super.onStart();
        bindMusicService();
        mOldLocation = "";
        mOldNotificationState = 0;
        mRunnable = () -> {
            InformationLogUtils.INSTANCE.logRunnableIsRunning(TAG);
            if (mMusicService != null && mMusicService.isRunning) {
                if (mMusicBound && mMusicService.exoPlayer != null) {
                    switch (mMusicService.notificationState) {
                        case -1:
                            if (mOldNotificationState == mMusicService.notificationState) break;
                            if (mMusicService.isPlaying()) {
                                pauseFragmentAnimation();
                                pausePlayer();
                            } else {
                                loadLastState();
                                setButtonImageResource();
                            }
                            mOldNotificationState = -1;
                            InformationLogUtils.INSTANCE.logNotificationIsNegativeState(TAG);
                            break;
                        case 1:
                            mOldNotificationState = 1;
                            if (!mOldLocation.equals(mMusicService.getPlayingSongLocation())) {
                                mOldLocation = mMusicService.getPlayingSongLocation();
                                updateSongInfo();
                                mOldPlayerState = !mMusicService.isPlaying();
                            }
                            if (mOldPlayerState != mMusicService.isPlaying()) {
                                if (mMusicService.isPlaying()) {
                                    resumeFragmentAnimation();
                                } else {
                                    pauseFragmentAnimation();
                                }
                                setButtonImageResource();
                                mOldPlayerState = mMusicService.isPlaying();
                            }
                            long currentPosition = mMusicService.getPlayerCurrentPosition();
                            Log.i("currentPosition=", currentPosition + "");
                            setSeekBarProgress(currentPosition);
                            InformationLogUtils.INSTANCE.logNotificationIsPositiveState(TAG);
                            break;
                    }
                }
                mHandler.postDelayed(mRunnable, 1000);
            } else {
                InformationLogUtils.INSTANCE.logServiceIsNotStarted(TAG);
//                        mAboutDataListener.onDataReceived(2);
                loadLastState();
                //fail here because music service is null
                setReplayAndShuffleImageResource();
            }
        };
        setCountTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mMusicConnection);
    }

    @Override
    public void logServiceConnected() {
        InformationLogUtils.INSTANCE.logServiceConnected(TAG);
    }

    @Override
    public void logServiceDisconnected() {
        InformationLogUtils.INSTANCE.logServiceDisconnected(TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_play_music;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getOptionMenuId() {
        return R.menu.activity_detail_play_music;
    }

    @Override
    protected void createOptionMenu(Menu menu) {

    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.option_menu:
                ToastUtil.INSTANCE.showToast("menu clicked");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        bindMusicService();
        setSupportActionBar(mBinding.toolbarTitle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setElevation(0);
        setCoverArtAnimation();

        assignViews(mBinding.btnShuffle, mBinding.btnPrev, mBinding.btnPlay, mBinding.btnNext,
                mBinding.btnReplay, mBinding.imageLyrics, mBinding.imagePlayingList,
                mBinding.imageFavorite, mBinding.imageMusicVideo, mBinding.imageVolume);
        mBinding.songSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                changeExoPlayerState();
                break;
            case R.id.imageLyrics:
                ToastUtil.INSTANCE.showToast("imageLyrics click");
                break;
            case R.id.imagePlayingList:
                ToastUtil.INSTANCE.showToast("imagePlayingList click");
                break;
            case R.id.imageFavorite:
                if (mMusicService != null && mMusicService.isRunning) {
                    editFavoriteList(mMusicService.getPlayingSong());
                } else {
                    for (BaseMusik music : mLastMusicList) {
                        if (music.getLocation().equals(mLastPlayedMusicObject.getLocation())) {
                            editFavoriteList(music);
                        }
                    }
                }
                break;
            case R.id.imageMusicVideo:
                BaseMusik music;
                if (mMusicService != null && mMusicService.isRunning) {
                    music = mMusicService.getPlayingSong();
                } else {
                    music = mLastPlayedMusicObject;
                }
                if (music == null) break;
                String musicType = music.getType();
                if (TextUtils.isEmpty(musicType)) break;
                switch (musicType) {
                    case ConstantUtil.OFFLINE_MUSIC:
                        ToastUtil.INSTANCE.showToast("MV is not available for this song");
                        break;
                    case ConstantUtil.ONLINE_MUSIC:
                        String mvLink = ((OnlineMusik) music).getMvLink();
                        if (!TextUtils.isEmpty(mvLink)) {
                            if (mMusicService != null && mMusicService.isRunning) {
                                mMusicService.stopForeground();
                            }
                            IntentMethodUtils.INSTANCE.launchDetailPlayMvActivity(this, music);
                        } else {
                            ToastUtil.INSTANCE.showToast("MV is not available for this song");
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.imageVolume:
                ToastUtil.INSTANCE.showToast("imageVolume click");
                break;
            case R.id.btn_prev:
                if (mMusicService.isRunning) {
                    startService();
                    playPrevSong();
                    mMusicService.setPlayerShuffleMode();
                    mMusicService.setPlayerRepeatMode();
                    setReplayAndShuffleImageResource();
                } else {
                    mMusicService.setList(mLastMusicList);
                    mMusicService.setSong(mLastPlayedMusicObject);
                    mMusicService.playPrev();
                    startService();
                    mMusicService.playSong();
                    setCountTime();
                    mMusicService.setPlayerShuffleMode();
                    mMusicService.setPlayerRepeatMode();
                    setReplayAndShuffleImageResource();
                }
                break;
            case R.id.btn_next:
                if (mMusicService.isRunning) {
                    startService();
                    playNextSong();
                    mMusicService.setPlayerShuffleMode();
                    mMusicService.setPlayerRepeatMode();
                    setReplayAndShuffleImageResource();
                } else {
                    mMusicService.setList(mLastMusicList);
                    mMusicService.setSong(mLastPlayedMusicObject);
                    mMusicService.playNext();
                    startService();
                    mMusicService.playSong();
                    setCountTime();
                    mMusicService.setPlayerShuffleMode();
                    mMusicService.setPlayerRepeatMode();
                    setReplayAndShuffleImageResource();
                }
                break;
            case R.id.btn_shuffle:
                mMusicService.shuffleState = !mMusicService.shuffleState;
                mMusicService.replayState = false;
                if (mMusicService.isRunning) {
                    mMusicService.setPlayerShuffleMode();
                }
                setReplayAndShuffleImageResource();
//                else {
//                    if (mMusicService.shuffleState) {
//                        mMusicService.setList(lastMusicList);
//                        mMusicService.songPosition = mMusicService.getRandom();
//                        startService();
//                        mMusicService.playSong();
////                    mOldLocation = -1;
//                        setCountTime();
////                        setPlayerShuffleMode();
//                        setButtonReplayAndShuffleImageResource();
//                    }
//                }
                if (mMusicService.exoPlayer != null) {
                    ToastUtil.INSTANCE.showToast("replayState = " + mMusicService.replayState
                            + "\n exo replayState state = " + mMusicService.exoPlayer.getRepeatMode()
                            + "\n shuffleState = " + mMusicService.shuffleState);
                }
                break;
            case R.id.btn_replay:
                mMusicService.replayState = !mMusicService.replayState;
                mMusicService.shuffleState = false;
                if (mMusicService.isRunning) {
                    mMusicService.setPlayerRepeatMode();
                }
                setReplayAndShuffleImageResource();
//                else {
//                    if (mMusicService.replayState) {
//                        mMusicService.setList(lastMusicList);
//                        mMusicService.songPosition = lastSongPosition;
//                        startService();
//                        mMusicService.mSeekTime = mBinding.songSeekBar.getProgress();
//                        mMusicService.playSong();
//                        setCountTime();
//                        setButtonReplayAndShuffleImageResource();
//                    }
//                }
                if (mMusicService.exoPlayer != null) {
                    ToastUtil.INSTANCE.showToast("replayState = " + mMusicService.replayState
                            + "\n exo replayState state = " + mMusicService.exoPlayer.getRepeatMode()
                            + "\n shuffleState = " + mMusicService.shuffleState);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mBinding.txtTimeCount.setText(mSimpleDateFormat.format(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mIsSeeking = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mIsSeeking = false;
        switch (seekBar.getId()) {
            case R.id.song_seek_bar:
                if (mMusicService == null || !mMusicService.isRunning || !mMusicBound) break;
                mMusicService.seek(mBinding.songSeekBar.getProgress());
                break;
        }

    }

    @Override
    public void changePlayButtonImageResource(int resId) {
        mBinding.btnPlay.setImageResource(resId);
    }

    @Override
    public void changeExoPlayerState() {
        if (mMusicService == null) return;
        if (mMusicService.isRunning) {
            if (!mMusicBound || mMusicService.exoPlayer == null) return;
            if (mMusicService.isPlaying()) {
                pauseFragmentAnimation();
                pausePlayer();
//                                mHandler.removeCallbacksAndMessages(null);
            } else {
                startService();
                resumeFragmentAnimation();
                playPlayer();
//                                mHandler.postDelayed(mRunnable, 500);
            }
        } else {
            InformationLogUtils.INSTANCE.logServiceIsNotRunning(TAG);
            mMusicService.setList(mLastMusicList);
            mMusicService.setSong(mLastPlayedMusicObject);
            startService();
            mMusicService.seekTime = mBinding.songSeekBar.getProgress();
            mMusicService.playSong();
//                mMusicService.seek(mBinding.songSeekBar.getProgress());
            mOldLocation = "";
            setCountTime();
            mMusicService.setPlayerShuffleMode();
            mMusicService.setPlayerRepeatMode();
            setReplayAndShuffleImageResource();
        }
    }

    private void assignViews(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    private void setSeekBarProgress(long barProgress) {
        if (!mIsSeeking) {
            mBinding.songSeekBar.setProgress((int) barProgress);
        }
    }

    private void pauseFragmentAnimation() {
        mObjectAnimator.pause();
    }

    private void resumeFragmentAnimation() {
        mObjectAnimator.resume();
    }

    private void pausePlayer() {
        mMusicService.exoPlayer.setPlayWhenReady(false);
        mBinding.btnPlay.setImageResource(R.drawable.ic_play_circle_outline_white_60dp);
    }

    private void playPlayer() {
        mMusicService.addPlayerListener();
        mMusicService.exoPlayer.setPlayWhenReady(true);
        mBinding.btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_60dp);
    }

    private void loadLastState() {
        long lastCountTime;
        List<BaseMusik> dataList = new ArrayList<>(
                MusicStateMethodUtils.INSTANCE.getLastPlayedMusicList(new WeakReference<>(this)));
        if (dataList.size() > 0) {
            mLastMusicList = new ArrayList<>(dataList);
            mLastPlayedMusicObject = MusicStateMethodUtils.INSTANCE.getLastPlayedSong(
                    new WeakReference<>(this));
            if (mLastPlayedMusicObject == null) {
                mLastPlayedMusicObject = LoadMusicUtil.sMusicList.get(0);
            }
            lastCountTime = MusicStateMethodUtils.INSTANCE.getLastCountTime(new WeakReference<>(this));
        } else {
            mLastMusicList = LoadMusicUtil.sMusicList;
            mLastPlayedMusicObject = LoadMusicUtil.sMusicList.get(0);
            lastCountTime = 0;
        }

        BaseMusik music = mLastPlayedMusicObject;
        setBackgroundAndCoverArt(music);
        updateFavoriteStatus();
        updateLyrics(music);
        int duration = music.getDuration();
        mBinding.txtTimeTotal.setText(mSimpleDateFormat.format(duration));
        mBinding.songSeekBar.setMax(duration);
        setSeekBarProgress(lastCountTime);
        setSongTitle(music);

        pauseFragmentAnimation();
        mBinding.btnPlay.setImageResource(R.drawable.ic_play_circle_outline_white_60dp);
    }

    private void loadDefaultBackGround(ImageView imageView) {
        ImageLoadingUtils.INSTANCE.loadImage(imageView, R.drawable.bg_main_color);
    }

    private void startService() {
        mPlayIntent.setAction(ConstantUtil.ACTION.STARTFOREGROUND_ACTION);
        ContextCompat.startForegroundService(this, mPlayIntent);
    }

    private void updateSongInfo() {
        BaseMusik music = mMusicService.getPlayingSong();
        setBackgroundAndCoverArt(music);
        updateFavoriteStatus();
        updateLyrics(music);
        setTotalTime(music);
        setSongTitle(music);
        setButtonImageResource();
        setFavoriteStatus(music.getLocation());
    }

    private void setCountTime() {
        mHandler.post(mRunnable);
    }

    private void setTotalTime(BaseMusik music) {
        if (music == null) return;
        int duration = music.getDuration();
        mBinding.txtTimeTotal.setText(mSimpleDateFormat.format(duration));
        mBinding.songSeekBar.setMax(duration);
    }

    private void setSongTitle(BaseMusik music) {
        if (music == null) return;
        String musicTitle = music.getTitle();
        String musicArtist = music.getArtist();
        if (TextUtils.isEmpty(musicTitle) || TextUtils.isEmpty(musicArtist)) return;
        mBinding.textSongTitle.setText(music.getTitle());
        mBinding.textArtistName.setText(music.getArtist());
    }

    private void updateLyrics(BaseMusik music) {
        if (music == null) return;
        String lyrics = music.getLyrics();
    }

    private void setBackgroundAndCoverArt(BaseMusik music) {
        if (music == null) return;
        String musicType = music.getType();
        if (TextUtils.isEmpty(musicType)) return;
        switch (musicType) {
            case ConstantUtil.OFFLINE_MUSIC:
                mMetadataRetriever.setDataSource(music.getLocation());
                byte[] bytes = mMetadataRetriever.getEmbeddedPicture();
                if (bytes != null) {
                    BlurImageUtils.INSTANCE.blurImage(
                            this, mCompositeDisposable, bytes, mBinding.imgBackground);
                } else {
                    loadDefaultBackGround(mBinding.imgBackground);
                }
                ImageLoadingUtils.INSTANCE.loadImage(mBinding.imgCenterCoverArt, bytes, mDiscRequestOptions);
                break;
            case ConstantUtil.ONLINE_MUSIC:
                String coverArtLink = ((OnlineMusik) music).getCoverArt();
                if (!TextUtils.isEmpty(coverArtLink)) {
                    BlurImageUtils.INSTANCE.loadAndBlurImageUsingCoil(
                            mBinding.imgBackground, mBinding.imgCenterCoverArt, coverArtLink,
                            mRequestOptions, this, mCompositeDisposable);
                } else {
                    loadDefaultBackGround(mBinding.imgBackground);
                    ImageLoadingUtils.INSTANCE.loadImageUsingCoil(mBinding.imgCenterCoverArt, coverArtLink, mDiscRequestOptions);
                }
                break;
            default:
                break;
        }
    }

    private void setCoverArtAnimation() {
//        mObjectAnimator = ObjectAnimator.ofFloat(mBinding.imgCenterCoverArt,
//                getString(R.string.rotation_property_name), 0, 360);
//        mObjectAnimator.setDuration(5000);
//        mObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        mObjectAnimator.setInterpolator(new LinearInterpolator());
//        mObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mObjectAnimator.setTarget(mBinding.imgCenterCoverArt);
        mObjectAnimator.start();
    }

    private void setButtonImageResource() {
        mBinding.btnPlay.setImageResource(mMusicService.exoPlayer.getPlayWhenReady()
                ? R.drawable.ic_pause_circle_outline_60dp
                : R.drawable.ic_play_circle_outline_white_60dp);
        setReplayAndShuffleImageResource();
    }

    private void updateFavoriteStatus() {
        if (mMusicService != null && mMusicService.isRunning) {
            setFavoriteStatus(mMusicService.getPlayingSong().getLocation());
        } else {
            for (BaseMusik music : mLastMusicList) {
                if (music.getLocation().equals(mLastPlayedMusicObject.getLocation())) {
                    setFavoriteStatus(music.getLocation());
                }
            }
        }
    }

    private void setFavoriteStatus(String musicLocation) {
        if (TextUtils.isEmpty(musicLocation)) return;
        setFavoriteIcon(false);
        for (BaseMusik musik : LoadMusicUtil.sFavoriteList) {
            if (musik.getLocation().equals(musicLocation)) {
                setFavoriteIcon(true);
                return;
            }
        }
    }

    private void editFavoriteList(BaseMusik musik) {
        boolean isAlreadyAFavoriteSong
                = FavoriteMethodUtils.INSTANCE.editFavoriteList(new WeakReference<>(this), musik);
        setFavoriteIcon(!isAlreadyAFavoriteSong);
    }

    private void setFavoriteIcon(boolean favoriteStatus) {
        mBinding.imageFavorite.setImageResource(favoriteStatus ?
                R.drawable.ic_favorite_selected : R.drawable.ic_favorite_normal);
    }

    private void setReplayAndShuffleImageResource() {
        if (mMusicService == null) return;
        mBinding.btnReplay.setImageResource(
                (mMusicService.replayState) ? R.drawable.ic_repeat_one_song : R.drawable.ic_repeat);
        mBinding.btnShuffle.setImageResource(
                mMusicService.shuffleState ? R.drawable.ic_shuffle_all : R.drawable.ic_shuffle);
    }

    private void playNextSong() {
        if (mMusicService == null || !mMusicBound) return;
        mMusicService.playNext();
        mBinding.btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_60dp);
    }

    private void playPrevSong() {
        if (mMusicService == null || !mMusicBound) return;
        mMusicService.playPrev();
        mBinding.btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_60dp);
    }

    private void checkActivityName() {
//        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//        if (am != null && am.getRunningTasks(1).get(0).topActivity.getClassName()
//                .equals("app.music.ui.screen.detailplaymusic.DetailPlayMusicActivity")) {
//            ToastUtil.showToast("Detail Play Music Activity");
//        }
    }
}
