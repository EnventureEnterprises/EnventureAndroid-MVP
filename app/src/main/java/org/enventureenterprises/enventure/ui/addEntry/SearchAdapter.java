package org.enventureenterprises.enventure.ui.addEntry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.lib.RealmSearchAdapter;
import org.enventureenterprises.enventure.lib.RealmSearchViewHolder;
import org.enventureenterprises.enventure.ui.addItem.ItemDetail;
import org.enventureenterprises.enventure.ui.inventory.ItemAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class SearchAdapter extends RealmSearchAdapter<Item, SearchAdapter.ViewHolder> {

    private @ApplicationContext
    Context mContext;

    @Inject
    EnventureApi client;


    public SearchAdapter(
            @ApplicationContext Context context,
            Realm realm,
            String filterColumnName) {
        super(context, realm, filterColumnName);
        mContext = context;
    }

    public class ViewHolder extends RealmSearchViewHolder {


        @BindView(R.id.name)
        TextView nameTextView;
        @BindView(R.id.item_image)
        ImageView itemImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Item item = realmResults.get(getAdapterPosition());
                            Intent intent = new Intent(v.getContext(), NewSaleActivity.class);
                            intent.putExtra("name", item.getName());
                            v.getContext().startActivity(intent);
                        }
                    }
            );

        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_item, viewGroup, false);

        return new SearchAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        Item item = realmResults.get(position);
        viewHolder.nameTextView.setText(String.format("%s",
                item.getName()));
        if (item.getImage() != null) {
            Glide.with(mContext).load(item.getImage()).placeholder(new ColorDrawable(Color.GRAY)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(viewHolder.itemImage);
        }
    }

    public Item getItem(Integer position) {
        return realmResults.get(position);
    }

}