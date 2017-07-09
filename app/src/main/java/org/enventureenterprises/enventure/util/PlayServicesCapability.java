package org.enventureenterprises.enventure.util;

import android.content.Context;
import android.support.annotation.NonNull;



import org.enventureenterprises.enventure.BaseApplication;


/**
 * Created by mossplix on 4/28/17.
 */

public final class PlayServicesCapability {
    private final boolean isCapable;

    public PlayServicesCapability(final boolean isCapable) {
        this.isCapable = isCapable;
    }

    public PlayServicesCapability(final @NonNull Context context) {
        final BaseApplication application = (BaseApplication) context.getApplicationContext();

            //final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            //final int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
            //isCapable = resultCode == ConnectionResult.SUCCESS;
        isCapable=true;

    }

    /**
     * Check the device to make sure it has the Google Play Services APK.
     */
    public boolean isCapable() {
        return isCapable;
    }
}
