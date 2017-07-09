package org.enventureenterprises.enventure.data.remote;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.enventureenterprises.enventure.data.model.User;
import org.enventureenterprises.enventure.injection.qualifier.AutoGson;

import auto.parcel.AutoParcel;



@AutoGson
@AutoParcel
public abstract class AccessToken implements Parcelable {



    @SerializedName("access_token")
    public abstract String accessToken();

    @SerializedName("token_type")
    public abstract String tokenType();

    @SerializedName("expires_in")
    public abstract Long expiresIn();

    @SerializedName("refresh_token")
    public abstract String refreshToken();

    public abstract User user();



    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract AccessToken.Builder accessToken(String __);
        public abstract AccessToken.Builder tokenType(String __);
        public abstract AccessToken.Builder expiresIn(Long __);
        public abstract AccessToken.Builder refreshToken(String __);
        public abstract AccessToken.Builder user(User __);
        public abstract AccessToken build();
    }


    public static AccessToken.Builder builder() {
        return new AutoParcel_AccessToken.Builder();
    }

    public abstract AccessToken.Builder toBuilder();
}