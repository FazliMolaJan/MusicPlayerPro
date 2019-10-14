package app.music.ui.screen.equalizer;

import android.view.Menu;

import app.music.R;
import app.music.base.BaseActivity;

public class EqualizerActivity extends BaseActivity {

    private static final String TAG = "EqualizerActivity";

    @Override
    public int getLayoutId() {
//        return R.layout.activity_equalizer;
        return R.layout.unused_test_layout;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getOptionMenuId() {
        return 0;
    }

    @Override
    protected void createOptionMenu(Menu menu) {

    }

    @Override
    protected void initInject() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
