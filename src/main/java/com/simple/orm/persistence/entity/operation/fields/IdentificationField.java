package com.simple.orm.persistence.entity.operation.fields;

public enum IdentificationField {

    ID_FIELD_NAME("ID_FIELD_NAME"),
    ID_FIELD_VALUE("ID_FIELD_VALUE");

    private final String value;

    IdentificationField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IdentificationField{" +
                "value='" + value + '\'' +
                '}';
    }
}
