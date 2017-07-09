package org.enventureenterprises.enventure.util.preference;

/**
 * Created by mossplix on 4/28/17.
 */

public interface IntPreferenceType {
    /**
     * Get the current value of the preference.
     */
    int get();

    /**
     * Returns whether a value has been explicitly set for the preference.
     */
    boolean isSet();

    /**
     * Set the preference to a value.
     */
    void set(int value);

    /**
     * Delete the currently stored preference.
     */
    void delete();
}
