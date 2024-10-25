

package com.urbanrides.enums;

public enum NotificationTypeEnum {
    // Rider side
    ID1("Deposit successful"),
    ID2("Ride accepted"),
    ID3("Payment Done"),

    // Commons
    ID4("Password Changed"),
    ID5("Appeal Raised"),
    ID6("Ride Concluded"),

    // Captain side
    ID7("Service Concluded"),
    ID8("Service Confirmed"),
    ID9("Ride Completed"),
    ID10("Withdraw Successful"),

    // Admin side
    ID11("Admin message"),
    ID12("Document Verified"),
    ID13("Document Verification Failed"),
    ID14("Account Warning"),
    ID15("Account Blocked");

    private final String value;

    NotificationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Method to retrieve value by ID
    public static String getValueById(String id) {
        for (NotificationTypeEnum pair : NotificationTypeEnum.values()) {
            if (pair.name().equals(id)) {
                return pair.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid notification type ID: " + id);
    }

    // Method to retrieve ID by value
    public static String getIdByValue(String value) {
        for (NotificationTypeEnum pair : NotificationTypeEnum.values()) {
            if (pair.getValue().equals(value)) {
                return pair.name();
            }
        }
        throw new IllegalArgumentException("Invalid notification type value: " + value);
    }
}
