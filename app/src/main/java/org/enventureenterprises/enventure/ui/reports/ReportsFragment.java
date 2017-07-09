package org.enventureenterprises.enventure.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;

import butterknife.ButterKnife;

/**
 * Created by mossplix on 7/7/17.
 */

public class ReportsFragment extends BaseFragment {
    public static final String TAG = "reports_fragment";


    public static ReportsFragment newInstance() {
        ReportsFragment fragment = new ReportsFragment();
        return fragment;
    }

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.daily_report_fragment, container, false);
        ButterKnife.bind(this, view);



        return view;
    }
}
