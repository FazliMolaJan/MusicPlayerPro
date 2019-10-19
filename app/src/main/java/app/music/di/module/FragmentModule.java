package app.music.di.module;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import app.music.di.scope.FragmentScope;
import app.music.viewmodel.HomeActivityViewModel;
import app.music.viewmodel.OnlineHomeActivityViewModel;
import dagger.Module;
import dagger.Provides;


@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }

    @Provides
    @FragmentScope
    public Fragment provideFragment() {
        return fragment;
    }

    @Provides
//    @FragmentScope
    static ViewModelProvider provideViewModelProvider(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity);
    }

    @Provides
//    @FragmentScope
    static HomeActivityViewModel provideHomeActivityViewModel(ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(HomeActivityViewModel.class);
    }

    @Provides
//    @FragmentScope
    static OnlineHomeActivityViewModel provideOnlineHomeActivityViewModel(ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(OnlineHomeActivityViewModel.class);
    }
}
