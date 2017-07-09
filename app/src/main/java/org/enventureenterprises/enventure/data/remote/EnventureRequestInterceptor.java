package org.enventureenterprises.enventure.data.remote;

import android.support.annotation.NonNull;

import org.enventureenterprises.enventure.util.Build;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mossplix on 4/18/17.
 */

public final class EnventureRequestInterceptor implements Interceptor {
    private final Build build;

    public EnventureRequestInterceptor(final @NonNull Build build) {
        this.build = build;
    }

    @Override
    public Response intercept(final @NonNull Chain chain) throws IOException {
        return chain.proceed(request(chain.request()));
    }

    private Request request(final @NonNull Request initialRequest) {
        return initialRequest.newBuilder()
                .header("urb-Android-App", build.versionCode().toString())
                .header("urb-App-Id", build.applicationId())
                .build();
    }
}
