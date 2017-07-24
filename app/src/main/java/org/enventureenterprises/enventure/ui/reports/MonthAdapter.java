package org.enventureenterprises.enventure.ui.reports;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.MonthlyReport;
import org.enventureenterprises.enventure.util.GeneralUtils;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mossplix on 7/11/17.
 */

public class MonthAdapter  extends PagerAdapter {

    private Context mContext;
    private Realm realm;
    private RealmResults<MonthlyReport> reports;




    @Inject
    public MonthAdapter(Context context,RealmResults<MonthlyReport> reports) {
        realm = Realm.getDefaultInstance ();
        mContext = context;
        this.reports=reports;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {


        final MonthlyReport report = reports.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.report_fragment, collection, false);


        TextView profit = (TextView)layout.findViewById(R.id.profit);


        TextView totalearned = (TextView)layout.findViewById(R.id.total_earned);
        TextView totalspent = (TextView)layout.findViewById(R.id.total_spent);









        profit.setText(String.format("%s",
                GeneralUtils.round(report.getProfit())));

        totalearned.setText(String.format("%s",
                GeneralUtils.round(report.getTotalEarned())));

        totalspent.setText(String.format("%s",
                GeneralUtils.round(report.getTotalSpent())));


        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);

    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MonthlyReport mr = reports.get(position);


        return mr.getName();
    }

    @Override
    public float getPageWidth(int position) {
        return(1f);
    }







}
