package app.music.di.component;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Handler;

import javax.inject.Singleton;

import app.music.base.BaseApplication;
import app.music.di.module.AppModule;
import app.music.di.module.HandlerModule;
import app.music.ui.screen.detailartist.DetailArtistActivity;
import app.music.utils.sharepreferences.SharedPrefMethodUtils;
import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, HandlerModule.class})
public interface AppComponent {

    BaseApplication getContext();

    Handler getHandler();

    MediaMetadataRetriever getMediaMetadataRetriever();

    Retrofit getRetrofit();

    ObjectAnimator getObjectAnimator();

    SharedPreferences getSharedPreferences();
}
