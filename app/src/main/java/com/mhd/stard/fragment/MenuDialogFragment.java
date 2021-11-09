package com.mhd.stard.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.TodoVo;

public class MenuDialogFragment extends DialogFragment {

    public interface MenuDialogListener {
        public void onDialogItemSelected(int which, int position);
    }
    MenuDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (MenuDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(" must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        int iValue = mArgs.getInt("position");

        TodoVo todoVo = MHDApplication.getInstance().getMHDSvcManager().getTodoVo();
        String title = todoVo.getMsg().get(iValue).getTitle();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(R.array.todo_menu_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        listener.onDialogItemSelected(which, iValue);
                    }
                });
        return builder.create();
    }
}