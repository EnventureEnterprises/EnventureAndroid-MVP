package org.enventureenterprises.enventure.ui.sales;

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
import org.enventureenterprises.enventure.data.model.Entry;
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
 * Created by mossplix on 7/7/17.
 */
public class SalesAdapter extends RealmBasedRecyclerViewAdapter<Entry, SalesAdapter.EntryViewHolder> {


    private
    @ApplicationContext
    Activity mContext;


    private Realm realm;

    @Inject
    EnventureApi client;



    @Inject
    public SalesAdapter(@ApplicationContext Activity context,
                         RealmResults<Entry> realmResults,
                         boolean automaticUpdate,
                         boolean animateIdType


    ) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;

    }


    public Entry getEntry(Integer position) {
        return realmResults.get(position);
    }

    @Override
    public SalesAdapter.EntryViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry, parent, false);

        return new SalesAdapter.EntryViewHolder(itemView);
    }

    @Override
    public void onBindRealmViewHolder(SalesAdapter.EntryViewHolder holder, int position) {
        final Entry entry = realmResults.get(position);
        //holder.hexColorView.setBackgroundColor(Color.parseColor(entry.profile.hexColor));


        holder.itemTextView.setText(String.format("%s",
                entry.getItem()));

        holder.quantityTextView.setText(String.format("%s",
                entry.getQuantity()));

        holder.amountTextView.setText(String.format("%s",
                entry.getAmount()));


        Glide.with(mContext).load(entry.getItem().getImage()).asBitmap().placeholder(new ColorDrawable(Color.GRAY)).transform(new CircleTransform(mContext)).into(holder.itemImage);


    }








    public class EntryViewHolder extends RealmViewHolder {


        @BindView(R.id.name)
        TextView itemTextView;


        @BindView(R.id.amount)
        TextView amountTextView;

        @BindView(R.id.quantity)
        TextView quantityTextView;

        @BindView(R.id.item_image)
        ImageView itemImage;




        public EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }




}
