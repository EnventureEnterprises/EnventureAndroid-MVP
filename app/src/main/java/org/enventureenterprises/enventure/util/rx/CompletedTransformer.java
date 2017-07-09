package org.enventureenterprises.enventure.util.rx;

import android.support.annotation.NonNull;

import rx.Notification;
import rx.Observable;

/**
 * Created by mossplix on 4/29/17.
 */

public final class CompletedTransformer<T> implements Observable.Transformer<Notification<T>, Void> {

    @Override
    public @NonNull
    Observable<Void> call(final @NonNull Observable<Notification<T>> source) {
        return source
                .filter(Notification::isOnCompleted)
                .map(__ -> null);
    }
}

