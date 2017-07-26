package org.enventureenterprises.enventure.data.model;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/6/17.
 */

public class MonthlyReport extends RealmObject {
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

    public void setProfit(Double profit)
    {
        this.profit = profit;
    }






    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
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


    public static MonthlyReport byName(Realm realm, String name) {
        return realm.where(MonthlyReport.class).equalTo("name", name).findFirst();
    }


    public static MonthlyReport getOrCreate(Realm realm,String name){
        MonthlyReport mr = MonthlyReport.byName(realm,name);
        if (mr == null){
            mr = new MonthlyReport();
            mr.setName(name);
        }
        return mr;


    }






}
