package org.enventureenterprises.enventure.util.preference;

/**
 * Created by mossplix on 4/28/17.
 */

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class StringPreference implements StringPreferenceType {
    private final SharedPreferences sharedPreferences;
    private final String key;
    private final String defaultValue;

    public StringPreference(final @NonNull SharedPreferences sharedPreferences, final @NonNull String key) {
        this(sharedPreferences, key, null);
    }

    public StringPreference(final @NonNull SharedPreferences sharedPreferences, final @NonNull String key,
                            final @Nullable String defaultValue) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public String get() {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    public boolean isSet() {
        return sharedPreferences.contains(key);
    }

    @Override
    public void set(final @NonNull String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    @Override
    public void delete() {
        sharedPreferences.edit().remove(key).apply();
    }
}
