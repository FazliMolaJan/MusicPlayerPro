package app.music.di.module;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import app.music.di.scope.ActivityScope;
import app.music.viewmodel.HomeActivityViewModel;
import app.music.viewmodel.OnlineHomeActivityViewModel;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
