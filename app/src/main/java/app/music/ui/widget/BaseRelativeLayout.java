package app.music.ui.widget;

import android.content.Context;
import androidx.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import app.music.base.BaseActivity;

/**
 * Created by jacky on 3/24/18.
 */

public class BaseRelativeLayout<T extends ViewDataBinding> extends RelativeLayout {

    protected T dataBinding;

    public BaseRelativeLayout(Context context) {
        super(context);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public BaseActivity getBaseActivity() {
        if (getContext() instanceof BaseActivity) {
            return (BaseActivity) getContext();
        }
        return null;
    }
}
