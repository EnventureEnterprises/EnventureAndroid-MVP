package org.enventureenterprises.enventure.data;

import android.os.Parcelable;

import org.enventureenterprises.enventure.data.model.User;
import org.enventureenterprises.enventure.injection.qualifier.AutoGson;

import auto.parcel.AutoParcel;


/**
 * Created by mossplix on 4/28/17.
 */

@AutoGson
@AutoParcel
public abstract class AccessTokenEnvelope implements Parcelable {
    public abstract String accessToken();
    public abstract User user();

    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract Builder accessToken(String __);
        public abstract Builder user(User __);
        public abstract AccessTokenEnvelope build();
    }

    public static Builder builder() {
        return new AutoParcel_AccessTokenEnvelope.Builder();
    }

    public abstract Builder toBuilder();
}
