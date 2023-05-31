package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Gets;
import net.zethmayr.fungu.fields.HasX;

public interface HasIndex extends HasX {

    @Gets
    Integer getIndex();
}
