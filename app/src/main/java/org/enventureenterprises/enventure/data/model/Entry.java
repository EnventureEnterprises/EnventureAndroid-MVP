package org.enventureenterprises.enventure.data.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import retrofit2.http.PUT;

/**
 * Created by mossplix on 7/6/17.
 */

public class Entry extends RealmObject {
    private Long id;

    private Item item;
    private Double amount;

    private Date created;
    private Integer quantity;
    private String type;
    private String user;
    private Account credit;
    private Account debit;
    private Double amount_paid;
    private String customer_mobile;
    private Double total_price;
    private Double amount_paying;
    private Double amount_remaining;


    private boolean synced;

    private String name;
    private Integer entry_year;
    private String entry_month;
    private String entry_week;
    private String entry_day;
    @PrimaryKey
    private Long created_ts;

    private String transaction_type; //inventory, installment payment,sale,installment_addon

    private Transaction transaction;

    private Double sale_value;


    public long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getSaleValue() {
        return this.sale_value;
    }

    public void setSaleValue(Double item_value) {
        this.sale_value = sale_value;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getEntryYear() {
        return this.entry_year;
    }

    public void setEntryYear(Integer entry_year) {
        this.entry_year = entry_year;
    }

    public String getEntryMonth() {
        return this.entry_month;
    }

    public void setEntryMonth(String entry_month) {
        this.entry_month = entry_month;
    }

    public String getEntryWeek() {
        return this.entry_week;
    }

    public void setEntryWeek(String entry_week) {
        this.entry_week = entry_week;
    }

    public String getEntryDay() {
        return this.entry_day;
    }

    public void setEntryDay(String entry_day) {
        this.entry_day = entry_day;
    }

    public Boolean getSynced() {
        return this.synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    public Long getCreatedTs() {
        return this.created_ts;
    }

    public void setCreatedTs(Long created_ts) {
        this.created_ts = created_ts;
    }

    public String getTransactionType() {
        return this.transaction_type;
    }

    public void setTransactionType(String transaction_type) {
        this.transaction_type = transaction_type;
    }


    public Account getDebitAccount() {
        return this.debit;
    }

    public void setDebitAccount(Account account) {
        this.debit = account;
    }


    public Account getCreditAccount() {
        return this.credit;
    }

    public void setCreditAccount(Account account) {
        this.credit = account;
    }


    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }


    public Double getAmountPaid() {
        return this.amount_paid;
    }

    public void setAmountPaid(Double amount) {
        this.amount_paid = amount;
    }

    public String getCustomerMobile() {
        return this.customer_mobile;
    }

    public void setCustomerMobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }


    public Double getTotalPrice() {
        return this.total_price;
    }

    public void setTotalPrice(Double totalprice) {
        this.total_price = totalprice;
    }

    public Double getAmountPaying() {
        return this.amount_paying;
    }

    public void setAmountPaying(Double amount) {
        this.amount_paying = amount_paying;
    }

    public Double getAmountRemaining() {
        return this.amount_remaining;
    }

    public void setAmountremaining(Double amount_remaining) {
        this.amount_remaining = amount_remaining;
    }

    public static void newEntry(Entry entry) {
        Realm realm = Realm.getDefaultInstance();
        if (entry.getTransactionType() != null && entry.getTransactionType().toString().equals("sale")) {
            Item item = realm.where(Item.class).equalTo("name", entry.getItem().getName()).findFirst();
            newSale(item, new DateTime(entry.getCreated()), entry.getType(), entry.getQuantity(), entry.getAmount(), entry.getCustomerMobile(), entry.getTotalPrice(), entry.getAmountPaid(), realm);
        } else {
            realm.beginTransaction();
            DateTime d = new DateTime(entry.getCreated());
            DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE");

            String day_name = d.toString(DateTimeFormat.mediumDate().withLocale(Locale.getDefault()));
            String week_name = WeeklyReport.getWeekName(d);
            String month_name = d.toString("MMM-Y");
            Item item = realm.where(Item.class).equalTo("name", entry.getItem().getName()).findFirst();
            entry.setCreated(d.toDateTime().toDate());
            entry.setCreated(d.toDate());
            entry.setEntryMonth(month_name);
            //entry.setEntryYear(d.);
            entry.setEntryWeek(week_name);
            entry.setEntryDay(day_name);
            realm.copyToRealmOrUpdate(entry);
            realm.commitTransaction();

            realm.beginTransaction();
            item.addInventoryUpdate(entry);
            realm.copyToRealmOrUpdate(item);

            realm.commitTransaction();
        }
    }

