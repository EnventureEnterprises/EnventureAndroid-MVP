package org.enventureenterprises.enventure.ui.addEntry;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.base.BaseActivity;

import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by mossplix on 7/6/17.
 */


public class SearchItemActivity extends BaseActivity {

    private String mQuery = "";
    private ListView mSearchResults;

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setContentView(R.layout.addentry_activity);
        realm = Realm.getDefaultInstance ();

        realm = Realm.getDefaultInstance();


        adapter = new SearchAdapter(SearchItemActivity.this, realm,"name");
        recyclerView.setAdapter(adapter);
        adapter.filter("!@#()%43()random_result");




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
