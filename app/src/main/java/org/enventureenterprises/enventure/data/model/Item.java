package org.enventureenterprises.enventure.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/7/17.
 */

public class Item extends RealmObject {
    @PrimaryKey
    private  long id;
    private Date created;
    private Double amount;
    private Double totalCost;
    private Integer quantity;
    private String image;
    private String name;


    public long getId(){
        return this.id;
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





}
