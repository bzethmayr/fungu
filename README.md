# fungu

Functional glue.

### Why on earth would I want another one of these?

The goal throughout is to enable expressive compositional patterns.

This provides predictable and tested implementations of common business use cases,
likely including something you find to be missing in the standard libraries.

This one isn't aimed towards any particular application functionality,
and since it's based on `java.util.function` interfaces,
is probably usable with any other specific libraries.

#### so Apache Commons
No, not as far as I know. This is mostly higher-order and/or generic glue code
which doesn't overlap with most of their use cases.

#### Opinion on mutability
This focuses on creation and mutation of objects, vs on immutable structures.
You can use certainly immutable objects with the methods here,
and probably should if they're going to be shared.

#### Dotlessness
Not so much but this can sharply cut down on the number of semicolons.

### Features
* [n-tuples](/src/main/java/net/zethmayr/fungu/hypothetical/Nuple.java)
  * unifying abstraction for various struct-like classes
* [tuples](/src/main/java/net/zethmayr/fungu/Fork.java)
  * provides equivalent of non-existing `Stream.zip` 
* [automated reference counting](/src/main/java/net/zethmayr/fungu/arc/package-info.java)
* [null coalescence](/src/main/java/net/zethmayr/fungu/CoalescenceHelper.java)
  * provides equivalent and higher-order forms of SQL `COALESCE`
* [varargs predicate composition](/src/main/java/net/zethmayr/fungu/PredicateFactory.java)
* [exception sinks](/src/main/java/net/zethmayr/fungu/throwing/package-info.java)
* [field interfaces](/src/main/java/net/zethmayr/fungu/fields/package-info.java)
* [fluent visitor pattern](/src/main/java/net/zethmayr/fungu/UponHelper.java)
* Punctiliously complete javadoc

### Smaller features
* [close() proxies](/src/main/java/net/zethmayr/fungu/CloseableFactory.java)
* [consumer adapters](/src/main/java/net/zethmayr/fungu/ConsumerFactory.java)
* [supplier adapters](/src/main/java/net/zethmayr/fungu/core/SupplierFactory.java)
* [value adapters](/src/main/java/net/zethmayr/fungu/core/ValueFactory.java)
* [fluent mutations](/src/main/java/net/zethmayr/fungu/Modifiable.java)
  * allows building complex instance graphs in single expressions.
* [rich exceptions](/src/main/java/net/zethmayr/fungu/core/ExceptionFactory.java)
