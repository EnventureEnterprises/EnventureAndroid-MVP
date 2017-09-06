package org.enventureenterprises.enventure.ui.addEntry;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.base.BaseActivity;

import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by mossplix on 7/6/17.
 */


public class SearchItemActivity extends BaseActivity implements SearchView.OnQueryTextListener {

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
        setContentView(R.layout.search_item_activity);
        recyclerView = (RecyclerView) findViewById(R.id.search_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        realm = getRealm();
        adapter = new SearchAdapter(this, realm, "name");
        recyclerView.setAdapter(adapter);
        adapter.filter("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(SearchManager.QUERY)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (!TextUtils.isEmpty(query)) {
                //searchFor(query);

                //mSearchView.setQuery(query, false);
            }
        }
    }


    /**
     * On Lollipop+ perform a circular reveal animation (an expanding circular mask) when showing
     * the search panel.
     */


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss(null);
    }

    public void dismiss(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //doExitAnim();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    private void searchFor(String query) {
        adapter.filter(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final SearchView searchView = (SearchView) findViewById(R.id.search_view_toolbar);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        // Set the query hint.
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.clearFocus();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchFor(newText);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
