package io.realm;

/**
 * Created by mossplix on 6/20/17.
 */


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.enventureenterprises.enventure.lib.LoadMoreListItemView;

/**
 * ViewHolder used with {@link RealmBasedRecyclerViewAdapter}
 */
public class RealmViewHolder extends RecyclerView.ViewHolder {

    public TextView headerTextView;

    public LoadMoreListItemView loadMoreView;

    public RealmViewHolder(View itemView) {
        super(itemView);
    }

    public RealmViewHolder(TextView headerTextView) {
        super(headerTextView);
        this.headerTextView = headerTextView;
    }

    public RealmViewHolder(LoadMoreListItemView loadMoreView) {
        super(loadMoreView);
        this.loadMoreView = loadMoreView;
    }
}