package com.solon.airbnb.util;

import java.util.HexFormat;
import java.util.UUID;

public class TestConstants {

    public static final String TEST_TOKEN = "CCMTokenString";

    /* Test User Constants */
    public static final Integer TEST_USER_ID = Integer.valueOf(1);
    public static final String TEST_USER_PUBLIC_ID = "7bf2e04a-f9d1-4f08-955d-f595d042ac3d";
    public static final String TEST_INVALID_USER_PUBLIC_ID = "7bf2e04a-f9d1-4f08-955d-f595d042ac3";
    public static final Integer TEST_USER_INVALID_ID = Integer.valueOf(-1);
    public static final String TEST_USER_FIRST_NAME = "Robert";
    public static final String TEST_USER_SURNAME = "Smith";
    public static final String TEST_USER_USERNAME = "admin1";

    public static byte[] TEST_FILE_CONTENT = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
}