    public static Entry newSale(Item item, DateTime d, String paymentType, Integer quantity, Double amount, String customer_mobile, Double total_price, Double amount_paid, Realm realm) {
        Entry toret;
        realm.beginTransaction();

        DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE");
        Entry entry = Entry.getOrCreate(realm, d.getMillis());

        String day_name = d.toString(DateTimeFormat.mediumDate().withLocale(Locale.getDefault()));
        String week_name = WeeklyReport.getWeekName(d);
        String month_name = d.toString("MMM-Y");

        entry.setName(item.getName());
        entry.setType(paymentType);
        entry.setItem(item);

        entry.setCreated(d.toDate());
        entry.setEntryMonth(month_name);
        //entry.setEntryYear(d.);
        entry.setEntryWeek(week_name);
        entry.setEntryDay(day_name);
        //entry.setCreatedTs(d.getMillis());
        entry.setSynced(false);


        String daily_report_name = fmt.withLocale(Locale.getDefault()).print(d);
        String monthly_report_name = d.toString("MMM");
        String weeklyReportName = getWeekName(d.getWeekOfWeekyear());


        DailyReport drep = DailyReport.getOrCreate(realm, daily_report_name);

        WeeklyReport wrep = WeeklyReport.getOrCreate(realm, weeklyReportName);
        MonthlyReport mrep = MonthlyReport.getOrCreate(realm, monthly_report_name);


        switch (paymentType) {

            case "Installment Addon":

                entry.setType("Installment Addon");
                entry.setAmount(amount);
                entry.setTotalPrice(total_price);
                entry.setCustomerMobile(customer_mobile);
                entry.setTransactionType("sale");
                item.addInstallmentPayments(entry);
                item.addSale(entry);
                Account acc2 = item.getDebtors().where().equalTo("name", customer_mobile).findFirst();
                acc2.setInstallment(entry);


                break;
            case "Cash":
                entry.setType("Cash");
                entry.setAmount(amount);
                entry.setTransactionType("sale");
                item.addSale(entry);

                break;
            case "Installment":
                //create account with phone number and add as entry
                entry.setType("Installment");
                entry.setAmount(amount);
                entry.setTotalPrice(total_price);
                entry.setCustomerMobile(customer_mobile);
                entry.setTransactionType("sale");
                Account acc = realm.createObject(Account.class, customer_mobile);
                acc.setTotalPrice(total_price);
                acc.setBalance(total_price - amount_paid);
                acc.setInstallment(entry);
                item.addDebtors(acc);
                item.addSale(entry);


                break;
        }

        entry.setQuantity(quantity);
        entry.setAmount(amount);
        toret = entry;
        realm.copyToRealmOrUpdate(entry);

        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();
        return toret;


    }


    public static String getWeekName(int week) {
        DateTime weekStartDate = new DateTime().withWeekOfWeekyear(week);
        DateTime weekEndDate = new DateTime().withWeekOfWeekyear(week + 1);
        return String.format("%s - %s", weekStartDate, weekEndDate);

    }


