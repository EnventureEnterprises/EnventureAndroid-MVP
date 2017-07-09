package org.enventureenterprises.enventure.data;

/**
 * Created by mossplix on 4/28/17.
 */

import android.support.annotation.NonNull;

/**
 * An exception class wrapping an {@link ErrorEnvelope}.
 */
public final class ApiException extends ResponseException {
    private final ErrorEnvelope errorEnvelope;

    public ApiException(final @NonNull ErrorEnvelope errorEnvelope, final @NonNull retrofit2.Response response) {
        super(response);
        this.errorEnvelope = errorEnvelope;
    }

    public @NonNull ErrorEnvelope errorEnvelope() {
        return errorEnvelope;
    }
}
