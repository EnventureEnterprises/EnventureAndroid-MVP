package org.enventureenterprises.enventure.util.preference;

/**
 * Created by mossplix on 4/28/17.
 */

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class BooleanPreference implements BooleanPreferenceType {
    private final SharedPreferences sharedPreferences;
    private final String key;
    private final boolean defaultValue;

    public BooleanPreference(final @NonNull SharedPreferences sharedPreferences, final @NonNull String key) {
        this(sharedPreferences, key, false);
    }

    public BooleanPreference(final @NonNull SharedPreferences sharedPreferences, final @NonNull String key,
                             final boolean defaultValue) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean get() {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public boolean isSet() {
        return sharedPreferences.contains(key);
    }

    @Override
    public void set(final boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public void delete() {
        sharedPreferences.edit().remove(key).apply();
    }
}
