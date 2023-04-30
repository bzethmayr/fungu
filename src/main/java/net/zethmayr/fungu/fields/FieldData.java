package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.Modifiable;

class FieldData implements Modifiable<FieldData> {

    private Class<?> fieldType;

    private Class<?> having;

    private Class<?> setting;

    private Class<?> editing;

    public FieldData() {

    }

    void setFieldType(final Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    void setHaving(final Class<?> having) {
        this.having = having;
    }

    void setSetting(final Class<?> setting) {
        this.setting = setting;
    }

    void setEditing(final Class<?> editing) {
        this.editing = editing;
    }

    Class<?> getFieldType() {
        return fieldType;
    }

    Class<?> getHaving() {
        return having;
    }

    Class<?> getSetting() {
        return setting;
    }

    Class<?> getEditing() {
        return editing;
    }
}
