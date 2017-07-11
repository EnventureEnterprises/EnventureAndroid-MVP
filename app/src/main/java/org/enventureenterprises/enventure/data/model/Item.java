package org.enventureenterprises.enventure.data.model;

import java.util.Date;

import io.realm.RealmObject;
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
    private String image;
    private String name;

    @PrimaryKey
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





}
