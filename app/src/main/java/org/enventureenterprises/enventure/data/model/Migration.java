package org.enventureenterprises.enventure.data.model;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by mossplix on 7/6/17.
 */

public class Migration implements RealmMigration {
    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            //RealmObjectSchema notificationSchema = schema.get("NotificationCommand");

            // Combine 'firstName' and 'lastName' in a new field called 'fullName'
            //notificationSchema
              //      .addField("created", Date.class, FieldAttribute.REQUIRED);

            //oldVersion++;
        }







    }
}