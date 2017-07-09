package org.enventureenterprises.enventure.util.preference;

/**
 * Created by mossplix on 4/28/17.
 */

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class IntPreference implements IntPreferenceType {
    private final SharedPreferences sharedPreferences;
    private final String key;
    private final int defaultValue;

    public IntPreference(final @NonNull SharedPreferences sharedPreferences, final @NonNull String key) {
        this(sharedPreferences, key, 0);
    }

    public IntPreference(final @NonNull SharedPreferences sharedPreferences, final @NonNull String key,
                         final int defaultValue) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public int get() {
        return sharedPreferences.getInt(key, defaultValue);
    }

    @Override
    public boolean isSet() {
        return sharedPreferences.contains(key);
    }

    @Override
    public void set(final int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    @Override
    public void delete() {
        sharedPreferences.edit().remove(key).apply();
    }
}
