package app.music.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.music.R;
import app.music.model.entity.BaseMusik;
import app.music.model.entity.OnlineMusik;
import app.music.ui.screen.detailplaymusic.DetailPlayMusicActivity;
import app.music.utils.ConstantUtil;
import app.music.utils.log.InformationLogUtils;
import app.music.utils.musicloading.LoadMusicUtil;
import app.music.utils.musicstate.MusicStateConstantUtils;
import app.music.utils.musicstate.MusicStateMethodUtils;
import app.music.utils.sharepreferences.SharedPrefMethodUtils;
import app.music.utils.toast.ToastUtil;

import static app.music.base.BaseApplication.CHANNEL_ID;

public class MusicService extends Service {

    private static final String TAG = "MusicService";
    public ExoPlayer exoPlayer;
    public List<BaseMusik> songs = new ArrayList<>();
    private int songPosition = 0;
    public boolean shuffleState = false;
    public boolean replayState = false;
    public int notificationState = -1;
    public long seekTime = 0;
    public boolean isRunning = false;
    private final IBinder mMusicBind = new MusicBinder();
    private Thread mPlayThread;
    private Player.EventListener mPlayerListener;
    private final String NOTIFICATION_SERVICE_TAG = "NotificationService";
    public String playingSongLocation;
    protected Handler mHandler = new Handler();
    private NotificationTarget notificationTarget;
    private RequestOptions mRequestOptions = new RequestOptions()
            .error(R.drawable.ic_album);
    RemoteViews mSmallNotificationLayout;
    RemoteViews mBigNotificationLayout;

//    private Handler mainHandler;
//    private RenderersFactory renderersFactory;
//    private BandwidthMeter bandwidthMeter;
//    private LoadControl loadControl;
//    private DataSource.Factory dataSourceFactory;
//    private ExtractorsFactory extractorsFactory;
//    private MediaSource mediaSource;
//    private TrackSelection.Factory trackSelectionFactory;
//    private SimpleExoPlayer player;
//    private final String streamUrl = "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-einws"; //bbc world service url
//    private TrackSelector trackSelector;

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = false;
        SharedPreferences sharedPreferences
                = SharedPrefMethodUtils.INSTANCE.getSharedPreferences(new WeakReference<>(getApplicationContext()));
        replayState = sharedPreferences.getBoolean(MusicStateConstantUtils.PREF_REPLAY_STATE, false);
        shuffleState = sharedPreferences.getBoolean(MusicStateConstantUtils.PREF_SHUFFLE_STATE, false);
        InformationLogUtils.INSTANCE.logOnCreate(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ConstantUtil.ACTION.STARTFOREGROUND_ACTION:
                    showNotification();
                    notificationState = 1;
                    break;
                case ConstantUtil.ACTION.PREV_ACTION:
                    playPrev();
                    setPlayerShuffleMode();
                    setPlayerRepeatMode();
                    Log.i(NOTIFICATION_SERVICE_TAG, "Clicked Previous");
                    break;
                case ConstantUtil.ACTION.PLAY_ACTION:
                    if (!isPlaying()) {
                        exoPlayer.addListener(mPlayerListener);
                    }
                    exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
                    showNotification();
                    Log.i(NOTIFICATION_SERVICE_TAG, "Clicked Play");
                    break;
                case ConstantUtil.ACTION.NEXT_ACTION:
                    playNext();
                    setPlayerShuffleMode();
                    setPlayerRepeatMode();
                    Log.i(NOTIFICATION_SERVICE_TAG, "Clicked Next");
                    break;
                case ConstantUtil.ACTION.STOPFOREGROUND_ACTION:
                    Log.i(NOTIFICATION_SERVICE_TAG, "Received Stop Foreground Intent");
//                    SharedPreferences.Editor editor = getSharedPreferences(
//                            KeyUtil.PREF_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
//                    editor.putString(KeyUtil.PREF_LAST_MUSIC_LIST,
//                            new Gson().toJson(new LastMusicListContainer(songs)));
//                    editor.putLong(KeyUtil.PREF_LAST_COUNT_TIME, exoPlayer.getCurrentPosition());
//                    editor.putInt(KeyUtil.PREF_LAST_PLAYED_MUSIC_OBJECT, songPosition);
//                    editor.putBoolean(KeyUtil.PREF_REPLAY_STATE, replayState);
//                    editor.putBoolean(KeyUtil.PREF_SHUFFLE_STATE, shuffleState);
//                    editor.apply();

                    stopForeground();
                    //crash the line above
                    break;
            }
        }
        return START_STICKY;
    }

    public void stopForeground() {
        String lastMusicObjectType = "";
        BaseMusik music = getPlayingSong();
        //crash the line above
        if (music != null) {
            String musicType = music.getType();
            if (!TextUtils.isEmpty(musicType)) {
                if (musicType.equals(ConstantUtil.OFFLINE_MUSIC)) {
                    lastMusicObjectType = MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT_TYPE_MUSIC;
                } else {
                    lastMusicObjectType = MusicStateConstantUtils.PREF_LAST_PLAYED_MUSIC_OBJECT_TYPE_ONLINE_MUSIC;
                }
            }
        }
        MusicStateMethodUtils.INSTANCE.saveLastState(
                new WeakReference<>(getBaseContext()),
                songs,
                exoPlayer.getCurrentPosition(),
                getPlayingSong(),
                replayState,
                shuffleState,
                lastMusicObjectType);
        notificationState = -1;
        stopForeground(true);
        stopSelf();
    }

    private void showNotification() {
// Using RemoteViews to bind custom layouts into Notification
        mSmallNotificationLayout = new RemoteViews(getPackageName(), R.layout.status_bar);
        mBigNotificationLayout = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

// showing default song image
        mSmallNotificationLayout.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        mSmallNotificationLayout.setViewVisibility(R.id.status_bar_album_art, View.GONE);
//        mBigNotificationLayout.setImageViewBitmap(R.id.status_bar_album_art,
//                ConstantUtil.getDefaultAlbumArt(this));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(new Intent(
                this, DetailPlayMusicActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

        //enable this code to navigate to HomeActivity
//        Intent notificationIntent = new Intent(this, HomeActivity.class);
//        notificationIntent.setAction(ConstantUtil.ACTION.MAIN_ACTION);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);

        Intent previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction(ConstantUtil.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(ConstantUtil.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(ConstantUtil.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, MusicService.class);
        closeIntent.setAction(ConstantUtil.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        mSmallNotificationLayout.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        mSmallNotificationLayout.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        mSmallNotificationLayout.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        mSmallNotificationLayout.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        mBigNotificationLayout.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        mBigNotificationLayout.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        mBigNotificationLayout.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        mBigNotificationLayout.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        mSmallNotificationLayout.setImageViewResource(R.id.status_bar_play, exoPlayer.getPlayWhenReady() ?
                R.drawable.ic_stat_notify_pause : R.drawable.ic_stat_notify_play);
        mBigNotificationLayout.setImageViewResource(R.id.status_bar_play, exoPlayer.getPlayWhenReady() ?
                R.drawable.ic_stat_notify_pause : R.drawable.ic_stat_notify_play);

        mSmallNotificationLayout.setTextViewText(R.id.status_bar_track_name, getPlayingSong().getTitle());
        mBigNotificationLayout.setTextViewText(R.id.status_bar_track_name, getPlayingSong().getTitle());

        mSmallNotificationLayout.setTextViewText(R.id.status_bar_artist_name, getPlayingSong().getArtist());
        mBigNotificationLayout.setTextViewText(R.id.status_bar_artist_name, getPlayingSong().getArtist());

        mBigNotificationLayout.setTextViewText(R.id.status_bar_album_name, getPlayingSong().getAlbum());

        Notification status = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setCustomContentView(mSmallNotificationLayout)
                .setCustomBigContentView(mBigNotificationLayout)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(ConstantUtil.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
        if (notificationState == -1) {
            startForeground(ConstantUtil.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
        }

        notificationTarget = new NotificationTarget(
                getApplicationContext(),
                R.id.status_bar_album_art,
                mBigNotificationLayout,
                status,
                ConstantUtil.NOTIFICATION_ID.FOREGROUND_SERVICE);

        BaseMusik musik = getPlayingSong();
        changeCoverArt(musik);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        InformationLogUtils.INSTANCE.logOnBind(TAG);
        return mMusicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        InformationLogUtils.INSTANCE.logOnUnbind(TAG);
        SharedPreferences.Editor editor
                = SharedPrefMethodUtils.INSTANCE.getSharedPreferences(new WeakReference<>(getApplicationContext()))
                .edit();
        editor.putBoolean(MusicStateConstantUtils.PREF_REPLAY_STATE, replayState);
        editor.putBoolean(MusicStateConstantUtils.PREF_SHUFFLE_STATE, shuffleState);
        editor.apply();
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        InformationLogUtils.INSTANCE.logOnRebind(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.removeListener(mPlayerListener);
            exoPlayer.release();
            exoPlayer = null;
        }
        isRunning = false;
        mHandler.removeCallbacksAndMessages(null);
        InformationLogUtils.INSTANCE.logOnDestroy(TAG);
    }

    public void initMusicPlayer() {
        stopAndReleaseExoPlayer();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(),
                new DefaultRenderersFactory(getApplicationContext()),
                new DefaultTrackSelector());
    }

    public long getPlayerCurrentPosition() {
        return exoPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return exoPlayer.getPlayWhenReady();
    }

    public void pausePlayer() {
        exoPlayer.setPlayWhenReady(false);
    }

    public void seek(long position) {
        exoPlayer.seekTo(position);
    }

    public void playPlayer() {
        exoPlayer.setPlayWhenReady(true);
//        checkEndSong();
    }

    public void playPrev() {
        if (LoadMusicUtil.sQueueList.size() > 0) {
            ToastUtil.INSTANCE.showToast("about to play - " + LoadMusicUtil.sQueueList.get(0).getTitle() + " - in the queue");
            addQueueBeforePlayingSong();
            songPosition -= 1;
        } else {
            if (shuffleState) {
                songPosition = getRandom();
            } else {
                if (songPosition > 0) {
                    songPosition -= 1;
                } else {
                    songPosition = songs.size() - 1;
                }
            }
        }
        playSong();
    }

    public void playNext() {
        if (LoadMusicUtil.sQueueList.size() > 0) {
            ToastUtil.INSTANCE.showToast("about to play - " + LoadMusicUtil.sQueueList.get(0).getTitle() + " - in the queue");
            addQueueAfterPlayingSong();
            songPosition += 1;
        } else {
            if (shuffleState) {
                songPosition = getRandom();
            } else {
                if (songPosition < songs.size() - 1) {
                    songPosition += 1;
                } else {
                    songPosition = 0;
                }
            }
        }
        playSong();
    }

    public void playSong() {
        setSongData(songs.get(songPosition).getLocation());
    }

    public String getPlayingSongLocation() {
        return playingSongLocation;
    }

    public void addQueueAfterPlayingSong() {
        if (LoadMusicUtil.sQueueList.size() < 1) return;
        songs.add(songPosition + 1, LoadMusicUtil.sQueueList.get(0));
        LoadMusicUtil.sQueueList.remove(0);
    }

    public void addQueueBeforePlayingSong() {
        if (LoadMusicUtil.sQueueList.size() < 1) return;
        songs.add(songPosition - 1, LoadMusicUtil.sQueueList.get(0));
        LoadMusicUtil.sQueueList.remove(0);
    }

    public BaseMusik getPlayingSong() {
        return songs.get(songPosition);
        //crash the line above
    }

    private void changeCoverArt(BaseMusik music) {
        if (music == null) return;
        String musicType = music.getType();
        if (TextUtils.isEmpty(musicType)) return;
        switch (musicType) {
            case ConstantUtil.OFFLINE_MUSIC:
                String songLocation = music.getLocation();
                if (TextUtils.isEmpty(songLocation)) break;
                MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                metadataRetriever.setDataSource(songLocation);
                byte[] bytes = metadataRetriever.getEmbeddedPicture();
                InformationLogUtils.INSTANCE.logCoverArtWasSent(TAG);
                if (bytes != null) {
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(bytes)
                            .apply(mRequestOptions)
                            .into(notificationTarget);
                } else {
                    setDefaultNotificationCoverArt();
                }
                break;
            case ConstantUtil.ONLINE_MUSIC:
                String coverArtLink = ((OnlineMusik) music).getCoverArt();
                if (!TextUtils.isEmpty(coverArtLink)) {
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(((OnlineMusik) music).getCoverArt())
                            .apply(mRequestOptions)
                            .into(notificationTarget);
                } else {
                    setDefaultNotificationCoverArt();
                }
                break;
            default:
                break;
        }
//        profileImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 64, 64, false));
//        bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
    }

    private void setDefaultNotificationCoverArt() {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(R.drawable.ic_album)
                .apply(mRequestOptions)
                .into(notificationTarget);
//        mSmallNotificationLayout.setImageViewResource(R.id.status_bar_album_art, R.drawable.ic_album);
//        mBigNotificationLayout.setImageViewResource(R.id.status_bar_album_art, R.drawable.ic_album);
    }

    private void setSongData(String songLocation) {
        playingSongLocation = songLocation;
        if (mPlayThread != null) {
            mPlayThread.interrupt();
        }
        mPlayThread = null;
        initMusicPlayer();
        exoPlayer.setPlayWhenReady(true);

        ///////////////////////

//        renderersFactory = new DefaultRenderersFactory(getApplicationContext());
//        bandwidthMeter = new DefaultBandwidthMeter();
//        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
//        loadControl = new DefaultLoadControl();

//        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
//        player.addListener(this);

//        exoPlayer = ExoPlayerFactory.newSimpleInstance(
//                new DefaultRenderersFactory(getBaseContext()),
//                new DefaultTrackSelector());

//        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
//        extractorsFactory = new DefaultExtractorsFactory();
//        mainHandler = new Handler();
//
//        mediaSource = new ExtractorMediaSource(Uri.parse(streamUrl),
//                dataSourceFactory,
//                extractorsFactory,
//                mainHandler,
//                null);
//
//        player.prepare(mediaSource);

        ///////////////////////

        mPlayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                exoPlayer.prepare(new ExtractorMediaSource(
                                Uri.parse(songLocation),
                                new DefaultDataSourceFactory(
                                        getBaseContext(),
                                        Util.getUserAgent(getBaseContext(), getString(R.string.app_name)),
                                        null),
                                new DefaultExtractorsFactory(),
                                null,
                                null),
                        true, true);
                if (seekTime > 0) {
                    exoPlayer.seekTo(seekTime);
                    seekTime = 0;
                }
            }
        });
        mPlayThread.start();
        playingSongLocation = songLocation;
        mPlayerListener = new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    playNext();
                }
                if (!playWhenReady) {
                    exoPlayer.removeListener(mPlayerListener);
                }
            }
        };

        exoPlayer.addListener(mPlayerListener);
        showNotification();
        Log.i(TAG, songs.toString());
    }

    public void setPlayerShuffleMode() {
        if (shuffleState) {
            exoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);
        }
    }

    public void setPlayerRepeatMode() {
        exoPlayer.setRepeatMode(replayState ? ExoPlayer.REPEAT_MODE_ONE : ExoPlayer.REPEAT_MODE_OFF);
    }

    public void addPlayerListener() {
        exoPlayer.addListener(mPlayerListener);
    }

    public void stopAndReleaseExoPlayer() {
        if (exoPlayer == null) return;
        exoPlayer.removeListener(mPlayerListener);
        exoPlayer.release();
        exoPlayer = null;
    }

    public void setList(List<? extends BaseMusik> theSongs) {
        songs.clear();
        songs = new ArrayList<>(theSongs);
    }

    public void setSong(BaseMusik music) {
        String lastPlayedSongLocation = music.getLocation();
        for (BaseMusik musik : songs) {
            if (musik.getLocation().equals(lastPlayedSongLocation)) {
                songPosition = songs.indexOf(musik);
                break;
            }
        }
    }

    public int getRandom() {
        int oldPosition = songPosition;
        while (songPosition == oldPosition) {
            songPosition = new Random().nextInt(songs.size());
        }
        return songPosition;
    }

    public class MusicBinder extends Binder {

        public MusicService getService() {
            return MusicService.this;
        }
    }
}
