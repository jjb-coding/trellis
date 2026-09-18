package com.github.jjbcoding.trellis.service.contributions;

/**
 * A DigestComponent is a divisible component of the output of a
 * DigestProcessor. Components are passed up the tree. Any component
 * can be consumed, which will remove it from propagation, or
 * fetched, which allows the Node to read and/or modify it before
 * it is passed on.
 */
public abstract class DigestComponent {}
