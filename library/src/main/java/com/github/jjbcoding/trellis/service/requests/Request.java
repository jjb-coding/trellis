package com.github.jjbcoding.trellis.service.requests;

/**
 * A Request traverses the tree from the originating node to leaf,
 * or from the root to the leaf if passed to the {@link com.github.jjbcoding.trellis.service.AppService}
 * via {@link com.github.jjbcoding.trellis.service.AppService#request(Request)}.
 * Any Node can catch a request, then discover its subtype, and potentially query fields or modify it.
 * A catching Node decides whether the request is satisfied or not. If a Request is not satisfied
 * by the time it terminates and requestMustBeSatisfied is configured true, a {@link com.github.jjbcoding.trellis.exceptions.ResolutionFailure}
 * exception will be thrown.
 */
public abstract class Request {}
