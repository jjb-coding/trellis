package com.github.jjbcoding.trellis.service.rules.defaults;

/**
 * Defines the relationship that the Rule validates.
 * Let:
 *      D             := the discovered set of classes
 *      P             := the provided set of classes
 * Then:
 *      InProvider    := D is a subset of P;
 *      CoverProvider := P is a subset of D;
 *      EqualProvider := D == P
 */
public enum Relationship {
    InProvider,
    CoverProvider,
    EqualProvider
}
