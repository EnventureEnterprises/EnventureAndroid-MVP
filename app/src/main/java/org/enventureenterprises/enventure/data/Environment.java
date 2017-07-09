package org.enventureenterprises.enventure.data;

import android.content.SharedPreferences;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.enventureenterprises.enventure.data.remote.EnventureApi;

import auto.parcel.AutoParcel;
import rx.Scheduler;

/**
 * Created by mossplix on 7/7/17.
 */

@AutoParcel
public abstract class Environment implements Parcelable {
    public abstract EnventureApi apiClient();
    public abstract CurrentUserType currentUser();
    public abstract Gson gson();
    public abstract Scheduler scheduler();
    public abstract SharedPreferences sharedPreferences();


    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract Builder apiClient(EnventureApi __);
        public abstract Builder currentUser(CurrentUserType __);
        public abstract Builder gson(Gson __);
        public abstract Builder scheduler(Scheduler __);
        public abstract Builder sharedPreferences(SharedPreferences __);
        public abstract Environment build();
    }

    public static Builder builder() {
        return new AutoParcel_Environment.Builder();
    }

    public abstract Builder toBuilder();
}
