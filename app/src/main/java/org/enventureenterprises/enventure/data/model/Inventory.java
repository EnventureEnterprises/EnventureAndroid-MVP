package org.enventureenterprises.enventure.data.model;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by mossplix on 7/18/17.
 */

public class Inventory extends RealmObject {

    private Item item;
    private Long current_quantity;
    private Double total_cost;
    private Double unitcost;

    private Long created_ts;


    public Item getItem(){
        return this.item;
    }
    public void setItem(Item item){
        this.item=item;
    }

    public Long getCurrentQuantity(){
        return this.current_quantity;
    }
    public void setCurrentQuantity(Long quantity){
        this.current_quantity=current_quantity;
    }


    public Double getTotalCost(){
        return this.total_cost;
    }
    public void setTotalCost(Double total_cost){
        this.total_cost=total_cost;
    }

    public Double getUnitCost(){
        return this.unitcost;
    }
    public void setUnitCost(Double unitcost){
        this.unitcost=unitcost;
    }

    public Long getCreatedTs(){
        return this.created_ts;
    }
    public void setCreatedTs(Long created_ts){
        this.created_ts=created_ts;
    }

    public static Inventory byItem(Realm realm, Item item) {
        return realm.where(Inventory.class).equalTo("item.name", item.getName()).findFirst();
    }

}
