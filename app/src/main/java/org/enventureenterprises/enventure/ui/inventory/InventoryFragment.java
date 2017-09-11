package org.enventureenterprises.enventure.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.lib.RealmRecyclerView;
import org.enventureenterprises.enventure.ui.addItem.AddItemActivity;
import org.enventureenterprises.enventure.ui.reports.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by mossplix on 7/7/17.
 */

public class InventoryFragment extends BaseFragment {
    public static final String TAG = "inventory_fragment";
    Realm realm;
    ItemAdapter mInventoryAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_inventory_layout)
    View mEmptyInventoryLayout;

    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }

    public InventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home2, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inventory_fragment, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();
        Log.d("", "realm path: " + realm.getPath());


        RealmResults<Item> mItems =
                realm.where(Item.class).equalTo("enabled", true).findAllSorted("created", Sort.DESCENDING);

        if (mItems.isEmpty()) {
            mEmptyInventoryLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyInventoryLayout.setVisibility(View.GONE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mInventoryAdapter = new ItemAdapter(getActivity(), mItems, true, true);
        mRecyclerView.setAdapter(mInventoryAdapter);
        realm.addChangeListener(realm -> {
            RealmResults<Item> items =
                    realm.where(Item.class).equalTo("enabled", true).findAllSorted("created", Sort.DESCENDING);
            if (items.isEmpty()) {
                mEmptyInventoryLayout.setVisibility(View.VISIBLE);
            } else {
                mEmptyInventoryLayout.setVisibility(View.GONE);
            }
        });
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.add_entry:

                startActivity(new Intent(getContext(), AddItemActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshAdapter() {
        RealmResults<Item> items =
                realm.where(Item.class).equalTo("enabled", true).findAllSorted("created", Sort.DESCENDING);
        if (items.isEmpty()) {
            mEmptyInventoryLayout.setVisibility(View.VISIBLE);
        } else {
            mInventoryAdapter = new ItemAdapter(getActivity(), items, true, true);
            mInventoryAdapter = new ItemAdapter(getActivity(), items, true, true);
            mRecyclerView.setAdapter(mInventoryAdapter);
            mEmptyInventoryLayout.setVisibility(View.GONE);
        }
        mInventoryAdapter.notifyDataSetChanged();
    }
}
