package net.zethmayr.fungu.fields;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

enum DeclaredFields {

    INSTANCE;
    private final Map<Class<?>, FieldData> knownInterfaces = new ConcurrentHashMap<>();


}
