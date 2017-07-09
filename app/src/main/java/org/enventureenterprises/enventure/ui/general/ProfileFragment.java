package org.enventureenterprises.enventure.ui.general;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.reports.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by mossplix on 7/7/17.
 */

public class ProfileFragment extends BaseFragment {
    public static final String TAG = "profile_fragment";

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);



        return view;
    }

}
