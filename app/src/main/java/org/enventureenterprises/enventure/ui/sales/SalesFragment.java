package org.enventureenterprises.enventure.ui.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.lib.RealmRecyclerView;
import org.enventureenterprises.enventure.ui.reports.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by mossplix on 7/7/17.
 */

public class SalesFragment extends BaseFragment {
    public static final String TAG = "sales_fragment";
    Realm realm;
    SalesAdapter  mSalesAdapter;


    @BindView(R.id.recycler_view)
    RealmRecyclerView mRecyclerView;

    public static SalesFragment newInstance() {
        SalesFragment fragment = new SalesFragment();
        return fragment;
    }

    public SalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sales_fragment, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance ();
        RealmResults<Entry> mEntries =
                realm.where(Entry.class).findAllSorted("id", Sort.DESCENDING);

       mSalesAdapter = new SalesAdapter(getActivity(), mEntries, true, true);

       mRecyclerView.setAdapter(mSalesAdapter);



        return view;
    }
}
