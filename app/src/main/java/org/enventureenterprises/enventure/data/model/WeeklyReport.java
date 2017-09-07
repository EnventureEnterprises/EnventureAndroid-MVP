package org.enventureenterprises.enventure.data.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
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
    public void setProfit(Double profit){
        this.profit = profit;
    }


    public void setTotalEarned(Double total_earned)
    {

            this.total_earned =  total_earned;

    }

    public void setTotalSpent(Double total_spent)
    {

            this.total_spent =  total_spent;

    }


    public Date getUpdated(){
        return this.updated;
    }
    public void setUpdated(Date updated){
        this.updated=updated;
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
        Double total_earned = realm.where(Entry.class).equalTo("transaction_type","sales").equalTo("entry_week",entry_week).sum("amount").doubleValue();

        this.total_earned =  total_earned;

    }


    public static String getWeekName(DateTime d){
        DateTime start  = d.withDayOfWeek(DateTimeConstants.MONDAY);
        DateTime end  = d.withDayOfWeek(DateTimeConstants.SUNDAY);
        return String.format("%s to %s",DailyReport.mediumDate(start),DailyReport.mediumDate(end));

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





}
