package org.enventureenterprises.enventure.ui.addItem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.general.HomeActivity;
import org.enventureenterprises.enventure.util.GeneralUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static org.enventureenterprises.enventure.R.id.items_in_stock;

/**
 * Created by mossplix on 7/10/17.
 */

public class ItemDetail extends BaseActivity {
    private String item_name;
    Realm realm;
    private ActionBar actionBar;

    Item item;


    @BindView(R.id.item_image)
    ImageView itemImage;
    @BindView(R.id.product_name)
    TextView productName;

    @BindView(R.id.total_cost)
    TextView totalStock;

    @BindView(items_in_stock)
    TextView quantityInStock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);


        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);


        realm = getRealm();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_details);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                item_name = extras.getString("name");

            }

        } else {
            item_name = savedInstanceState.getString("name");
        }

        realm = Realm.getDefaultInstance();
        actionBar = getSupportActionBar();

        item = realm.where(Item.class).equalTo("name", item_name).findFirst();

        productName.setText(item.getName());


        Long items_stocked = item.getInventories().where().sum("quantity").longValue();
        Long items_sold = item.getSales().where().sum("quantity").longValue();

        Double value_of_purchase = item.getInventories().where().sum("amount").doubleValue();

        RealmResults<Entry> purchases = item.getInventories().where().findAll();
        ArrayList<Double> costPrices = new ArrayList<Double>();

        Double standardized_unitcost = value_of_purchase / items_stocked;

        Long items_in_stock = items_stocked - items_sold;

        Double value_of_stock = standardized_unitcost * items_in_stock;


        totalStock.setText(GeneralUtils.round(value_of_stock).toString());


        quantityInStock.setText(String.format("%s Items in stock",
                items_in_stock));

        if (item != null) {
            // actionBar.setTitle (item.getName ());
            Glide.with(ItemDetail.this).load(item.getImage()).centerCrop().placeholder(R.drawable.ic_no_image_available).into(itemImage);
        }

    }


    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }


    @OnClick(R.id.delete_product)
    public void delete_product() {
        realm = getRealm();


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setEnabled(false);
                realm.copyToRealmOrUpdate(item);
                final Intent intent = new Intent(ItemDetail.this, HomeActivity.class);
                startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
            }
        });

    }

    @OnClick(R.id.edit_product)
    public void edit_product() {
        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("edit", true);
        intent.putExtra("add", false);
        startActivity(intent);

    }

    @OnClick(R.id.add_product)
    public void addItem() {


        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("add", true);
        intent.putExtra("edit", false);
        startActivity(intent);

    }
}
