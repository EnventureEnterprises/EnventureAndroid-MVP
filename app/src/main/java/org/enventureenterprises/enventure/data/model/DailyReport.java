package org.enventureenterprises.enventure.data.model;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/6/17.
 */

public class DailyReport extends RealmObject {
    @PrimaryKey
    private  String name;

    private Double profit;
    private Double total_earned;
    private Double total_spent;
    private Date updated;

    public Double getProfit() {
        return this.profit;
    }

    public Double getTotalEarned()
    {
        return this.total_earned;
    }

    public Double getTotalSpent()
    {
        return this.total_spent;
    }

     public void updateTotalSpent(DateTime d,Realm realm)
     {

     }


    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }

    public void setProfit(Double amount,Item item)
    {
        Double unitcost = item.getTotalCost()/item.getQuantity();
        Double current_profit = this.profit;
        if (current_profit != null) {
            this.profit = amount + current_profit  - unitcost;
        }else{
            this.profit =  amount-unitcost;
        }
    }
    public void setProfit(Double amount){
        this.profit = profit;
    }

    public void updateProfit(DateTime d,Realm realm){
        String day_name = d.toString(DateTimeFormat.mediumDate().withLocale(Locale.getDefault()).withZoneUTC());
        String week_name =  WeeklyReport.getWeekName(d);
        String month_name = d.toString("MMM-Y");

        WeeklyReport wrep = WeeklyReport.byName(realm, WeeklyReport.getWeekName(d));
        MonthlyReport mrep = realm.where(MonthlyReport.class)
                .equalTo("name", d.toString("MMM-Y"))
                .findFirst();

        RealmResults<Item> items = realm.where(Item.class).findAll();
        Double profit = 0.0;

        Double total_earned = 0.0;

        Double total_spent = 0.0;

        for (int i = 0; i<items.size(); i++) {


            Long items_stocked = items.get(i).inventory_updates.where().equalTo("entry_day",day_name).sum("quantity").longValue();
            Double amount_spent = items.get(i).inventory_updates.where().equalTo("entry_day",day_name).sum("amount").doubleValue();
            Long items_sold = items.get(i).sales.where().equalTo("entry_day",day_name).sum("quantity").longValue();
            Double amount_sold = items.get(i).sales.where().equalTo("entry_day",day_name).sum("amount").doubleValue();

            total_earned = total_earned+ amount_sold;

            total_spent = total_spent + amount_spent;


            RealmResults<Entry> purchases = items.get(i).inventory_updates.where().findAll();

            Double sum = 0.0;

            for (int j = 0; j < purchases.size(); j++) {
                Double unitcost = purchases.get(j).getAmount() / purchases.get(j).getQuantity();
                sum += unitcost;
            }
            Double standardized_unitcost = sum / purchases.size();

            profit = profit+amount_sold-(standardized_unitcost*items_sold);


        }

        this.profit = profit;

        this.total_earned = total_earned;

        this.total_spent = total_spent;



    }


    public void setTotalEarned(Double total_earned)
    {
        Double current_earnings = this.total_earned;
        if (current_earnings != null) {
            this.total_earned = total_earned + current_earnings;
        }else{
            this.total_earned =  total_earned;
        }
    }

    public void setTotalSpent(Double total_spent)
    {
        Double current_expenditure = this.total_spent;
        if (current_expenditure != null) {
            this.total_spent = total_spent + current_expenditure;
        }else{
            this.total_spent =  total_spent;
        }
    }

    public Date getUpdated(){
        return this.updated;
    }
    public void setUpdated(Date updated){
        this.updated=updated;
    }

    public static void update(final Transaction transaction) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    //Score score = new Score();
                    //score.setName(winnerName);
                    //score.setTime(finishTime);
                    //bgRealm.copyToRealm(score);
                }
            });
        }
    }


    public static DailyReport byName(Realm realm, String name) {
        return realm.where(DailyReport.class).equalTo("name", name).findFirst();
    }

    public static boolean isDateToday(final @NonNull DateTime dateTime) {
        return dateTime.withZone(DateTimeZone.UTC).withTimeAtStartOfDay()
                .equals(DateTime.now().withTimeAtStartOfDay().withZoneRetainFields(DateTimeZone.UTC));
    }

    public static @NonNull String fullDate(final @NonNull DateTime dateTime) {
        return fullDate(dateTime, Locale.getDefault());
    }

    /**
     * e.g.: Dec 17, 2015.
     */
    public static @NonNull String fullDate(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        try {
            return dateTime.toString(DateTimeFormat.fullDate().withLocale(locale).withZoneUTC());
        } catch (final IllegalArgumentException e) {
            // JodaTime doesn't support the 'cccc' pattern, triggered by fullDate and fullDateTime. See: https://github.com/dlew/joda-time-android/issues/30
            // Instead just return a medium date.
            return mediumDate(dateTime, locale);
        }
    }

    /**
     * e.g.: Dec 17, 2015.
     */
    public static @NonNull String mediumDate(final @NonNull DateTime dateTime) {
        return mediumDate(dateTime, Locale.getDefault());
    }

    /**
     * e.g.: Dec 17, 2015.
     */
    public static @NonNull String mediumDate(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        return dateTime.toString(DateTimeFormat.mediumDate().withLocale(locale).withZoneUTC());
    }

    public static DailyReport getOrCreate(Realm realm,String name){
        DailyReport dr = DailyReport.byName(realm,name);
        if (dr == null){
            dr = new DailyReport();
            dr.setName(name);
        }
        return dr;


    }



}
