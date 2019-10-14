package app.music.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import app.music.R;

public class ConstantUtil {

    public static final int SPAN_COUNT_THREE = 3;
    public static final int MY_PERMISSION_REQUEST = 1;
    public static final int PLAYING_NOTIFICATION_ID = 1;
    public static final float DYNAMIC_BLUR_RADIUS = 20f;

    public static final String ONLINE_MUSIC = "ONLINE_MUSIC";
    public static final String OFFLINE_MUSIC = "OFFLINE_MUSIC";

    public interface ACTION {
        public static String MAIN_ACTION = "customnotification.action.main";
        public static String INIT_ACTION = "customnotification.action.init";
        public static String PREV_ACTION = "customnotification.action.prev";
        public static String PLAY_ACTION = "customnotification.action.play";
        public static String NEXT_ACTION = "customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "customnotification.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_album_art, options);
        return bm;
    }
}
