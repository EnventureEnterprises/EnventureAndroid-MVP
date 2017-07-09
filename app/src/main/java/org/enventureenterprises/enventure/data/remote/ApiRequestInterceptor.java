package org.enventureenterprises.enventure.data.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.enventureenterprises.enventure.data.CurrentUserType;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mossplix on 4/28/17.
 */

public final class ApiRequestInterceptor implements Interceptor {
    private final String clientId;
    private final CurrentUserType currentUser;
    private final String endpoint;
    private String social_type ="";

    public ApiRequestInterceptor(final @NonNull String clientId, final @NonNull CurrentUserType currentUser,
                                 final @NonNull String endpoint) {
        this.clientId = clientId;
        this.currentUser = currentUser;
        this.endpoint = endpoint;
    }

    @Override
    public Response intercept(final @NonNull Chain chain) throws IOException {
        return chain.proceed(request(chain.request()));
    }

    private Request request(final @NonNull Request initialRequest) {
        if (!shouldIntercept(initialRequest)) {
            return initialRequest;
        }













        return initialRequest.newBuilder()
                //.header("Accept", "application/json")
                .header ("Authorization", "Bearer "+ currentUser.getAccessToken())
                .method (initialRequest.method (), initialRequest.body ())
                //.url(url(initialRequest.url()))
                .build();
    }

    /*private HttpUrl url(final @NonNull HttpUrl initialHttpUrl) {


        final HttpUrl.Builder builder = initialHttpUrl.newBuilder()
                .setQueryParameter("client_id", clientId);
        if (currentUser.exists()) {
            builder.setQueryParameter("oauth_token", currentUser.getAccessToken());
        }

        return builder.build();
    }*/

    private boolean shouldIntercept(final @NonNull Request request) {
        return isApiUri(Uri.parse(request.url().toString()), endpoint) && currentUser.exists();
    }



    public static boolean isApiUri(final @NonNull Uri uri,String endpoint) {
          return isUrbUri(uri, endpoint);
            }

    public static boolean isUrbUri(final @NonNull Uri uri, final @NonNull String endpoint) {
        return uri.getHost().equals(Uri.parse(endpoint).getHost());
    }
}
