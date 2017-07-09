package org.enventureenterprises.enventure.injection.module;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.enventureenterprises.enventure.injection.qualifier.ActivityContext;
import org.enventureenterprises.enventure.injection.scopes.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Moses on 7/25/16.
 */

@Module
public class FragmentModule {

    private final Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @PerFragment
    @ActivityContext
    Context provideAppContext() {
        return mFragment.getActivity ();
    }
}
