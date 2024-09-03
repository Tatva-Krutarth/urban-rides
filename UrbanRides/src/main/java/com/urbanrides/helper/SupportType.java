package com.urbanrides.helper;

public enum SupportType {

    QUERY(1, "Query"),
    REQUEST(2, "Request"),
    COMPLAINT(3, "Complaint"),
    ASK_FOR_CALL(4, "Ask for a Call");

    private final int id;
    private final String value;

    SupportType(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static SupportType getById(int id) {
        for (SupportType type : SupportType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SupportType ID: " + id);
    }

    public static String getValueById(int id) {
        SupportType type = getById(id);
        return type.getValue();
    }
}