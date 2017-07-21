package org.enventureenterprises.enventure.service;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import org.enventureenterprises.enventure.BaseApplication;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.util.rx.Transformers;

import javax.inject.Inject;

import io.realm.OrderedRealmCollectionSnapshot;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by mossplix on 7/11/17.
 */

public class SyncService extends GcmTaskService {
    @Inject
    EnventureApi client;

    private static final String TAG = "BestTimeService";
    Realm realm;

    public SyncService() {
        super();
        BaseApplication.getInstance().getComponent().inject(this);

    }


    public void setSynced(Item item,Realm realm){
        Timber.i("item synced with waragi");

        realm.beginTransaction();
        item.setSynced(true);

        realm.copyToRealmOrUpdate (item);

        realm.commitTransaction();

    }

    public void setSynced(Entry entry,Realm realm){
        Timber.i("entry synced with waragi");

        realm.beginTransaction();
        entry.setSynced(true);

        realm.copyToRealmOrUpdate (entry);

        realm.commitTransaction();

    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Timber.i("sync service running on waragi");
        realm = realm.getDefaultInstance();

        RealmResults<Entry> entries = realm.where(Entry.class)
                .equalTo("synced", false)
                .findAll();

        RealmResults<Item> items = realm.where(Item.class)
                .equalTo("synced", false)
                .findAll();

        OrderedRealmCollectionSnapshot<Item> itemsSnapshot = items.createSnapshot();

        OrderedRealmCollectionSnapshot<Entry> entriesSnapshot = entries.createSnapshot();

        for (int i = 0; i >entriesSnapshot.size(); i++) {
            Entry entry= entriesSnapshot.get(i);
            client.createEntry(entry)
                    .compose(Transformers.neverError()).subscribe(k ->setSynced(entry,realm));
        }

        for (int i = 0; i >itemsSnapshot.size(); i++) {
            Item item = itemsSnapshot.get(i);
            client.createItem(item)
                    .compose(Transformers.neverError()).subscribe(k ->setSynced(item,realm));
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
