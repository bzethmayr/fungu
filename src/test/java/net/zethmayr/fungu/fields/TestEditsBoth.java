package net.zethmayr.fungu.fields;


import net.zethmayr.fungu.test.TestConstants;

import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;

public class TestEditsBoth implements EditsFoo, EditsBar {
    private String foo = UNEXPECTED;
    private String bar = UNEXPECTED;

    @Override
    public String getFoo() {
        return foo;
    }

    @Override
    public void setFoo(final String foo) {
        this.foo = foo;
    }

    @Override
    public String getBar() {
        return bar;
    }

    @Override
    public void setBar(final String bar) {
        this.bar = bar;
    }
}
