package org.enventureenterprises.enventure.ui.addEntry;

import android.os.Bundle;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.base.BaseActivity;

import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by mossplix on 7/8/17.
 */

public class NewEntryActivity extends BaseActivity {
    Realm realm;
    private Long item_id;
    private String item_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);



        setContentView(R.layout.addentry_activity);
        ButterKnife.bind(this);


        realm = Realm.getDefaultInstance();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                item_id = extras.getLong("item");
                item_name = extras.getString("name");

            }

        } else {
            item_id = savedInstanceState.getLong("item");
            item_name = savedInstanceState.getString("name");
        }

    }
}
