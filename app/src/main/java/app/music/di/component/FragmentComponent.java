package app.music.di.component;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import app.music.di.module.FragmentModule;
import app.music.di.module.FragmentViewModelModule;
import app.music.di.scope.FragmentScope;
import app.music.ui.screen.album.AlbumFragment;
import app.music.ui.screen.album.OnlineAlbumFragment;
import app.music.ui.screen.artist.ArtistFragment;
import app.music.ui.screen.artist.OnlineArtistFragment;
import app.music.ui.screen.folder.FolderFragment;
import app.music.ui.screen.genre.GenreFragment;
import app.music.ui.screen.genre.OnlineGenreFragment;
import app.music.ui.screen.playlist.OnlinePlaylistFragment;
import app.music.ui.screen.playlist.PlaylistFragment;
import app.music.ui.screen.song.OnlineSongFragment;
import app.music.ui.screen.song.SongFragment;
import app.music.viewmodel.HomeActivityViewModel;
import app.music.viewmodel.OnlineHomeActivityViewModel;
import dagger.Component;

@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = {FragmentModule.class, FragmentViewModelModule.class}
)
public interface FragmentComponent {

    Activity getActivity();

    Fragment getFragment();

    HomeActivityViewModel getHomeActivityViewModel();

    OnlineHomeActivityViewModel getOnlineHomeActivityViewModel();

    void inject(AlbumFragment albumFragment);

    void inject(OnlineAlbumFragment onlineAlbumFragment);

    void inject(SongFragment songFragment);

    void inject(OnlineSongFragment onlineSongFragment);

    void inject(ArtistFragment artistFragment);

    void inject(OnlineArtistFragment onlineArtistFragment);

    void inject(GenreFragment genreFragment);

    void inject(OnlineGenreFragment onlineGenreFragment);

    void inject(PlaylistFragment playlistFragment);

    void inject(OnlinePlaylistFragment onlinePlaylistFragment);

    void inject(FolderFragment folderFragment);
}
