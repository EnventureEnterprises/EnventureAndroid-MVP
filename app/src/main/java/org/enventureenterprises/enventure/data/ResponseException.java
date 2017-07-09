package org.enventureenterprises.enventure.data;

import android.support.annotation.NonNull;

/**
 * Created by mossplix on 4/28/17.
 */

public class ResponseException extends RuntimeException {
    private final retrofit2.Response response;

    public ResponseException(final @NonNull retrofit2.Response response) {
        this.response = response;
    }

    public @NonNull retrofit2.Response response() {
        return response;
    }
}
