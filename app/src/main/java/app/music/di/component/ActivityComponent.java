package app.music.di.component;

import android.app.Activity;

import app.music.di.module.ActivityModule;
import app.music.di.module.ViewModelModule;
import app.music.di.scope.ActivityScope;
import app.music.ui.screen.detailalbum.DetailAlbumActivity;
import app.music.ui.screen.detailartist.DetailArtistActivity;
import app.music.ui.screen.detailfavorite.DetailFavoriteActivity;
import app.music.ui.screen.detailfolder.DetailFolderActivity;
import app.music.ui.screen.detailgenre.DetailGenreActivity;
import app.music.ui.screen.detailplaylist.DetailOnlinePlaylistActivity;
import app.music.ui.screen.detailplaylist.DetailPlaylistActivity;
import app.music.ui.screen.detailplaymusic.DetailPlayMusicActivity;
import app.music.ui.screen.home.HomeActivity;
import app.music.ui.screen.onlinemusic.OnlineHomeActivity;
import app.music.ui.screen.setting.ChooseThemeActivity;
import app.music.ui.screen.setting.SettingActivity;
import app.music.ui.screen.splash.SplashActivity;
import app.music.viewmodel.ChooseThemeActivityViewModel;
import app.music.viewmodel.HomeActivityViewModel;
import app.music.viewmodel.SettingActivityViewModel;
import app.music.viewmodel.SplashActivityViewModel;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ViewModelModule.class})
public interface ActivityComponent {

    Activity getActivity();

    HomeActivityViewModel getHomeActivityViewModel();

    SettingActivityViewModel getSettingActivityViewModel();

    SplashActivityViewModel getSplashActivityViewModel();

    ChooseThemeActivityViewModel getChooseThemeActivityViewModel();

    void inject(SplashActivity splashActivity);

    void inject(DetailAlbumActivity detailAlbumActivity);

    void inject(DetailArtistActivity detailArtistActivity);

    void inject(DetailFolderActivity detailFolderActivity);

    void inject(DetailGenreActivity detailGenreActivity);

    void inject(DetailOnlinePlaylistActivity detailOnlinePlaylistActivity);

    void inject(DetailPlaylistActivity detailPlaylistActivity);

    void inject(HomeActivity homeActivity);

    void inject(OnlineHomeActivity onlineHomeActivity);

    void inject(ChooseThemeActivity chooseThemeActivity);

    void inject(SettingActivity settingActivity);

    void inject(DetailFavoriteActivity detailFavoriteActivity);

    void inject(DetailPlayMusicActivity detailPlayMusicActivity);
}
