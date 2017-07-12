package org.enventureenterprises.enventure.ui.addItem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.general.HomeActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static org.enventureenterprises.enventure.R.id.items_in_stock;

/**
 * Created by mossplix on 7/10/17.
 */

public class ItemDetail extends BaseActivity{
    private Long item_id;
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



        setContentView(R.layout.itemdetails_activity);
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

        realm = Realm.getDefaultInstance ();
        actionBar = getSupportActionBar ();

        item = realm.where(Item.class).equalTo ("created_ts",item_id).findFirst ();
        productName.setText(item.getName());

        totalStock.setText(item.getTotalCost().toString());
        quantityInStock.setText(item.getQuantity());

        if(item != null){


            actionBar.setTitle (item.getName ());
            Glide.with(ItemDetail.this).load(new File(Uri.parse(item.getImage()).getPath())).placeholder(new ColorDrawable(Color.GRAY)).into(itemImage);
        }






    }


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }


    @OnClick(R.id.delete_product)
    public void delete_product()
    {


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Item> result = realm.where(Item.class).equalTo("created_ts",item.getCreatedTs()).findAll();
                result.deleteAllFromRealm();
                final Intent intent = new Intent(ItemDetail.this, HomeActivity.class);
                startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
            }
        });

    }

    @OnClick(R.id.edit_product)
    public void edit_product()
    {

    }

}
