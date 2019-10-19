package app.music.utils.musicloading;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.music.R;
import app.music.model.entity.Album;
import app.music.model.entity.Artist;
import app.music.model.entity.BaseMusik;
import app.music.model.entity.Folder;
import app.music.model.entity.Genre;
import app.music.model.entity.Music;
import app.music.model.entity.OnlineMusik;
import app.music.model.entity.OnlinePlaylist;
import app.music.model.entity.OnlinePlaylistContainer;
import app.music.model.entity.Playlist;
import app.music.model.entity.PlaylistContainer;
import app.music.network.APIUtils;
import app.music.utils.ConstantUtil;
import app.music.utils.ListUtils;
import app.music.utils.favorite.FavoriteMethodUtils;
import app.music.utils.playlist.PlaylistConstantUtils;
import app.music.utils.sharepreferences.SharedPrefMethodUtils;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoadMusicUtil {

    public static List<OnlineMusik> sOnlineMusicList = new ArrayList<>();
    public static List<Genre> sOnlineGenreList = new ArrayList<>();
    public static List<Artist> sOnlineArtistList = new ArrayList<>();
    public static List<Album> sOnlineAlbumList = new ArrayList<>();
    public static List<OnlinePlaylist> sOnlinePlaylistList = new ArrayList<>();

    public static String sLoadOnlineMusicStatus = "";
    public static final String LOAD_ONLINE_MUSIC_PROCESSING = "LOAD_ONLINE_MUSIC_PROCESSING";
    public static final String LOAD_ONLINE_MUSIC_FAILED = "LOAD_ONLINE_MUSIC_FAILED";
    public static final String LOAD_ONLINE_MUSIC_SUCCEED = "LOAD_ONLINE_MUSIC_SUCCEED";

    public static List<Music> sMusicList = new ArrayList<>();
    public static List<Genre> sGenreList = new ArrayList<>();
    public static List<Artist> sArtistList = new ArrayList<>();
    public static List<Album> sAlbumList = new ArrayList<>();
    public static List<Playlist> sPlaylistList = new ArrayList<>();
    public static List<Folder> sFolderList = new ArrayList<>();

    public static List<BaseMusik> sQueueList = new ArrayList<>();
    public static List<BaseMusik> sFavoriteList = new ArrayList<>();
    private static Handler loadArtistMusicHandler = new Handler(Looper.getMainLooper());

    private static final String TAG = "LoadMusicUtil";

    public static void getMusic(Context activity) {
        getArtistNameList(activity);
        Log.e(TAG, "getMusicFinish:" + false);
        ListUtils.INSTANCE.clearList(sMusicList);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//        Cursor cursorAlbum = null;
//        String albumCoverPath = "";
        Cursor audioCursor = activity.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Audio.Media.IS_MUSIC + "!='0'",
                        null, null);

        if (audioCursor != null && audioCursor.getCount() > 0 && audioCursor.moveToFirst()) {
            do {
//                albumCoverPath = "";
//                Long albumId = Long.valueOf(audioCursor.getString(audioCursor
//                        .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                String location = audioCursor.getString(
                        audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

//                audioCursor.getString(
//                        audioCursor.getColumnIndex(MediaStore.Audio.Artists.Albums.getContentUri()));

//                String[] from= new String[]{ MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS };
//                int[] to = new int[] { R.id.songname, R.id.rowlength, R.id.rowartist };
//                SimpleCursorAdapter adapter = new SimpleCursorAdapter(activity, R.layout.musicrow, musiccursor, from, to);
//                SongsView.setAdapter(adapter);

//                String data = audioCursor.getString(audioCursor
//                        .getColumnIndex(MediaStore.Audio.Media.DATA));
//                String displayName = audioCursor.getString(audioCursor
//                        .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
//                String folder = data - displayName;

//                cursorAlbum = activity.getContentResolver()
//                        .query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
//                                MediaStore.Audio.Albums._ID + "=" + albumId,
//                                null, null);
//                if (cursorAlbum != null && cursorAlbum.getCount() > 0 && cursorAlbum.moveToFirst()) {
//                    albumCoverPath = cursorAlbum.getString(cursorAlbum
//                            .getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                }
                mediaMetadataRetriever.setDataSource(location);
                String genre = mediaMetadataRetriever
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                if (TextUtils.isEmpty(genre)) {
                    genre = activity.getString(R.string.unknown);
                }
                String year = audioCursor.getString(
                        audioCursor.getColumnIndex(MediaStore.Audio.Media.YEAR));
                if (null == year) {
                    year = "0";
                }
                sMusicList.add(new Music(
                        audioCursor.getString(
                                audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        audioCursor.getString(
                                audioCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
//                        albumCoverPath,
                        year,
                        audioCursor.getString(
                                audioCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                        genre,
                        location,
                        Integer.parseInt(audioCursor.getString(
                                audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))),
//                        audioCursor.getString(audioCursor
//                                .getColumnIndex(MediaStore.Audio.Media.SIZE)),
                        ConstantUtil.OFFLINE_MUSIC,
                        "",
                        audioCursor.getString(
                                audioCursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)),
                        audioCursor.getString(
                                audioCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                        audioCursor.getString(
                                audioCursor.getColumnIndex(MediaStore.Audio.Media.TRACK))
                ));
            } while (audioCursor.moveToNext());
        }
        Log.e(TAG, "getMusicFinish: " + true);
    }

    private static List<String> getArtistNameList(Context context) {
        List<String> artistList = new ArrayList<>();
        String[] projection = new String[]{
//                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
//                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
//                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        };

        Cursor musicCursor = context.getContentResolver()
                .query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, projection, null,
                        null, null);

        // Iterate over the List
        if (musicCursor == null || !musicCursor.moveToFirst()) return artistList;
//        int artistIdIndex = musicCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
        int artistNameIndex = musicCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
//        int numberOfAlbumsIndex = musicCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
//        int numberOfTracksIndex = musicCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);

        do {
//                String artistId = musicCursor.getString(artistIdIndex);
            String artistName = musicCursor.getString(artistNameIndex);
//                String numberOfAlbums = musicCursor.getString(numberOfAlbumsIndex);
//                String numberOfTracks = musicCursor.getString(numberOfTracksIndex);
            if (TextUtils.isEmpty(artistName)) {
                artistName = MediaStore.UNKNOWN_STRING;
            }
            artistList.add(artistName);
        } while (musicCursor.moveToNext());
        return artistList;
    }

    public static void getGenre() {
        ListUtils.INSTANCE.clearList(sGenreList);
        Set<String> genreSet = new HashSet<>();
        for (Music music : sMusicList) {
            genreSet.add(music.getGenre());
        }

        List<String> tempGenreList = new ArrayList<>(genreSet);
        List<Music> tempMusicList = new ArrayList<>();

        for (String genre : tempGenreList) {
            for (Music music : sMusicList) {
                if ((music.getGenre().trim()).equals(genre.trim())) {
                    tempMusicList.add(music);
                }
            }
            sGenreList.add(new Genre(genre, new ArrayList<>(tempMusicList)));
            tempMusicList.clear();
        }
        Log.e(TAG, "getGenreFinish: " + true);
    }

    public static void getArtistWithContext(WeakReference<Context> contextReference) {
        Log.e(TAG, "getArtistStart");
        ListUtils.INSTANCE.clearList(sArtistList);
        addMusicToArtistList(getArtistNameList(contextReference.get()));
    }

    public static void getArtist(Context context, boolean loadAsynchronously) {
        Log.e(TAG, "getArtistStart");
        ListUtils.INSTANCE.clearList(sArtistList);

        List<String> tempArtistList = new ArrayList<>(getArtistNameList(context));
        addMusicToArtistList(tempArtistList);
        loadArtistMusicHandler.removeCallbacksAndMessages(null);
    }

    private static void addMusicToArtistList(List<String> tempArtistList) {
        List<Music> tempMusicList = new ArrayList<>();

        for (String artist : tempArtistList) {
            for (Music music : sMusicList) {
//                Log.i("LoadMusicUtil", music.getTitle());
                if ((music.getArtist().trim()).equals(artist.trim())) {
                    tempMusicList.add(music);
                }
            }

            Set<String> albumSet = new HashSet<>();
            for (Music music : tempMusicList) {
                albumSet.add(music.getAlbum());
            }

            List<String> albumNameList = new ArrayList<>(albumSet);
            sArtistList.add(new Artist(artist, albumNameList,
                    new ArrayList<>(tempMusicList)));
            tempMusicList.clear();
        }
        Log.e(TAG, "getArtistFinish: " + true);
    }

    public static void getAlbum() {
        ListUtils.INSTANCE.clearList(sAlbumList);
        Set<String> albumSet = new HashSet<>();
        for (Music music : sMusicList) {
            albumSet.add(music.getAlbum());
        }

        List<String> tempAlbumList = new ArrayList<>(albumSet);
        List<Music> tempMusicList = new ArrayList<>();

        for (String album : tempAlbumList) {
            for (Music music : sMusicList) {
                if ((music.getAlbum().trim()).equals(album.trim())) {
                    tempMusicList.add(music);
                }
                // crash the line above
            }

            sAlbumList.add(new Album(album, new ArrayList<>(tempMusicList)));
            tempMusicList.clear();
        }
        Log.e(TAG, "getAlbumFinish: " + true);
    }

    public static void getPlaylist(WeakReference<Context> contextReference) {
        List<Playlist> dataList = SharedPrefMethodUtils.INSTANCE.getList(
                contextReference,
                Playlist.class,
                PlaylistContainer.class,
                PlaylistConstantUtils.PREF_ALL_PLAYLISTS);
        if (null != dataList && dataList.size() > 0) {
            sPlaylistList = new ArrayList<>(dataList);
        }
        Log.e(TAG, "getPlaylistFinish: " + true);
    }

    public static void getFolder() {
        ListUtils.INSTANCE.clearList(sFolderList);
        for (Music music : sMusicList) {
            String musicLocation = music.getLocation();
            String fileName = music.getFileName();
            String musicFolder = musicLocation.substring(0, musicLocation.lastIndexOf(fileName));

            boolean isInFolderList = false;
            if (sFolderList.size() > 0) {
                for (Folder folder : sFolderList) {
                    String folderName = folder.getFolderName();
                    if (folderName != null && folderName.equals(musicFolder)) {
                        folder.getMusicList().add(music);
                        isInFolderList = true;
                        break;
                    }
                }
            }
            if (!isInFolderList) {
                List<BaseMusik> musicList = new ArrayList<>();
                musicList.add(music);
                sFolderList.add(new Folder(musicFolder, musicList));
            }
        }
        Log.e(TAG, "getFolderFinish: " + true);
    }

    public static void getOnlineMusic(Retrofit retrofit) {
        sLoadOnlineMusicStatus = LOAD_ONLINE_MUSIC_PROCESSING;
        ListUtils.INSTANCE.clearList(LoadMusicUtil.sOnlineMusicList);
//        IEndPoint iDataService = APIUtils.takeService();
//        Call<List<OnlineMusik>> callOnlineMusic = iDataService.getAllMusic();

        Call<List<OnlineMusik>> callOnlineMusic = (retrofit == null) ?
                APIUtils.INSTANCE.getAllMusicCall() : APIUtils.INSTANCE.getAllMusicCall(retrofit);
        Response<List<OnlineMusik>> responseList;
        if (callOnlineMusic == null) return;
        try {
            responseList = callOnlineMusic.execute();
            if (responseList.body() == null) return;
            handleResponse(new ArrayList<>(responseList.body()));
            sLoadOnlineMusicStatus = LOAD_ONLINE_MUSIC_SUCCEED;
        } catch (IOException e) {
            e.printStackTrace();
            sLoadOnlineMusicStatus = LOAD_ONLINE_MUSIC_FAILED;
        }
    }

    public static void getOnlineMusicAsynchronously(CompositeDisposable compositeDisposable) {

        sLoadOnlineMusicStatus = LOAD_ONLINE_MUSIC_FAILED;

        //enable this code below to load online music
//        sLoadOnlineMusicStatus = LOAD_ONLINE_MUSIC_PROCESSING;
//        ListUtils.INSTANCE.clearList(LoadMusicUtil.sOnlineMusicList);
//        IEndPoint requestInterface = new Retrofit.Builder()
//                .baseUrl(APIUtils.sBaseUrl)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(IEndPoint.class);
//
//        Disposable disposable = requestInterface.testAllOnlineMusic()
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .subscribe(LoadMusicUtil::handleResponse, LoadMusicUtil::handleError, LoadMusicUtil::handleSuccess);
//
//        compositeDisposable.add(disposable);
    }

    private static void handleResponse(List<OnlineMusik> onlineMusikList) {
        if (onlineMusikList.size() > 0) {
            for (OnlineMusik music : onlineMusikList) {
                Log.i(TAG, music.toString());
                LoadMusicUtil.sOnlineMusicList.add(music);
            }
        }
    }

    private static void handleError(Throwable error) {
        sLoadOnlineMusicStatus = LOAD_ONLINE_MUSIC_FAILED;
    }

    private static void handleSuccess() {
        sLoadOnlineMusicStatus = LOAD_ONLINE_MUSIC_SUCCEED;
    }

    public static void getOnlineGenre() {
        ListUtils.INSTANCE.clearList(sOnlineGenreList);
        Set<String> onlineGenreSet = new HashSet<>();
        for (OnlineMusik onlineMusic : sOnlineMusicList) {
            onlineGenreSet.add(onlineMusic.getGenre());
        }

        List<String> tempOnlineGenreList = new ArrayList<>(onlineGenreSet);
        List<OnlineMusik> tempOnlineMusicList = new ArrayList<>();

        for (String onlineGenre : tempOnlineGenreList) {
            for (OnlineMusik onlineMusic : sOnlineMusicList) {
                if ((onlineMusic.getGenre().trim()).equals(onlineGenre.trim())) {
                    tempOnlineMusicList.add(onlineMusic);
                }
            }
            sOnlineGenreList.add(new Genre(onlineGenre, new ArrayList<>(tempOnlineMusicList)));
            tempOnlineMusicList.clear();
        }
        Log.e(TAG, "getOnlineGenreFinish: " + true);
    }

    public static void getOnlineArtist() {
        ListUtils.INSTANCE.clearList(sOnlineArtistList);
        Set<String> artistOnlineSet = new HashSet<>();
        for (OnlineMusik onlineMusic : sOnlineMusicList) {
            artistOnlineSet.add(onlineMusic.getArtist());
        }

        List<String> tempOnlineArtistList = new ArrayList<>(artistOnlineSet);
        List<OnlineMusik> tempOnlineMusicList = new ArrayList<>();

        for (String onlineArtist : tempOnlineArtistList) {
            for (OnlineMusik onlineMusic : sOnlineMusicList) {
                if ((onlineMusic.getArtist().trim()).equals(onlineArtist.trim())) {
                    tempOnlineMusicList.add(onlineMusic);
                }
            }

            Set<String> onlineAlbumSet = new HashSet<>();
            for (OnlineMusik onlineMusic : tempOnlineMusicList) {
                onlineAlbumSet.add(onlineMusic.getAlbum());
            }

            List<String> onlineAlbumNameList = new ArrayList<>(onlineAlbumSet);
            sOnlineArtistList.add(new Artist(onlineArtist, onlineAlbumNameList,
                    new ArrayList<>(tempOnlineMusicList)));
            tempOnlineMusicList.clear();
        }
        Log.e(TAG, "getOnlineArtistFinish: " + true);
    }

    public static void getOnlineAlbum() {
        ListUtils.INSTANCE.clearList(sOnlineAlbumList);
        Set<String> onlineAlbumSet = new HashSet<>();
        for (OnlineMusik onlineMusic : sOnlineMusicList) {
            onlineAlbumSet.add(onlineMusic.getAlbum());
        }

        List<String> tempOnlineAlbumList = new ArrayList<>(onlineAlbumSet);
        List<OnlineMusik> tempOnlineMusicList = new ArrayList<>();

        for (String onlineAlbum : tempOnlineAlbumList) {
            for (OnlineMusik onlineMusic : sOnlineMusicList) {
                if ((onlineMusic.getAlbum().trim()).equals(onlineAlbum.trim())) {
                    tempOnlineMusicList.add(onlineMusic);
                }
            }

            sOnlineAlbumList.add(new Album(onlineAlbum, new ArrayList<>(tempOnlineMusicList)));
            tempOnlineMusicList.clear();
        }
        Log.e(TAG, "getOnlineAlbumFinish:" + true);
    }

    public static void getOnlinePlaylist(WeakReference<Context> contextReference) {
        List<OnlinePlaylist> dataList = SharedPrefMethodUtils.INSTANCE.getList(
                contextReference,
                OnlinePlaylist.class,
                OnlinePlaylistContainer.class,
                PlaylistConstantUtils.PREF_ALL_ONLINE_PLAYLISTS);
        if (null != dataList && dataList.size() > 0) {
            sOnlinePlaylistList = new ArrayList<>(dataList);
        }
        Log.e(TAG, "getOnlinePlaylistFinish: " + true);
    }

    public static void getFavoriteList(WeakReference<Context> contextReference) {
        List<BaseMusik> dataList = FavoriteMethodUtils.INSTANCE.getFavoriteList(contextReference);
        if (null != dataList && dataList.size() > 0) {
            sFavoriteList = new ArrayList<>(dataList);
        }
        Log.e(TAG, "getFavoriteListFinish: " + true);
    }
}
