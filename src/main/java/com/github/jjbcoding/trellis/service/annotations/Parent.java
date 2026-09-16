package com.github.jjbcoding.trellis.service.annotations;

import com.github.jjbcoding.trellis.exceptions.BuilderException;
import com.github.jjbcoding.trellis.service.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intended to be applied to all classes that extend Node or MultiNode,
 * except the intended root node. The value of this annotation allocates their parent.
 * Nodes should form a tree; cyclic relationships will result in a {@link BuilderException}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Parent {
	Class<? extends Node> value();
}
