package org.enventureenterprises.enventure.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.Logout;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.reports.BaseFragment;
import org.enventureenterprises.enventure.util.PrefUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by mossplix on 7/7/17.
 */

public class ProfileFragment extends BaseFragment {
    public static final String TAG = "profile_fragment";
    protected @Inject
    Logout logout;
    @BindView(R.id.user_number)
    TextView usernumber;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        usernumber.setText(PrefUtils.getMobile(getContext()));


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.sign_out_user)
    public void signOut() {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getString(R.string.alert_sign_out))
                .setPositiveButton("Yes", (dialog, which) -> {
                    logout.execute();
                    final Intent intent = new Intent(getContext(), DispatchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);
                })
                .setNegativeButton("No", null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextSize(18);
                btnPositive.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

                Button btnNegative = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
                btnNegative.setTextSize(18);
                btnNegative.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(24);

    }


}
