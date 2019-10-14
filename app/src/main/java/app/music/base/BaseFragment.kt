package app.music.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import app.music.di.component.FragmentComponent
import app.music.di.module.FragmentModule
import app.music.network.DisposableManager
import app.music.utils.log.InformationLogUtils
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by jacky on 3/5/18.
 */

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var binding: T
    var compositeDisposable: CompositeDisposable? = null
    protected var isDestroy: Boolean = false
    private var mLogTag = "BaseFragment"
    protected var mIsVisibleToUser: Boolean = false
    abstract val layoutId: Int
    abstract val logTag: String

    val baseActivity: BaseActivity<*>?
        get() = if (activity is BaseActivity<*>) {
            activity as BaseActivity<*>?
        } else null

    val containerId: Int
        get() = 0

    val titleApp: String
        get() = ""

    override fun onAttach(context: Context) {
        mLogTag = logTag
        InformationLogUtils.logOnAttach(mLogTag)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        InformationLogUtils.logOnCreate(mLogTag)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initInject()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        InformationLogUtils.logOnCreateView(mLogTag)
        compositeDisposable = CompositeDisposable()
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initView()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        InformationLogUtils.logOnActivityCreated(mLogTag)
        super.onActivityCreated(savedInstanceState)
        initData()
        //        getBaseActivity().setTitleApp(getTitleApp());
    }

    override fun onStart() {
        InformationLogUtils.logOnStart(mLogTag)
        super.onStart()
    }

    override fun onResume() {
        InformationLogUtils.logOnResume(mLogTag)
        super.onResume()
    }

    override fun onPause() {
        InformationLogUtils.logOnPause(mLogTag)
        super.onPause()
    }

    override fun onStop() {
        InformationLogUtils.logOnStop(mLogTag)
        super.onStop()
    }

    override fun onDestroyView() {
        InformationLogUtils.logOnDestroyView(mLogTag)
        isDestroy = true
        binding.unbind()
        compositeDisposable?.run {
            dispose()
            clear()
        }
        DisposableManager.disposeAll()
        super.onDestroyView()
    }

    override fun onDestroy() {
        InformationLogUtils.logOnDestroy(mLogTag)
        super.onDestroy()
    }

    override fun onDetach() {
        InformationLogUtils.logOnDetach(mLogTag)
        super.onDetach()
    }

    protected fun getFragmentComponent(): FragmentComponent? {
//        return DaggerFragmentComponent.builder()
//                .appComponent(BaseApplication.getAppComponent())
//                .fragmentModule(getFragmentModule())
//                .build()
        return null;
    }

    protected fun getFragmentModule(): FragmentModule {
        return FragmentModule(this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        if (isResumed) { // fragment have created
            if (mIsVisibleToUser) {
                onVisible() // when user switch to another fragment, we can reload data here
            } else {
                onInVisible()
            }
        }
    }

    open fun onVisible() {
        mIsVisibleToUser = true
    }

    open fun onInVisible() {
        mIsVisibleToUser = false
    }

    abstract fun initView()

    abstract fun initData()

    open fun initInject() {

    }

    fun replaceFragment(fragment: BaseFragment<*>, mBundle: Bundle) {
        val fragmentTransaction = baseActivity!!.supportFragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            addToBackStack(null)
        }
        fragmentTransaction.commit()
    }
}