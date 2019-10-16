package app.music.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import app.music.di.component.DaggerFragmentComponent;
import app.music.di.component.FragmentComponent;
import app.music.di.module.FragmentModule;
import app.music.network.DisposableManager;
import app.music.utils.log.InformationLogUtils;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by jacky on 3/5/18.
 */

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    protected T binding;
    protected CompositeDisposable compositeDisposable;
    protected boolean isDestroy;
    private String mLogTag = "BaseFragment";
    protected Boolean mIsVisibleToUser = false;

    @Override
    public void onAttach(Context context) {
        mLogTag = getLogTag();
        InformationLogUtils.INSTANCE.logOnAttach(mLogTag);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        InformationLogUtils.INSTANCE.logOnCreate(mLogTag);
        super.onCreate(savedInstanceState);
        initInject();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        InformationLogUtils.INSTANCE.logOnCreateView(mLogTag);
        compositeDisposable = new CompositeDisposable();
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        InformationLogUtils.INSTANCE.logOnActivityCreated(mLogTag);
        super.onActivityCreated(savedInstanceState);
        initData();
//        getBaseActivity().setTitleApp(getTitleApp());
    }

    @Override
    public void onStart() {
        InformationLogUtils.INSTANCE.logOnStart(mLogTag);
        super.onStart();
    }

    @Override
    public void onResume() {
        InformationLogUtils.INSTANCE.logOnResume(mLogTag);
        super.onResume();
    }

    @Override
    public void onPause() {
        InformationLogUtils.INSTANCE.logOnPause(mLogTag);
        super.onPause();
    }

    @Override
    public void onStop() {
        InformationLogUtils.INSTANCE.logOnStop(mLogTag);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        InformationLogUtils.INSTANCE.logOnDestroyView(mLogTag);
        isDestroy = true;
        if (binding != null) {
            binding.unbind();
        }
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
        DisposableManager.disposeAll();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        InformationLogUtils.INSTANCE.logOnDestroy(mLogTag);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        InformationLogUtils.INSTANCE.logOnDetach(mLogTag);
        super.onDetach();
    }

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(BaseApplication.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
//        return null;
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isResumed()) { // fragment have created
            if (mIsVisibleToUser) {
                onVisible(); // when user switch to another fragment, we can reload data here
            } else {
                onInVisible();
            }
        }
    }

    protected void onVisible() {
        mIsVisibleToUser = true;
    }

    protected void onInVisible() {
        mIsVisibleToUser = false;
    }

    protected void initInject() {

    }

    public abstract int getLayoutId();

    public abstract String getLogTag();

    public abstract void initView();

    public abstract void initData();

    public BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        return null;
    }

    public void replaceFragment(BaseFragment fragment, Bundle mBundle) {
        FragmentTransaction fragmentTransaction
                = getBaseActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(getContainerId(), fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public int getContainerId() {
        return 0;
    }

    public String getTitleApp() {
        return "";
    }
}