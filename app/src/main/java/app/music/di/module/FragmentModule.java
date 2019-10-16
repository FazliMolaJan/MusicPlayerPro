package app.music.di.module;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;

import app.music.di.scope.FragmentScope;
import app.music.viewmodel.HomeActivityViewModel;
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
    @FragmentScope
    public SimpleDateFormat provideSimpleDateFormat() {
        return new SimpleDateFormat("mm:ss");
    }

    @Provides
    @FragmentScope
    static HomeActivityViewModel provideHomeActivityViewModel(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(HomeActivityViewModel.class);
    }
}
