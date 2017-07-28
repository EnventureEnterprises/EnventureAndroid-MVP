package org.enventureenterprises.enventure.data.model;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/7/17.
 */

public class Item extends RealmObject {

    private  long id;
    private Date created;
    private Double amount;
    private Double totalCost;
    private Integer quantity;
    private Double unitcost;
    private String image;
    @PrimaryKey
    private String name;

    private Boolean enabled;

    public  RealmList<Entry> sales;
    public  RealmList<Entry> inventory_updates;
    public  RealmList<Account> debtors;
    public RealmList<Entry> installment_payments;



    private Long created_ts;

    private boolean synced;


    public long getId(){
        return this.created_ts;
    }
    public void setId(Long id){
        this.id=id;
    }

    public Double getTotalCost(){
        return this.totalCost;
    }
    public void setTotalCost(Double totalCost){

        this.totalCost=totalCost;


    }

    public RealmList<Entry> getSales(){
        return this.sales;
    }

    public RealmList<Entry> getInventories(){
        return this.inventory_updates;
    }


    public Double getUnitCost(){
        return this.totalCost;
    }
    public void setUnitcost(Double unitcost){
        this.unitcost=unitcost;
    }


    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount=amount;
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

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getImage(){
        return this.image;
    }
    public void setImage(String image){
        this.image=image;
    }

    public Boolean getSynced(){
        return this.synced;
    }
    public void setSynced(Boolean synced){
        this.synced=synced;
    }

    public Long getCreatedTs(){
        return this.created_ts;
    }
    public void setCreatedTs(Long created_ts){
        this.created_ts=created_ts;
    }


    public static Item byName(Realm realm, String name) {
        return realm.where(Item.class).equalTo("name", name).findFirst();
    }

    public static Item getOrCreate(Realm realm,String name){
         Item item = Item.byName(realm,name);
        if (item == null){
            item = new Item();
            item.setName(name);
        }
        return item;


    }


    public void addSale(Entry sale)
    {

       this.sales.add(sale);
    }

    public void addInventoryUpdate(Entry inventory)
    {
        this.inventory_updates.add(inventory);
    }

    public void addInstallmentPayments(Entry payment){
        this.installment_payments.add(payment);

    }

    public void addDebtors(Account account){
        this.debtors.add(account);


    }

    public RealmResults<Account> getDebtors() {
        return this.debtors.where().findAll();
    }

    public ArrayList<String> getCustomerNumbers() {
        ArrayList<String> mobiles = new ArrayList<String>();

        if(this.debtors == null) {
            return new ArrayList<String>();
        }

        for (Account acc : this.debtors.where().findAll()) {
            mobiles.add(acc.getName());

        }
        return mobiles;
    }




    public Boolean getEnabled(){
        return this.enabled;
    }
    public void setEnabled(Boolean enabled){
        this.enabled=enabled;
    }



}


