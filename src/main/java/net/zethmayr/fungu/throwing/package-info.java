/**
 * Adapts methods throwing checked exceptions
 * to analogues deferring throw.
 * Contrived example usage:
 * <pre>
 * final Closeable resource = getResource();
 * final Sink thrown = sink();
 * Optional.of(resource)
 *    .ifPresent(sinking(sinkable(Closeable::close), thrown));
 * thrown.raise();
 * </pre>
 */
@LowerLevel
package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.core.declarations.LowerLevel;