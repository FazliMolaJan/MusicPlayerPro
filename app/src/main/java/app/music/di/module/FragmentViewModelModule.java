package app.music.di.module;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import app.music.di.scope.FragmentScope;
import app.music.di.scope.FragmentScope;
import app.music.viewmodel.ChooseThemeActivityViewModel;
import app.music.viewmodel.HomeActivityViewModel;
import app.music.viewmodel.OnlineHomeActivityViewModel;
import app.music.viewmodel.SettingActivityViewModel;
import app.music.viewmodel.SplashActivityViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class FragmentViewModelModule {

//    @Provides
//    @FragmentScope
//    static ViewModelProvider provideViewModelProvider(Activity activity) {
//        return ViewModelProviders.of((FragmentActivity) activity);
//    }
//
//    @Provides
//    @FragmentScope
//    static HomeActivityViewModel provideHomeActivityViewModel(ViewModelProvider viewModelProvider) {
//        return viewModelProvider.get(HomeActivityViewModel.class);
//    }

//    @Provides
//    @FragmentScope
//    static OnlineHomeActivityViewModel provideOnlineHomeActivityViewModel(ViewModelProvider viewModelProvider) {
//        return viewModelProvider.get(OnlineHomeActivityViewModel.class);
//    }
}
