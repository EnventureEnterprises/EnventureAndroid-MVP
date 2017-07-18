package org.enventureenterprises.enventure.ui.general;

/**
 * Created by mossplix on 7/18/17.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {
    public static ProgressDialogFragment newInstance(String msg) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putString("msg", msg);

        fragment.setArguments(args);

        return fragment;
    }

    public ProgressDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String msg = getArguments().getString("msg");
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(msg);
        return dialog;
    }
}
