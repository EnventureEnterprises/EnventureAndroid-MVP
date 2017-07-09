package org.enventureenterprises.enventure.util.rx;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.enventureenterprises.enventure.data.ErrorEnvelope;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by mossplix on 4/29/17.
 */

final class NeverApiErrorTransformer<T> implements Observable.Transformer<T, T> {
    private final @Nullable
    Action1<ErrorEnvelope> errorAction;

    protected NeverApiErrorTransformer() {
        this.errorAction = null;
    }

    protected NeverApiErrorTransformer(final @Nullable Action1<ErrorEnvelope> errorAction) {
        this.errorAction = errorAction;
    }

    @Override
    public @NonNull
    Observable<T> call(final @NonNull Observable<T> source) {
        return source
                .doOnError(e -> {
                    final ErrorEnvelope env = ErrorEnvelope.fromThrowable(e);
                    if (env != null && errorAction != null) {
                        errorAction.call(env);
                    }
                })
                .onErrorResumeNext(e -> {
                    if (ErrorEnvelope.fromThrowable(e) == null) {
                        return Observable.error(e);
                    } else {
                        return Observable.empty();
                    }
                });
    }
}
