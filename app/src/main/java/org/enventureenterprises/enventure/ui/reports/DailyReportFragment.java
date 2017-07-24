package org.enventureenterprises.enventure.ui.reports;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.DailyReport;
import org.enventureenterprises.enventure.lib.RealmRecyclerView;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.inventory.ItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by mossplix on 7/7/17.
 */

public class DailyReportFragment  extends Fragment {

    Realm realm;
    ItemAdapter mInventoryAdapter;

    @BindView(R.id.recycler_view)
    RealmRecyclerView mRecyclerView;


    @BindView(R.id.pager)
    ViewPager vpager;
    @BindView(R.id.title)
    TextView title;

    RealmResults<DailyReport> mReports;


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

        realm = Realm.getDefaultInstance ();
        mReports =
                realm.where(DailyReport.class).findAllSorted("updated", Sort.DESCENDING);

        vpager.setAdapter(new DayAdapter(getContext(),mReports));

        if(vpager.getAdapter().getCount()>0) {
            title.setText(mReports.get(vpager.getCurrentItem()).getName());
        }

        return view;
    }


    //@OnClick(R.id.back)
    void back()
    {
        if (vpager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

        } else {
            // Otherwise, select the previous step.
            vpager.setCurrentItem(vpager.getCurrentItem() - 1);
        }
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

    @OnClick(R.id.forward)
    public void forward()
    {
        int prev = vpager.getCurrentItem() - 1;
        if (prev>=0 && prev <= vpager.getAdapter().getCount()) {
            vpager.setCurrentItem(prev);
            title.setText(mReports.get(prev).getName());
        }


    }

    @OnClick(R.id.back)
    public void prev(){
        int ff = vpager.getCurrentItem() + 1;
        if(ff<vpager.getAdapter().getCount())
        {
            vpager.setCurrentItem(ff);
            title.setText(mReports.get(ff).getName());
        }

    }








}



