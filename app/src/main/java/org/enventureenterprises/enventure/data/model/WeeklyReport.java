package org.enventureenterprises.enventure.data.model;

import org.joda.time.DateTime;
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

    public static String getWeekName(int week){
        DateTime weekStartDate = new DateTime().withWeekOfWeekyear(week);
        DateTime weekEndDate = new DateTime().withWeekOfWeekyear(week + 1);
        return "";

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
