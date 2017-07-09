package org.enventureenterprises.enventure.data;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import org.enventureenterprises.enventure.injection.qualifier.AutoGson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import auto.parcel.AutoParcel;

/**
 * Created by mossplix on 4/28/17.
 */

@AutoGson
@AutoParcel
public abstract class ErrorEnvelope implements Parcelable {
    public abstract @Nullable
    List<String> errorMessages();
    public abstract int httpCode();
    public abstract @Nullable String urbCode();
    public abstract @Nullable FacebookUser facebookUser();
    public abstract @Nullable TwitterUser twitterUser();

    @AutoGson
    @AutoParcel
    public static abstract class FacebookUser implements Parcelable {
        public abstract String id();
        public abstract String name();
        public abstract String email();

        @AutoParcel.Builder
        public static abstract class Builder {
            public abstract Builder id(String __);
            public abstract Builder name(String __);
            public abstract Builder email(String __);
            public abstract FacebookUser build();
        }

        public static Builder builder() {
            return new AutoParcel_ErrorEnvelope_FacebookUser.Builder();
        }

        public abstract Builder toBuilder();
    }

    @AutoGson
    @AutoParcel
    public static abstract class TwitterUser implements Parcelable {
        public abstract String id();
        public abstract String name();
        public abstract @Nullable  String email();
        public abstract @Nullable  String mobile();

        @AutoParcel.Builder
        public static abstract class Builder {
            public abstract Builder id(String __);
            public abstract Builder name(String __);
            public abstract Builder email(String __);
            public abstract Builder mobile(String __);
            public abstract TwitterUser build();
        }

        public static Builder builder() {
            return new AutoParcel_ErrorEnvelope_TwitterUser.Builder();
        }

        public abstract Builder toBuilder();
    }



    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract Builder errorMessages(List<String> __);
        public abstract Builder httpCode(int __);
        public abstract Builder urbCode(String __);
        public abstract Builder facebookUser(FacebookUser __);
        public abstract Builder twitterUser(TwitterUser __);
        public abstract ErrorEnvelope build();
    }

    public static Builder builder() {
        return new AutoParcel_ErrorEnvelope.Builder();
    }

    public abstract Builder toBuilder();

    public static final String CONFIRM_FACEBOOK_SIGNUP = "confirm_facebook_signup";
    public static final String CONFIRM_TWITTER_SIGNUP = "confirm_twitter_signup";
    public static final String INVALID_XAUTH_LOGIN = "invalid_xauth_login";
    public static final String TFA_FAILED = "tfa_failed";
    public static final String TFA_REQUIRED = "tfa_required";
    public static final String MISSING_FACEBOOK_EMAIL = "missing_facebook_email";
    public static final String FACEBOOK_INVALID_ACCESS_TOKEN = "facebook_invalid_access_token";
    public static final String UNAUTHORIZED = "unauthorized";

    @StringDef({INVALID_XAUTH_LOGIN, TFA_FAILED, TFA_REQUIRED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {}

    /**
     * Tries to extract an {@link ErrorEnvelope} from an exception, and if it
     * can't returns null.
     */
    public static @Nullable
    ErrorEnvelope fromThrowable(final @NonNull Throwable t) {
        if (t instanceof ApiException) {
            final ApiException exception = (ApiException) t;
            return exception.errorEnvelope();
        }

        return null;
    }

    public boolean isConfirmFacebookSignupError() {
        return CONFIRM_FACEBOOK_SIGNUP.equals(urbCode());
    }

    public boolean isConfirmTwitterSignupError() {
        return CONFIRM_TWITTER_SIGNUP.equals(urbCode());
    }


    public boolean isInvalidLoginError() {
        return INVALID_XAUTH_LOGIN.equals(urbCode());
    }

    public boolean isTfaRequiredError() {
        return TFA_REQUIRED.equals(urbCode());
    }

    public boolean isTfaFailedError() {
        return TFA_FAILED.equals(urbCode());
    }

    public boolean isMissingFacebookEmailError() {
        return MISSING_FACEBOOK_EMAIL.equals(urbCode());
    }

    public boolean isFacebookInvalidAccessTokenError() {
        return FACEBOOK_INVALID_ACCESS_TOKEN.equals(urbCode());
    }

    public boolean isUnauthorizedError() {
        return UNAUTHORIZED.equals(urbCode());
    }

    /*
      When logging in the only two possible errors are INVALID_XAUTH_LOGIN
      and TFA_REQUIRED, so we consider anything else an unknown error.
     */
    public boolean isGenericLoginError() {
        return
                !INVALID_XAUTH_LOGIN.equals(urbCode()) &&
                        !TFA_REQUIRED.equals(urbCode());
    }

    /**
     * Returns the first error message available, or `null` if there are none.
     */
    public @Nullable String errorMessage() {
        if (errorMessages() == null) {
            return null;
        } else {
            return first(errorMessages());
        }
    }

    public static @Nullable <T> T first(final @NonNull List<T> xs) {
        return xs.size() > 0 ? xs.get(0) : null;
    }
}
