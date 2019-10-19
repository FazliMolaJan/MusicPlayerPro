package app.music.base;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;

import java.lang.ref.WeakReference;

import app.music.di.component.ActivityComponent;
import app.music.di.component.DaggerActivityComponent;
import app.music.di.module.ActivityModule;
import app.music.utils.log.InformationLogUtils;
import app.music.utils.theme.ThemeMethodUtils;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by jacky on 3/5/18.
 */

public abstract class BaseActivity<T extends ViewDataBinding>
        extends AppCompatActivity {

    protected T mBinding;
    public CompositeDisposable mCompositeDisposable;
    private String mLogTag = "BaseActivity";
    private String mBaseThemeName;
    private String mThemeMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLogTag = getLogTag();
        InformationLogUtils.INSTANCE.logOnCreate(mLogTag);
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            mThemeMode = ThemeMethodUtils.INSTANCE.getCurrentThemeMode(new WeakReference(this));
            mBaseThemeName = ThemeMethodUtils.INSTANCE.getCurrentBaseThemeName(new WeakReference(this));
            ThemeMethodUtils.INSTANCE.setAppTheme(this, mBaseThemeName, mThemeMode);
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy
                    = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mCompositeDisposable = new CompositeDisposable();
        //initPlaylistDialog();
        if (getLayoutId() != 0) {
            mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        }
        initInject();
        onViewCreated();
        initView();
        initData();
        //SharedPrefMethodUtils.deleteAllPlaylists(new WeakReference<>(this));
    }

    @Override
    protected void onStart() {
        InformationLogUtils.INSTANCE.logOnStart(mLogTag);
        super.onStart();
    }

    @Override
    protected void onResume() {
        InformationLogUtils.INSTANCE.logOnResume(mLogTag);
        super.onResume();
        if (getLayoutId() != 0) {
            String currentBaseThemeName = ThemeMethodUtils.INSTANCE.getCurrentBaseThemeName(new WeakReference<>(this));
            String currentThemeMode = ThemeMethodUtils.INSTANCE.getCurrentThemeMode(new WeakReference<>(this));
            if (!mBaseThemeName.equals(currentBaseThemeName) || !mThemeMode.equals(currentThemeMode)) {
                recreate();
            }
        }
    }

    @Override
    protected void onPause() {
        InformationLogUtils.INSTANCE.logOnPause(mLogTag);
        super.onPause();
    }

    @Override
    protected void onStop() {
        InformationLogUtils.INSTANCE.logOnStop(mLogTag);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        InformationLogUtils.INSTANCE.logOnDestroy(mLogTag);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId = getOptionMenuId();
        if (0 != menuId) {
            getMenuInflater().inflate(menuId, menu);
            createOptionMenu(menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    protected abstract String getLogTag();

    protected abstract int getOptionMenuId();

    protected abstract void createOptionMenu(Menu menu);

    protected T getBinding() {
        return mBinding;
    }

    public void openActivity(Class<? extends Activity> pClass) {
        openActivity(pClass, null);
    }

    public void openActivity(Class<? extends Activity> pClass, boolean isFinish) {
        openActivity(pClass);
        if (isFinish) {
            finish();
        }
    }

    public void openActivity(Class<? extends Activity> pClass, Bundle bundle) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public <K> Bundle pushBundle(K k) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(k.getClass().getName(), (Parcelable) k);
        return bundle;
    }

    public void replaceFragment(BaseFragment fragment, Bundle bundle) {
        if (bundle != null && fragment != null) fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    public void replaceFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(getContainerId(), fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setTitleApp(fragment.getTitleApp());
    }

    public void replaceFragmentWithoutBackStack(BaseFragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(getContainerId(), fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void setTitleApp(String title) {
    }

    public int getContainerId() {
        return 0;
    }

    public BaseFragment getCurrentBaseFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(getContainerId());
        if (fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        }
        return null;
    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(BaseApplication.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    protected abstract void initInject();

    protected void onViewCreated() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
