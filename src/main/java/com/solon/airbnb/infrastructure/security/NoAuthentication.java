package com.solon.airbnb.infrastructure.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicate that the endpoint requires no authentication, thus is will completely skip auth filters.
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface NoAuthentication {
}
