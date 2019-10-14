package app.music.di.module;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

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
//    @FragmentScope
    static HomeActivityViewModel provideHomeActivityViewModel(Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity).get(HomeActivityViewModel.class);
    }
}
