package app.music.di.module;

import app.music.di.scope.ActivityScope;
import app.music.presenter.SettingActivityPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @ActivityScope
    public SettingActivityPresenter provideSettingActivityPresenter() {
        return new SettingActivityPresenter();
    }
}