    public static void updateReports(DateTime d) {
        Realm realm = Realm.getDefaultInstance();
        if (d == null) {
            d = new DateTime();
        }
        String day_name = d.toString(DateTimeFormat.mediumDate().withLocale(Locale.getDefault()));
        String previousDayName = d.minusDays(1).toString(DateTimeFormat.mediumDate().withLocale(Locale.getDefault()));
        String week_name = WeeklyReport.getWeekName(d);
        String month_name = d.toString("MMM-Y");

        DailyReport drep = DailyReport.getOrCreate(realm, day_name);
        WeeklyReport wrep = WeeklyReport.getOrCreate(realm, week_name);
        MonthlyReport mrep = MonthlyReport.getOrCreate(realm, month_name);

        RealmResults<Item> items = realm.where(Item.class).findAll();
        Double profit_today = 0.0;

        Double total_earned_today = 0.0;

        Double total_spent_today = 0.0;

        Double profit_thisweek = 0.0;

        Double total_earned_thisweek = 0.0;

        Double total_spent_thisweek = 0.0;

        Double profit_thismonth = 0.0;

        Double total_earned_thismonth = 0.0;

        Double total_spent_thismonth = 0.0;


        for (Item item : items) {
//                Long items_stocked = item.getInventories().where().sum("quantity").longValue();
//                Long items_stocked_today = item.getInventories().where().equalTo("entry_day", day_name).sum("quantity").longValue();
//                Double amount_spent_today = item.getInventories().where().equalTo("entry_day", day_name).sum("amount").doubleValue();
//                Long items_sold_today = item.getSales().where().equalTo("entry_day", day_name).sum("quantity").longValue();
//                Double amount_earned_today = item.getSales().where().equalTo("entry_day", day_name).sum("amount").doubleValue();
//                // total_spent_today = total_spent_today+amount_spent_today;
//
//                Long items_stocked_thisweek = item.getInventories().where().equalTo("entry_week",week_name).sum("quantity").longValue();
//                Double amount_spent_thisweek = item.getInventories().where().equalTo("entry_week",week_name).sum("amount").doubleValue();
//                Long items_sold_thisweek = item.getSales().where().equalTo("entry_week",week_name).sum("quantity").longValue();
//                Double amount_earned_thisweek = item.getSales().where().equalTo("entry_week",week_name).sum("amount").doubleValue();
//
//                total_spent_thisweek = total_spent_thisweek+amount_spent_thisweek;
//
//                Long items_stocked_thismonth = item.getInventories().where().equalTo("entry_month",month_name).sum("quantity").longValue();
//                Double amount_spent_thismonth = item.getInventories().where().equalTo("entry_month",month_name).sum("amount").doubleValue();
//                Long items_sold_thismonth = item.getSales().where().equalTo("entry_month",month_name).sum("quantity").longValue();
//                Double amount_earned_thismonth = item.getSales().where().equalTo("entry_month",month_name).sum("amount").doubleValue();
//
//                total_spent_thismonth = total_spent_thismonth+amount_spent_thismonth;
//
//
//                RealmResults<Entry> purchases = item.getInventories().where().findAll();
//
//                Double standardized_unitcost =  purchases.where().sum("amount").doubleValue()/items_stocked;
//
//                profit_today = profit_today+amount_earned_today - (standardized_unitcost * items_sold_today) - amount_spent_today;
//                profit_thisweek = profit_thisweek+amount_earned_thisweek - (standardized_unitcost * items_sold_thisweek);
//                profit_thismonth = profit_thismonth+amount_earned_thismonth - (standardized_unitcost * items_sold_thismonth);
//
//                //total earnings today is total revenue
//                total_earned_today = total_earned_today + amount_earned_today;
//
//                //total spent today is the cost of inventory till today
//                total_spent_today =
//
//                total_earned_thisweek = total_earned_thisweek+amount_earned_thisweek;
//                total_earned_thismonth = total_earned_thismonth+amount_earned_thismonth;

            //first find the revenue for today i.e. the amount earned today
            double earnedTodayForItem = item.getSales().where().equalTo("entry_day", day_name).sum("amount").doubleValue();
            total_earned_today += earnedTodayForItem;

            //second find the cost of inventory left till today
            long iSt = item.getInventories().where().lessThan("created", d.toDate()).sum("quantity").longValue();
            long iSt1 = item.getSales().where().lessThan("created", d.toDate()).sum("quantity").longValue();

            // cost of all inventory purchases till yesterday = all purchase cost - purchase cost today
            RealmResults<Entry> purchases = item.getInventories().where().findAll();
            double purchaseCostToday = purchases.where().equalTo("entry_day", day_name).sum("amount").doubleValue();
            double cSt = purchases.where().lessThan("created", d.toDate()).sum("amount").doubleValue();
            long val = iSt - iSt1;
            double totalSpentForItem = 0.0;
            if (val != 0) {
                double unitcost = cSt / (iSt * 1.0);
                totalSpentForItem += val * unitcost;
            }
            total_spent_today += totalSpentForItem;
            total_spent_today += purchaseCostToday;

            

            double earnedWeeklyForItem = item.getSales().where().equalTo("entry_week", week_name).sum("amount").doubleValue();
            total_earned_thisweek += earnedWeeklyForItem;

            total_spent_thisweek += totalSpentForItem;
            double amount_spent_thisweek = item.getInventories().where().equalTo("entry_week", week_name).sum("amount").doubleValue();
            total_spent_thisweek += amount_spent_thisweek;

            double earnedMontlyForItem = item.getSales().where().equalTo("entry_month", month_name).sum("amount").doubleValue();
            total_earned_thismonth += earnedMontlyForItem;

            total_spent_thismonth += totalSpentForItem;
            double amount_spent_thismonth = item.getInventories().where().equalTo("entry_month", month_name).sum("amount").doubleValue();
            total_spent_thismonth += amount_spent_thismonth;
        }
        realm.beginTransaction();

        drep.setProfit(total_earned_today - total_spent_today);
        drep.setTotalEarned(total_earned_today);
        drep.setTotalSpent(total_spent_today);
        mrep.setUpdated(d.toDate());

        drep.setUpdated(d.toDate());

        wrep.setUpdated(d.toDate());


        wrep.setProfit(total_earned_thisweek - total_spent_thisweek);
        wrep.setTotalEarned(total_earned_thisweek);
        wrep.setTotalSpent(total_spent_thisweek);

        mrep.setProfit(total_earned_thismonth - total_spent_thismonth);
        mrep.setTotalEarned(total_earned_thismonth);
        mrep.setTotalSpent(total_spent_thismonth);
        realm.copyToRealmOrUpdate(drep);
        realm.copyToRealmOrUpdate(wrep);
        realm.copyToRealmOrUpdate(mrep);
        realm.commitTransaction();
        realm.close();

    }


    public static Entry byId(Realm realm, Long created_ts) {
        return realm.where(Entry.class).equalTo("created_ts", created_ts).findFirst();
    }

    public static Entry getOrCreate(Realm realm, Long created_ts) {
        Entry entry = Entry.byId(realm, created_ts);
        if (entry == null) {
            entry = realm.createObject(Entry.class, created_ts);

        }
        return entry;


    }


}
