package org.enventureenterprises.enventure.ui.reports;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.lib.RealmRecyclerView;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.inventory.ItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        RealmResults<Item> mItems =
                realm.where(Item.class).findAllSorted("id", Sort.DESCENDING);

        mInventoryAdapter = new ItemAdapter(getActivity(), mItems, true, true);

        mRecyclerView.setAdapter(mInventoryAdapter);



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
