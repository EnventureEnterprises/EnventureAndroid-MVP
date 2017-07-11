package org.enventureenterprises.enventure.ui.reports;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.ui.addEntry.NewEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.enventureenterprises.enventure.R.id.profit;

/**
 * Created by mossplix on 7/11/17.
 */

public class ReportView extends RelativeLayout

{

    @BindView(profit)
    TextView profitTextView;


    @BindView(R.id.total_earned)
    TextView totalEarned;

    @BindView(R.id.total_spent)
    TextView totalspentText;


    private @ApplicationContext
    Context context;

    public ReportView(@ApplicationContext Context context) {
        super(context);
        init(context);
        context=context;
    }

    private void init(Context context) {
        inflate(context, R.layout.daily_report_fragment, this);
        ButterKnife.bind(this);
    }

    public void bind(final Report report) {


        profitTextView.setText(String.format("%s",
                report.getProfit()));


        totalspentText.setText(String.format("%s",
                report.getTotalSpent()));


        totalEarned.setText(String.format("%s",
                report.getTotalEarned()));

    }

    public void showAddEntryFragment(@NonNull final Long item, String name) {

        Intent intent = new Intent(getContext(), NewEntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", item);
        intent.putExtra("name", name);

        getContext().startActivity(intent);
    }
}
