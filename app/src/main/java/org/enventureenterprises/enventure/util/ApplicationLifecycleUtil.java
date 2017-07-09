package org.enventureenterprises.enventure.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.enventureenterprises.enventure.BaseApplication;
import org.enventureenterprises.enventure.data.ErrorEnvelope;
import org.enventureenterprises.enventure.data.Logout;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.ui.general.HomeActivity;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static org.enventureenterprises.enventure.BaseApplication.context;


/**
 * Created by mossplix on 4/27/17.
 */

public final class ApplicationLifecycleUtil  {

    protected @Inject
    EnventureApi client;
    //protected @Inject CurrentConfigType config;
    protected @Inject
    Logout logout;

    private final BaseApplication application;
    private boolean isInBackground = true;

    public ApplicationLifecycleUtil(final @NonNull BaseApplication application) {
        this.application = application;
        application.component().inject(this);
    }


    public void onActivityCreated(final @NonNull Activity activity, final @Nullable Bundle bundle) {
    }


    public void onActivityStarted(final @NonNull Activity activity) {
    }


    public void onActivityResumed(final @NonNull Activity activity) {
        if(isInBackground){


            isInBackground = false;
        }
    }

    /**
     * Handles a config API error by logging the user out in the case of a 401. We will interpret
     * 401's on the config request as meaning the user's current access token is no longer valid,
     * as that endpoint should never 401 othewise.
     */
    private void handleConfigApiError(final @NonNull ErrorEnvelope error) {
        if (error.httpCode() == 401) {
            logout.execute();
            final Intent intent = new Intent(context, HomeActivity.class)
                    .setAction(Intent.ACTION_MAIN)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            context.startActivity(intent);
        }
    }





}
