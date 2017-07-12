package org.enventureenterprises.enventure.ui.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
        super.onCreateOptionsMenu(menu,inflater);
    }

    @OnClick(R.id.sign_out_user)
    public void signOut(){
        logout.execute();
        final Intent intent = new Intent(getContext(), DispatchActivity.class);

        getActivity().startActivity(intent);

    }


}
