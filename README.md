# fungu

Functional glue.

### Why on earth would I want another one of these?
This focuses on creation and mutation of objects, vs on immutable structures.

This one isn't linked to any particular functionality,
and since it's based on `java.util.function` interfaces,
is probably compatible with them.

### Features
* [n-tuples](/src/main/java/net/zethmayr/fungu/hypothetical/Nuple.java)
* [tuples](/src/main/java/net/zethmayr/fungu/Fork.java)
* [automated reference counting](/src/main/java/net/zethmayr/fungu/arc/package-info.java)
* [null coalescence](/src/main/java/net/zethmayr/fungu/CoalescenceHelper.java)
* [varargs predicate composition](/src/main/java/net/zethmayr/fungu/PredicateFactory.java)
* [exception sinks](/src/main/java/net/zethmayr/fungu/throwing/package-info.java)
* [field interfaces](/src/main/java/net/zethmayr/fungu/fields/package-info.java)
* [fluent visitor pattern](/src/main/java/net/zethmayr/fungu/UponHelper.java)

### Smaller features
* [close proxies](/src/main/java/net/zethmayr/fungu/CloseableFactory.java)
* [consumer adapters](/src/main/java/net/zethmayr/fungu/ConsumerFactory.java)
* [supplier adapters](/src/main/java/net/zethmayr/fungu/core/SupplierFactory.java)
* [value adapters](/src/main/java/net/zethmayr/fungu/core/ValueFactory.java)
* [fluent mutations](/src/main/java/net/zethmayr/fungu/Modifiable.java)
* [rich exceptions](/src/main/java/net/zethmayr/fungu/core/ExceptionFactory.java)
