package org.enventureenterprises.enventure.data.model;

import io.realm.RealmObject;

/**
 * Created by mossplix on 8/11/17.
 */

public class Image extends RealmObject {
    private Item item;
    private String image;
    private boolean synced;

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

}
