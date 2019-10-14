package app.music.di.module;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import app.music.di.scope.ActivityScope;
import app.music.viewmodel.ChooseThemeActivityViewModel;
import app.music.viewmodel.HomeActivityViewModel;
import app.music.viewmodel.OnlineHomeActivityViewModel;
import app.music.viewmodel.SettingActivityViewModel;
import app.music.viewmodel.SplashActivityViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

    @Provides
    @ActivityScope
    static HomeActivityViewModel provideHomeActivityViewModel(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity).get(HomeActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static OnlineHomeActivityViewModel provideOnlineHomeActivityViewModel(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity).get(OnlineHomeActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static SettingActivityViewModel provideSettingActivityViewModel(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity).get(SettingActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static SplashActivityViewModel provideSplashActivityViewModel(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity).get(SplashActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static ChooseThemeActivityViewModel provideChooseThemeActivityViewModel(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity).get(ChooseThemeActivityViewModel.class);
    }
}
