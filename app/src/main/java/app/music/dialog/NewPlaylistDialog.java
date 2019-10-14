package app.music.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

import app.music.R;

public class NewPlaylistDialog extends DialogFragment {

    MaterialEditText editText;
    Button buttonCancel, buttonCreate;
    Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_playlist, null);
        builder.setView(view)
                .setTitle("t")
                .setPositiveButton("butoon", (dialog, which) -> {

                })
                .setNegativeButton("but", (dialog, which) -> {

                });
//        editText = view.findViewById(R.id.edit_playlist_name);
//        buttonCancel = view.findViewById(R.id.button_cancel);
//        buttonCreate = view.findViewById(R.id.button_create);
//        buttonCreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showToast("create");
//            }
//        });
        dialog = builder.create();
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }
}
