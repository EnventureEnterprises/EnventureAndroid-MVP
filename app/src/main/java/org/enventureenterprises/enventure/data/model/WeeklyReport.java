package org.enventureenterprises.enventure.data.model;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/6/17.
 */

public class WeeklyReport extends RealmObject {
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

    public static WeeklyReport byName(Realm realm, String name) {
        return realm.where(WeeklyReport.class).equalTo("name", name).findFirst();
    }


    public static WeeklyReport getOrCreate(Realm realm,String name){
        WeeklyReport wr = WeeklyReport.byName(realm,name);
        if (wr == null){
            wr = new WeeklyReport();
            wr.setName(name);
        }
        return wr;


    }

    public void updateProfit(Realm realm,Integer entry_week){
        Double total_earned = realm.where(Entry.class).equalTo("transaction_type","sale").equalTo("entry_week",entry_week).sum("amount").doubleValue();
        Long total_items_sold = realm.where(Entry.class).equalTo("transaction_type","sale").equalTo("entry_week",entry_week).sum("quantity").longValue();

        this.profit = profit;
    }


    public void updateTotalEarned(Realm realm,Integer entry_week)
    {
        Double total_earned = realm.where(Entry.class).equalTo("transaction_type","sale").equalTo("entry_week",entry_week).sum("amount").doubleValue();

        this.total_earned =  total_earned;

    }


    public static String getWeekName(DateTime d){
        DateTime start  = new DateTime().withWeekOfWeekyear(d.getWeekOfWeekyear());
        DateTime end  = new DateTime().withWeekOfWeekyear(d.getWeekOfWeekyear() + 1);
        return String.format("%s to %s",start.toString("dd-M-YY"),end.toString("dd-M-YY"));

    }

    public static String  getRelativeDate(DateTime date){


        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendWeeks().appendSuffix(" weeks ago\n")
                .printZeroNever()
                .toFormatter();

        /*

        PeriodFormatter yearsAndMonths = new PeriodFormatterBuilder()
     .printZeroAlways()
     .appendYears()
     .appendSuffix(" year", " years")
     .appendSeparator(" and ")
     .printZeroRarelyLast()
     .appendMonths()
     .appendSuffix(" month", " months")
     .toFormatter();
         */

        return formatter.print(new Period(date).normalizedStandard());

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


            Long items_stocked = items.get(i).inventory_updates.where().equalTo("entry_week",week_name).sum("quantity").longValue();
            Double amount_spent = items.get(i).inventory_updates.where().equalTo("entry_week",week_name).sum("amount").doubleValue();
            Long items_sold = items.get(i).sales.where().equalTo("entry_week",week_name).sum("quantity").longValue();
            Double amount_sold = items.get(i).sales.where().equalTo("entry_week",week_name).sum("amount").doubleValue();

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




}
