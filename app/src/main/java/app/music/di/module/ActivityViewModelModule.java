package app.music.di.module;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
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
public class ActivityViewModelModule {

    @Provides
    @ActivityScope
    static ViewModelProvider provideViewModelProvider(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity);
    }

    @Provides
    @ActivityScope
    static HomeActivityViewModel provideHomeActivityViewModel(ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(HomeActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static OnlineHomeActivityViewModel provideOnlineHomeActivityViewModel(ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(OnlineHomeActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static SettingActivityViewModel provideSettingActivityViewModel(ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(SettingActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static SplashActivityViewModel provideSplashActivityViewModel(ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(SplashActivityViewModel.class);
    }

    @Provides
    @ActivityScope
    static ChooseThemeActivityViewModel provideChooseThemeActivityViewModel(ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(ChooseThemeActivityViewModel.class);
    }
}
