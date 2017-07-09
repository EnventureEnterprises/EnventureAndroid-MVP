package org.enventureenterprises.enventure.util.rx;

import android.os.Looper;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mossplix on 4/29/17.
 */

public final class ObserveForUITransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public @NonNull
    Observable<T> call(final @NonNull Observable<T> source) {

        return source.flatMap(value -> {
            if (isMainThread()) {
                return Observable.just(value).observeOn(Schedulers.immediate());
            } else {
                return Observable.just(value).observeOn(AndroidSchedulers.mainThread());
            }
        });
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
