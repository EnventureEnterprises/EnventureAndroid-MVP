package org.enventureenterprises.enventure.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/18/17.
 */

public class Transaction extends RealmObject {
    @PrimaryKey
    private Long created_ts;
    private RealmList<Entry> entries;

    private Boolean status;
    public Long getCreatedTs(){
        return this.created_ts;
    }
    public void setCreatedTs(Long created_ts){
        this.created_ts=created_ts;
    }


    public boolean isValidTransaction() {
        Double amount = entries.where().sum("amount").doubleValue();
        return (amount==0);
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }


}
