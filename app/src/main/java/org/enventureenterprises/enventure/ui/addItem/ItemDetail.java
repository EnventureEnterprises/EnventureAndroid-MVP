package org.enventureenterprises.enventure.ui.addItem;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.ui.base.BaseActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by mossplix on 7/10/17.
 */

public class ItemDetail extends BaseActivity{
    private Long item_id;
    private String item_name;
    Realm realm;
    private ActionBar actionBar;


    @BindView(R.id.item_image)
    ImageView itemImage;



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

        Item item = realm.where(Item.class).equalTo ("id",item_id).findFirst ();


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

}
