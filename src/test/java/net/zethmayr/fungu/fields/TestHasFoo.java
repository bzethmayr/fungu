package net.zethmayr.fungu.fields;

import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;

public class TestHasFoo implements HasFoo {
    public TestHasFoo(final String foo) {
        this.foo = foo;
    }

    public TestHasFoo() {
        this(UNEXPECTED);
    }

    private String foo;

    public String getFoo() {
        return foo;
    }
}

