package org.enventureenterprises.enventure.ui.addEntry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mossplix on 7/7/17.
 */

public class ItemView extends RelativeLayout

    {

    @BindView(R.id.name)
    TextView nameTextView;
    @BindView(R.id.item_image)
    ImageView itemImage;


    private @ApplicationContext
    Context context;

    public ItemView(@ApplicationContext Context context) {
        super(context);
        init(context);
        context=context;
    }

    private void init(Context context) {
        inflate(context, R.layout.search_item, this);
        ButterKnife.bind(this);
    }

    public void bind(final Item item) {
        nameTextView.setText(String.format("%s",
                item.getName()));

        nameTextView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showAddEntryFragment(item.getId(), item.getName());
                    }
                }
        );




        Glide.with(this.getContext()).load(item.getImage()).placeholder(new ColorDrawable(Color.GRAY)).fitCenter().crossFade().into(itemImage);






        itemImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showAddEntryFragment(item.getId(), item.getName());
                    }
                }
        );


    }

    public void showAddEntryFragment(@NonNull final Long item, String name) {

        Intent intent = new Intent(this.getContext(), NewEntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", item);
        intent.putExtra("name", name);
        this.getContext().startActivity(intent);
    }
}
