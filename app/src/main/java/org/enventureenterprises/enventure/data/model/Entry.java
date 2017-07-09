package org.enventureenterprises.enventure.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/6/17.
 */

public class Entry extends RealmObject {
    @PrimaryKey
    private  long id;
    private Item item;
    private Double amount;
    private Date created;
    private Integer quantity;
    private String type;
    private Integer entry_year;
    private Integer entry_month;
    private Integer entry_week;
    private Integer entry_day;


    public long getId(){
        return this.id;
    }
    public void setId(Long id){
        this.id=id;
    }

    public Item getItem(){
        return this.item;
    }
    public void setItem(Item item){
        this.item=item;
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

    public Integer getEntryYear(){
        return this.entry_year;
    }
    public void setEntryYear(Integer entry_year){
        this.entry_year=entry_year;
    }

    public Integer getEntryMonth(){
        return this.entry_month;
    }
    public void setEntryMonth(Integer entry_month){
        this.entry_month=entry_month;
    }

    public Integer getEntryWeek(){
        return this.entry_week;
    }
    public void setEntryWeek(Integer entry_week){
        this.entry_week=entry_week;
    }

    public Integer getEntryDay(){
        return this.entry_day;
    }
    public void setImage(Integer entry_day){
        this.entry_day=entry_day;
    }


}
