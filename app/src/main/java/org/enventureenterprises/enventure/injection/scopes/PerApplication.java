package org.enventureenterprises.enventure.injection.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Moses on 7/25/16.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerApplication {
}
