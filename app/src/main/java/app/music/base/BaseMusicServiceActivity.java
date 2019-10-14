package app.music.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import androidx.databinding.ViewDataBinding;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import app.music.model.BaseMusik;
import app.music.service.MusicService;

public abstract class BaseMusicServiceActivity<T extends ViewDataBinding> extends BaseActivity<T> {

    @Inject
    protected Handler mHandler;
    @Inject
    protected SharedPreferences mSharedPreferences;
    @Inject
    protected MediaMetadataRetriever mMetadataRetriever;

    public MusicService mMusicService;
    protected Intent mPlayIntent;
    protected boolean mMusicBound = false;
    protected String mOldLocation;
    protected int mOldQueuePosition = -1;
    protected Runnable mRunnable;
    protected BaseMusik mLastPlayedMusicObject;
    protected boolean mOldPlayerState;
    protected int mOldNotificationState;
    protected List<? extends BaseMusik> mLastMusicList = new ArrayList<>();
    protected ServiceConnection mMusicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicService = ((MusicService.MusicBinder) service).getService();
            mMusicBound = true;
            logServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
            logServiceDisconnected();
        }
    };

    public abstract void logServiceConnected();

    public abstract void logServiceDisconnected();

    protected void bindMusicService() {
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, MusicService.class);
        }
        bindService(mPlayIntent, mMusicConnection, Context.BIND_AUTO_CREATE);
    }
}
