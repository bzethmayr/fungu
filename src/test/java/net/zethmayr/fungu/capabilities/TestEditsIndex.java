package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Gets;

public class TestEditsIndex extends TestSetsIndex implements EditsIndex {

    @Gets
    @Override
    public Integer getIndex() {
        return index;
    }
}
