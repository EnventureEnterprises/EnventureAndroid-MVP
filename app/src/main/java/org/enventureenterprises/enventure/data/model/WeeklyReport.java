package org.enventureenterprises.enventure.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mossplix on 7/6/17.
 */

public class WeeklyReport extends RealmObject {
    @PrimaryKey
    private  long id;
}
