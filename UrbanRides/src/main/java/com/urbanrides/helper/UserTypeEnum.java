


package com.urbanrides.helper;

public enum UserTypeEnum {
    ADMIN(1, "Admin"),
    CAPTAIN(2, "Captain"),
    RIDER(3, "Rider");

    private final int id;
    private final String value;

    UserTypeEnum(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static String getValueById(int id) {
        for (UserTypeEnum pair : UserTypeEnum.values()) {
            if (pair.getId() == id) {
                return pair.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid notification type ID: " + id);
    }

    public static int getIdByValue(String value) {
        for (UserTypeEnum pair : UserTypeEnum.values()) {
            if (pair.getValue().equals(value)) {
                return pair.getId();
            }
        }
        throw new IllegalArgumentException("Invalid notification type value: " + value);
    }
}