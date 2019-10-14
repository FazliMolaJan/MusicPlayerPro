package app.music.di.module;

import android.os.Handler;

import dagger.Module;
import dagger.Provides;

@Module
public class HandlerModule {

    @Provides
    static Handler provideHandler(){
        return new Handler();
    }
}
