package org.enventureenterprises.enventure.util.rx;

/**
 * Created by mossplix on 5/9/17.
 */

import android.support.annotation.NonNull;

import rx.Observable;

public final class IgnoreValuesTransformer<S> implements Observable.Transformer<S, Void> {
    @Override
    @NonNull public Observable<Void> call(final @NonNull Observable<S> source) {
        return source.map(__ -> null);
    }
}
