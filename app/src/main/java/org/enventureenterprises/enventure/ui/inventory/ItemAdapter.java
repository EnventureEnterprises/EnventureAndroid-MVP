package org.enventureenterprises.enventure.ui.inventory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.injection.qualifier.ActivityContext;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.ui.addEntry.NewSaleActivity;
import org.enventureenterprises.enventure.ui.addItem.ItemDetail;
import org.enventureenterprises.enventure.util.CircleTransform;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by mossplix on 7/8/17.
 */

public class ItemAdapter extends RealmBasedRecyclerViewAdapter<Item, ItemAdapter.ItemViewHolder> implements AdapterView.OnItemClickListener {


    private
    @ApplicationContext
    Activity mContext;


    private Realm realm;

    @Inject
    EnventureApi client;

    View itemView;


    @Inject
    public ItemAdapter(@ActivityContext Activity context,
                       RealmResults<Item> realmResults,
                       boolean automaticUpdate,
                       boolean animateIdType


    ) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;

    }


    public Item getItem(Integer position) {
        return realmResults.get(position);
    }

    @Override
    public ItemAdapter.ItemViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_item, parent, false);

        return new ItemAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindRealmViewHolder(ItemAdapter.ItemViewHolder holder, int position) {
        final Item item = realmResults.get(position);
        //holder.hexColorView.setBackgroundColor(Color.parseColor(item.profile.hexColor));


        Long items_stocked = item.getInventories().where().sum("quantity").longValue();
        Long items_sold = item.getSales().where().sum("quantity").longValue();

        Long items_in_stock = items_stocked - items_sold;


        holder.itemTextView.setText(String.format("%s",
                item.getName()));


        holder.quantityTextView.setText(String.format("%s Items",
                items_in_stock));

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), ItemDetail.class);
                        intent.putExtra("name", item.getName());
                        v.getContext().startActivity(intent);


                    }
                }
        );

        holder.quantityTextView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), ItemDetail.class);
                        intent.putExtra("name", item.getName());
                        v.getContext().startActivity(intent);


                    }
                }
        );

        holder.itemTextView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), ItemDetail.class);
                        intent.putExtra("name", item.getName());
                        v.getContext().startActivity(intent);

                    }
                }
        );


        Glide.with(mContext.getApplicationContext()).load(item.getImage()).centerCrop().placeholder(R.drawable.ic_no_image_available).into(holder.itemImage);
        holder.itemImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), ItemDetail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("name", item.getName());
                        v.getContext().startActivity(intent);


                    }
                }
        );
//        Glide.with(mContext).load(item.getImage()).asBitmap().placeholder(new ColorDrawable(Color.GRAY)).into(holder.itemImage);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Item item = realmResults.get(position);
        Intent intent = new Intent(getContext(), NewSaleActivity.class);
        intent.putExtra("name", item.getName());
        getContext().startActivity(intent);


    }


    public class ItemViewHolder extends RealmViewHolder {


        @BindView(R.id.name)
        TextView itemTextView;


        @BindView(R.id.quantity)
        TextView quantityTextView;

        @BindView(R.id.item_image)
        ImageView itemImage;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Item item = realmResults.get(getAdapterPosition());
                            Intent intent = new Intent(v.getContext(), ItemDetail.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("name", item.getName());
                            v.getContext().startActivity(intent);
                        }
                    }
            );


        }


    }


}
