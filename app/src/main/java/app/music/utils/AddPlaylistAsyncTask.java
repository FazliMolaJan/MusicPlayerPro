package app.music.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import androidx.viewpager.widget.ViewPager;
import androidx.palette.graphics.Palette;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import app.music.R;
import app.music.model.entity.BaseMusik;
import app.music.model.entity.Music;
import app.music.model.entity.Playlist;
import app.music.model.entity.PlaylistContainer;
import app.music.utils.playlist.PlaylistConstantUtils;
import app.music.utils.toast.ToastUtil;

public class AddPlaylistAsyncTask extends AsyncTask<Music, Void, Boolean> {

    private WeakReference<ViewPager> mViewPagerWeakReference;
    private SharedPreferences mSharedPreferences;
    private MediaMetadataRetriever mMetadataRetriever = new MediaMetadataRetriever();
    private int mWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private SharedPreferences.Editor mEditor;

    public AddPlaylistAsyncTask(SharedPreferences sharedPreferences, ViewPager viewPager) {
        this.mSharedPreferences = sharedPreferences;
        this.mViewPagerWeakReference = new WeakReference<>(viewPager);
    }

    @Override
    protected Boolean doInBackground(Music... music) {
        mMetadataRetriever.setDataSource(music[0].getLocation());
        byte[] bytes = mMetadataRetriever.getEmbeddedPicture();
        if (bytes != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 7;
            Palette palette = Palette
                    .from(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options))
                    .generate();

            GradientDrawable gradientDrawable = new GradientDrawable();
            Context context = mViewPagerWeakReference.get().getContext();
            gradientDrawable.setColors(
                    new int[]{
                            palette.getVibrantColor(palette.getMutedColor(
                                    context.getResources().getColor(
                                            R.color.gradient_background_center_color))
                            ),
                            palette.getDarkVibrantColor(palette.getDarkMutedColor(
                                    context.getResources().getColor(
                                            R.color.gradient_background_start_color))
                            ),
//                                        palette.getLightVibrantColor(palette.getLightMutedColor(
//                                                getResources().getColor(
//                                                        R.color.gradient_background_end_color))
//                                        )
                    }
            );
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BitmapUtils.INSTANCE.convertToBitmap(
                    gradientDrawable,
                    mWidth,
                    context.getResources()
                            .getDimensionPixelSize(R.dimen.fifth_recycler_item_height))
                    .compress(Bitmap.CompressFormat.JPEG, 100, stream);

            String allPlaylistsString = mSharedPreferences.getString(
                    PlaylistConstantUtils.PREF_ALL_PLAYLISTS, "");
            List<Playlist> allPlaylists = new ArrayList<>();
            if (!allPlaylistsString.isEmpty()) {
                allPlaylists = new Gson()
                        .fromJson(allPlaylistsString, PlaylistContainer.class)
                        .getPlaylistList();
            }
            List<BaseMusik> newMusicList = new ArrayList<>();
            newMusicList.add(music[0]);
//            allPlaylists.add(new Playlist(null,
//                    music[0].getTitle(),
//                    newMusicList,
//                    "thumbnail song",
//                    music[0],
//                    music[0],
//                    Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)));

//            SharedPreferences.Editor editor = mSharedPreferences.edit();
//            mEditor = mSharedPreferences.edit();
            mEditor = mSharedPreferences.edit().putString(
                    PlaylistConstantUtils.PREF_ALL_PLAYLISTS, new Gson().toJson(new PlaylistContainer(allPlaylists))
            );
//            editor.apply();
        }
        return mEditor.commit();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Context context = mViewPagerWeakReference.get().getContext();
        ToastUtil.INSTANCE.showToast(aBoolean
                ? context.getString(R.string.add_song_completed)
                : context.getString(R.string.add_song_failed)
        );
        super.onPostExecute(aBoolean);
    }
}
