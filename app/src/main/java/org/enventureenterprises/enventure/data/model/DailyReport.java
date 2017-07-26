package org.enventureenterprises.enventure.data.model;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
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
