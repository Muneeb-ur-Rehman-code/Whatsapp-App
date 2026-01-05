package com.example.myapplication.utils;

public class PhoneUtils {

    // Remove all non-digit characters
    public static String normalize(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        return phone.replaceAll("[^0-9]", "");
    }

    // Check if phone number is valid (at least 10 digits)
    public static boolean isValid(String phone) {
        String normalized = normalize(phone);
        return normalized.length() >= 10 && normalized.length() <= 15;
    }
}