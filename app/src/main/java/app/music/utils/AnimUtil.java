package app.music.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import app.music.R;

/**
 * Created by jacky on 3/5/18.
 */

public class AnimUtil {

    public static Animation fadeIn(Context context, final OnAnimationCallBack onAnimationCallBack) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onAnimationCallBack != null) {
                    onAnimationCallBack.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    public interface OnAnimationCallBack {
        void onAnimationEnd();
    }
}
