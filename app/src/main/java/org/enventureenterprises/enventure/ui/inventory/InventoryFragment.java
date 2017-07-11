package org.enventureenterprises.enventure.ui.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
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

public class InventoryFragment extends BaseFragment {
    public static final String TAG = "inventory_fragment";
    Realm realm;
    ItemAdapter  mInventoryAdapter;

    @BindView(R.id.recycler_view)
    RealmRecyclerView mRecyclerView;

    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inventory_fragment, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance ();
        RealmResults<Item> mItems =
                realm.where(Item.class).findAllSorted("created", Sort.DESCENDING);

        mInventoryAdapter = new ItemAdapter(getActivity(), mItems, true, true);

        mRecyclerView.setAdapter(mInventoryAdapter);



        return view;
    }
}
