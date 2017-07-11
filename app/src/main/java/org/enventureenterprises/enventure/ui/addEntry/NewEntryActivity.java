package org.enventureenterprises.enventure.ui.addEntry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.DailyReport;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.model.MonthlyReport;
import org.enventureenterprises.enventure.data.model.WeeklyReport;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by mossplix on 7/8/17.
 */

public class NewEntryActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    Realm realm;
    private Long item_id;
    private String item_name;
    private String paymentType;
    private Item item;
    

    @BindView(R.id.quantity)
    EditText quantityEditText;

    @BindView(R.id.totalcost)
    EditText amountEditText;

    @BindView(R.id.product)
    EditText nameEditText;




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

        nameEditText.setText(item_name);

        Spinner spinner = (Spinner) findViewById(R.id.cash_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.entry_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(NewEntryActivity.this);

        item = realm.where(Item.class).equalTo ("created_ts",item_id).findFirst ();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem nItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = nItem.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.save:
                realm = Realm.getDefaultInstance ();
                realm.beginTransaction();
                DateTime d = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE");
                Entry entry = new Entry ();
                DailyReport drep = realm.where(DailyReport.class)
                        .equalTo("name", fmt.withLocale(Locale.getDefault()).print(d))
                        .findFirst();
                WeeklyReport wrep = realm.where(WeeklyReport.class)
                        .equalTo("name", Integer.toString(d.getWeekOfWeekyear()))
                        .findFirst();
                MonthlyReport mrep = realm.where(MonthlyReport.class)
                        .equalTo("name", d.toString("MMM"))
                        .findFirst();


                if (drep == null) {
                    drep = new DailyReport();
                    drep.setName(fmt.withLocale(Locale.getDefault()).print(d));

                }
                if (wrep == null) {
                    wrep = new WeeklyReport();
                    wrep.setName(Integer.toString(d.getWeekOfWeekyear()));

                }

                if (mrep == null) {
                    mrep = new MonthlyReport();
                    mrep.setName(d.toString("MMM"));

                }

                entry.setName(nameEditText.getText().toString());
                entry.setType(paymentType);
                entry.setItem(item);

                Integer current_quantity = item.getQuantity();
                Double current_total_stock = item.getTotalCost();

                Double unit_cost  =  current_total_stock/current_quantity;

                Double current_value_minus = (Double.parseDouble(quantityEditText.getText().toString())*unit_cost);

                Double  current_value = item.getTotalCost() - current_value_minus;

                item.setTotalCost(current_value);
                item.setQuantity(current_quantity-Integer.parseInt(quantityEditText.getText().toString()));







                entry.setCreated(d.toDate());
                entry.setEntryMonth(d.getDayOfMonth());
                entry.setEntryYear(d.getYear());
                entry.setEntryWeek(d.getWeekOfWeekyear());
                entry.setCreatedTs(d.getMillis());



                mrep.setProfit(Double.parseDouble(amountEditText.getText().toString()),item);
                mrep.setTotalEarned(Double.parseDouble(amountEditText.getText().toString()));
                mrep.setUpdated(d.toDate());


                drep.setProfit(Double.parseDouble(amountEditText.getText().toString()),item);
                drep.setTotalEarned(Double.parseDouble(amountEditText.getText().toString()));
                drep.setUpdated(d.toDate());


                wrep.setProfit(Double.parseDouble(amountEditText.getText().toString()),item);
                wrep.setTotalEarned(Double.parseDouble(amountEditText.getText().toString()));
                wrep.setUpdated(d.toDate());


                entry.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                entry.setAmount(Double.parseDouble(amountEditText.getText().toString()));
                realm.copyToRealmOrUpdate (entry);
                realm.copyToRealmOrUpdate (drep);
                realm.copyToRealmOrUpdate (mrep);
                realm.copyToRealmOrUpdate (wrep);
                realm.copyToRealmOrUpdate (item);




                realm.commitTransaction();



                final Intent intent = new Intent(NewEntryActivity.this, NewEntryActivity.class);
                Toast.makeText(
                        NewEntryActivity.this,
                        "Sale Added Successfully",
                        Toast.LENGTH_LONG)
                        .show();
                startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);

                break;
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                break;


        }

        return super.onOptionsItemSelected(nItem);
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        paymentType = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
