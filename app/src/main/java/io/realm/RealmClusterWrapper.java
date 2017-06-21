package io.realm;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * A wrapper around a {@link RealmObject} and cache for the latitude and longitude values so that
 * the wrapper class can be used on the background thread for marker calculation.
 *
 * The title is also cached, since the realmObject's row can no longer be accessed directly / via
 * index.
 */
public class RealmClusterWrapper<T extends RealmObject> implements ClusterItem {

    private T realmObject;

    private LatLng latLng;

    private String title;

    private String description;

    private Long id;

    public RealmClusterWrapper(T realmObject, String title, double latitude, double longitude,String description,Long id) {
        this.realmObject = realmObject;
        this.title = title;
        latLng = new LatLng(latitude, longitude);
        this.id = id;
        this.description = description;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    public String getTitle() {
        return title;
    }

    public Long getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public LatLng getLocation(){
        return latLng;
    }
}
