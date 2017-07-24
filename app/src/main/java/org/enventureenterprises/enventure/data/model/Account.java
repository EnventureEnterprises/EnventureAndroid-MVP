package org.enventureenterprises.enventure.data.model;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/18/17.
 */

public class Account extends RealmObject {
    public static String   SALES_ACCOUNT = "Sales Account";
    public static String   ACCOUNT_RECEIVABLE = "Account Receivable";
    public static String   INVENTORY_ACCOUNT = "Inventory Account";

    private Date created;
    @PrimaryKey
    private String name;

    private Double balance;

    private Integer quantity;

    private RealmList<Transaction> transactions;

    private RealmList<Entry> entries;

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }

    public Date getCreated(){
        return this.created;
    }
    public void setCreated(Date created){
        this.created=created;
    }

    public Integer getQuantity(){
        return this.quantity;
    }
    public void setQuantity(Integer quantity){
        this.quantity=quantity;
    }

    public Double getBalance(){
        return this.balance;
    }
    public void setBalance(Double balance){
        this.balance=balance;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public static Account byName(Realm realm, String name) {
        return realm.where(Account.class).equalTo("name", name).findFirst();
    }

    public static Account getOrCreate(Realm realm,String name){
        Account acc = Account.byName(realm,name);
        if (acc == null){
            acc = new Account();
            acc.setName(name);
        }
        return acc;


    }

}
