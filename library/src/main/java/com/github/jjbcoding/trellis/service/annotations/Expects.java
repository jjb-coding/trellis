package com.github.jjbcoding.trellis.service.annotations;

import com.github.jjbcoding.trellis.service.Injectable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intended to be applied to any Node or MultiNode.
 * The value of this annotation describes which Injectables the Node expects
 * to receive from an Injectable Bag. Such instances are subsequently
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Expects {
    Class<? extends Injectable>[] value();
}