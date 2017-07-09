package org.enventureenterprises.enventure.ui.reports;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by mossplix on 7/7/17.
 */

public class DailyReportFragment  extends BaseFragment {





    // TODO: Rename and change types and number of parameters
    public static DailyReportFragment newInstance() {
        DailyReportFragment fragment = new DailyReportFragment();
        return fragment;
    }

    public DailyReportFragment() {
        // Required empty public constructor
    }



    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseActivity) getActivity()).getActivityComponent().inject(this);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.daily_report_fragment, container, false);
        ButterKnife.bind(this, view);



        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setEmptyText(R.string.no_);
    }





}
