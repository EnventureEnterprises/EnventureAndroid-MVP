package org.enventureenterprises.enventure.ui.sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.lib.RealmRecyclerView;
import org.enventureenterprises.enventure.ui.addEntry.SearchItemActivity;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        realm = Realm.getDefaultInstance ();
        RealmResults<Entry> mEntries =
                realm.where(Entry.class).equalTo("transaction_type","sale").findAllSorted("created", Sort.DESCENDING);

       mSalesAdapter = new SalesAdapter(getActivity(), mEntries, true, true);

       mRecyclerView.setAdapter(mSalesAdapter);



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home2, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.add_entry:

                startActivity(new Intent(getContext(), SearchItemActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
                break;



        }

        return super.onOptionsItemSelected(item);
    }
}

