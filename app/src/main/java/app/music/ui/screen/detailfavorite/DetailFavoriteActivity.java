package app.music.ui.screen.detailfavorite;

import android.content.Intent;
import android.view.Menu;

import java.util.List;

import app.music.base.BaseSingleRecyclerActivity;
import app.music.model.entity.BaseMusik;
import app.music.utils.log.InformationLogUtils;
import app.music.utils.musicloading.LoadMusicUtil;

public class DetailFavoriteActivity extends BaseSingleRecyclerActivity {

    private static final String TAG = "DetailFavoriteActivity";

    @Override
    protected Object getDataObject(Intent intent) {
        return null;
    }

    @Override
    protected String getToolbarTitle(Object object) {
        return "Favorite Song";
    }

    @Override
    protected List<BaseMusik> getDataList(Object object) {
        return LoadMusicUtil.sFavoriteList;
    }

    @Override
    public void logServiceConnected() {
        InformationLogUtils.INSTANCE.logServiceConnected(TAG);
    }

    @Override
    public void logServiceDisconnected() {
        InformationLogUtils.INSTANCE.logServiceDisconnected(TAG);
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
        getActivityComponent().inject(this);
    }
}
