package app.music.ui.screen.splash

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.music.base.BaseMVVMActivity
import app.music.base.contract.SplashActivityContract
import app.music.databinding.ActivitySplashBinding
import app.music.utils.intent.IntentMethodUtils
import app.music.utils.musicloading.LoadMusicUtil
import app.music.utils.toast.ToastUtil
import app.music.utils.viewmodel.ViewModelUtils
import app.music.viewmodel.SplashActivityViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


/**
 * Created by jacky on 3/23/18.
 */

class SplashActivity
    : BaseMVVMActivity<ActivitySplashBinding, SplashActivityViewModel>(),
        SplashActivityContract.View {

    private var isOnPausing = false
    private var mPermissionRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMusicDoneListener()
        mViewModel.loadMusic()
        //        SharedPrefMethodUtils.deleteAllPlaylists(new WeakReference<>(this));
    }

    override fun onResume() {
        super.onResume()
        requestStoragePermission()
        isOnPausing = false
    }

    override fun onPause() {
        super.onPause()
        isOnPausing = true
    }

    override fun initInject() = activityComponent.inject(this)

    override fun getViewModel() = ViewModelUtils.getViewModel<SplashActivityViewModel>(this)

    override fun getLayoutId() = 0

    override fun getLogTag() = TAG

    override fun getOptionMenuId() = 0

    override fun createOptionMenu(menu: Menu) {

    }

    override fun initView() {

    }

    override fun initData() {

    }

//    override fun onRequestPermissionsResult(
//            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            ConstantUtil.MY_PERMISSION_REQUEST -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        ToastUtil.showToast(getString(R.string.permission_granted))
//                        getListMusic()
//                    }
//                } else {
//                    ToastUtil.showToast(getString(R.string.no_permission_granted))
//                    finish()
//                }
//            }
//        }
//    }

//    private fun checkPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                            this,
//                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                        ConstantUtil.MY_PERMISSION_REQUEST)
//            } else {
//                ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                        ConstantUtil.MY_PERMISSION_REQUEST)
//            }
//        } else {
//            getListMusic()
//            getPlaylistList()
//            getOnlinePlaylistList()
//            getFavoriteList()
//        }
//        requestStoragePermission(this, packageName)
//    }

    fun showPermissionDenied(permission: String, isPermanentlyDenied: Boolean) {
        when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> {

            }
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {

            }
            Manifest.permission.INTERNET -> {

            }
            else -> throw RuntimeException("No feedback view for this permission")
        }
    }

    private fun requestStoragePermission() {
        if (!mPermissionRequested) {
            mPermissionRequested = true
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            var permissionName: String
                            var message: String
//                        if (report.areAllPermissionsGranted()) {
//                            getListMusic()
//                            getPlaylistList()
//                            getOnlinePlaylistList()
//                            getFavoriteList()
//                        } else {
                            for (deniedPermission in report.deniedPermissionResponses) {
                                when (deniedPermission.permissionName) {
                                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                                        permissionName = "STORAGE"
                                        message = "load offline music"
                                        showSettingsDialog(permissionName, message, true)
//                                        requestStoragePermission()
                                    }
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
//                                        message = "to load offline music"
//                                    }
                                    Manifest.permission.INTERNET -> {
                                        permissionName = "Internet"
                                        message = "load online music"
                                    }
                                    Manifest.permission.CAMERA -> {
                                        permissionName = "CAMERA"
                                        message = "take photo"
                                    }
                                }
                            }
                            for (grantedPermission in report.grantedPermissionResponses) {
                                when (grantedPermission.permissionName) {
                                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                                        mViewModel.mStoragePermissionGranted = true
                                    }
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
//                                        message = "to load offline music"
//                                    }
                                    Manifest.permission.INTERNET -> {
                                    }
                                    Manifest.permission.CAMERA -> {
                                    }
                                }
                            }
//                        }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>?,
                                token: PermissionToken?) {
                            token?.continuePermissionRequest()
                        }
                    }).withErrorListener { ToastUtil.showToast("Error occurred! ") }
                    .onSameThread()
                    .check()
        }
    }

    private fun loadMusicDoneListener() {
        LoadMusicUtil.sLoadOnlineMusicStatus = ""
        WorkManager.getInstance()
                .getWorkInfoByIdLiveData(mViewModel.mLoadingMusicDoneWorkRequest.id)
                .observe(
                        this as LifecycleOwner,
                        Observer<WorkInfo> { workInfo ->
                            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                                when (LoadMusicUtil.sLoadOnlineMusicStatus) {
                                    LoadMusicUtil.LOAD_ONLINE_MUSIC_SUCCEED -> {
                                        if (LoadMusicUtil.sMusicList.size > 0) {
                                            if (!isOnPausing) {
                                                IntentMethodUtils.launchHomeActivity(this, true)
                                            }
                                        } else {
                                            ToastUtil.showToast("there's no song in your device")
                                            exitApplication()
                                        }
                                    }

                                    LoadMusicUtil.LOAD_ONLINE_MUSIC_FAILED -> {
                                        if (!isOnPausing) {
                                            ToastUtil.showToast("online music load failed")
                                            IntentMethodUtils.launchHomeActivity(this, true)
                                        }
                                    }
                                }
                            }
                        })
    }

    private fun showSettingsDialog(permissionName: String, message: String,
                                   isMustBeEnabledPermission: Boolean) {
        AlertDialog.Builder(this).run {
            setTitle("Need Permissions")
            setMessage(
                    (if (isMustBeEnabledPermission) "$permissionName permission must be enabled "
                    else "This app needs $permissionName permission ")
                            + "to $message. You can grant permission in APP SETTINGS -> PERMISSION.")
            setPositiveButton("GOTO APP SETTINGS") { dialog, which ->
                dialog.cancel()
                openSettings(packageName)
                mPermissionRequested = false
            }
            if (!isMustBeEnabledPermission) {
                setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
            }
            show()
        }
    }

    private fun openSettings(packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    private fun exitApplication() {
        if (Build.VERSION.SDK_INT in 16..20) {
            finishAffinity()
        } else if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask()
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun showPermissionRationale(context: Context, token: PermissionToken) {

    }

//    fun requestStoragePermission(): Boolean {
//        var storagePermissionGranted = false
//
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(object : PermissionListener {
//                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
//                        storagePermissionGranted = true
//                    }
//
//                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
//                        if (showSettingsDialog(this@SplashActivity, packageName, "Storage", "load offline music")) {
//                            showSettingsDialog(this@SplashActivity, packageName, "Storage", "load offline music")
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
//                        token?.continuePermissionRequest()
//                    }
//                })
//                .check()
//        return storagePermissionGranted
//    }

    companion object {

        private const val TAG = "SplashActivity"
    }
}
