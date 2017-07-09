package org.enventureenterprises.enventure.ui.addEntry;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.lib.RealmSearchAdapter;
import org.enventureenterprises.enventure.lib.RealmSearchViewHolder;

import io.realm.Realm;


public class SearchAdapter extends RealmSearchAdapter<Item, SearchAdapter.ViewHolder> {

    private @ApplicationContext
    Context context;
    

    public SearchAdapter(
            @ApplicationContext Context context,
            Realm realm,
            String filterColumnName) {
        super(context, realm, filterColumnName);
    }

    public class ViewHolder extends RealmSearchViewHolder {

        private ItemView itemView;

        public ViewHolder(FrameLayout container, TextView footerTextView) {
            super(container, footerTextView);
        }

        public ViewHolder(ItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder vh = new ViewHolder(new ItemView(viewGroup.getContext()));
        return vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Item item = realmResults.get(position);
        viewHolder.itemView.bind(item);
    }

    @Override
    public ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.footer_view, viewGroup, false);
        return new ViewHolder(
                (FrameLayout) v,
                (TextView) v.findViewById(R.id.footer_text_view));
    }

    @Override
    public void onBindFooterViewHolder(ViewHolder holder, int position) {
        super.onBindFooterViewHolder(holder, position);
        final Item item = realmResults.get(position);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext (), NewEntryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("item",item.getId ());
                        context.startActivity (intent);


                    }
                }
        );
    }
}