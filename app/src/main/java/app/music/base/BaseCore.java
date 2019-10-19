package app.music.base;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jacky on 3/5/18.
 */

public class BaseCore extends AppCompatActivity {

    private ProgressDialog dialog;

    protected void showDialogLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissDialog();
                dialog = new ProgressDialog(BaseCore.this);
                dialog.setMessage("Loading...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    protected void dismissDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null || !dialog.isShowing()) return;
                dialog.dismiss();
            }
        });
    }

    public void initRecyclerViewLinearLayout(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * go to dialog fragment
     */

    public void goToDialogFragment(final BaseDialog baseDialog, final Bundle bundle) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                baseDialog.setArguments(bundle);
                baseDialog.show(fragmentManager, baseDialog.getClass().getName());
            }
        });
    }
}
