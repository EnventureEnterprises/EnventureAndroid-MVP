package org.enventureenterprises.enventure;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mossplix on 7/6/17.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent extends ApplicationGraph {
}
