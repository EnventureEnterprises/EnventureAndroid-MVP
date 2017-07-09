package org.enventureenterprises.enventure.util.rx;

import android.support.annotation.NonNull;

import org.enventureenterprises.enventure.util.ObjectUtils;

import rx.Observable;

/**
 * Created by mossplix on 4/29/17.
 */

public final class CoalesceTransformer<T> implements Observable.Transformer<T, T> {
    private final T theDefault;

    public CoalesceTransformer(final @NonNull T theDefault) {
        this.theDefault = theDefault;
    }

    @Override
    public @NonNull Observable<T> call(final @NonNull Observable<T> source) {
        return source
                .map(ObjectUtils.coalesceWith(theDefault));
    }
}
