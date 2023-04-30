package net.zethmayr.fungu.fields;

import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;

public class TestEditsFoo implements EditsFoo {

    private String foo;

    public TestEditsFoo(final String foo) {
        this.foo = foo;
    }

    public TestEditsFoo() {
        this(UNEXPECTED);
    }

    @Override
    public String getFoo() {
        return foo;
    }

    @Override
    public void setFoo(final String foo) {
        this.foo = foo;
    }
}
