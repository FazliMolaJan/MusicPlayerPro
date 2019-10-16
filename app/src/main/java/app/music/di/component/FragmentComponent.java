package app.music.di.component;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;

import app.music.di.module.FragmentModule;
import app.music.di.scope.FragmentScope;
import app.music.ui.screen.album.AlbumFragment;
import app.music.viewmodel.HomeActivityViewModel;
import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {

    Activity getActivity();

    Fragment getFragment();

    SimpleDateFormat getSimpleDateFormat();

    HomeActivityViewModel getHomeActivityViewModel();

    void inject(AlbumFragment albumFragment);
}
