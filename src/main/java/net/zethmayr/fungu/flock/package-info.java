/**
 * Vector clock implementation.
 * <p/>
 * So far, add-remove operations with dense indices mean non-comparability pretty quickly.
 * There's no particular need for indices to be dense or arbitrary, though.
 * Using indices according to the top-traversed nodes of a binary tree over the ints, is for instance feasible.
 * This would allow construction of a particular view of the tree nodes.
 * We can never validly zero the value at an index, but since a remove cannot precede an add, we can resume at an index.
 * This avoids indefinite tombstones but still requires marking vs performing removals.
 * Since top traversal manipulates the most-significant bits starting from zero,
 * we can compare indices by number of trailing zeros then nearness to zero.
 * This gives an iteration order for serialization that corresponds to the top-traversal order.
 * <p />
 * The above is not straightforward to actually do, but,
 * {@link java.lang.Integer#reverse} scatters indexes in a sufficiently similar way.
 */
@HigherLevel
@NotDone
package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.core.declarations.HigherLevel;
import net.zethmayr.fungu.core.declarations.NotDone;