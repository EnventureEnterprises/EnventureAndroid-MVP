package org.enventureenterprises.enventure.ui.inventory;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
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

public class ItemAdapter extends RealmBasedRecyclerViewAdapter<Item, ItemAdapter.ItemViewHolder> {


    private
    @ApplicationContext
    Activity mContext;


    private Realm realm;

    @Inject
    EnventureApi client;



    @Inject
    public ItemAdapter(@ApplicationContext Activity context,
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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_item, parent, false);

        return new ItemAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindRealmViewHolder(ItemAdapter.ItemViewHolder holder, int position) {
        final Item item = realmResults.get(position);
        //holder.hexColorView.setBackgroundColor(Color.parseColor(item.profile.hexColor));


        holder.itemTextView.setText(String.format("%s",
                item.getName()));

        holder.quantityTextView.setText(String.format("%s",
                item.getQuantity()));




        Glide.with(mContext).load(item.getImage()).asBitmap().placeholder(new ColorDrawable(Color.GRAY)).transform(new CircleTransform(mContext)).into(holder.itemImage);


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
        }


    }




}