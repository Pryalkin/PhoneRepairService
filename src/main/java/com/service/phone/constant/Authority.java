package com.service.phone.constant;

public class Authority {
    public static final String[] USER_AUTHORITIES = { "user:create_app"};
    public static final String[] ENGINEER_AUTHORITIES = { "user:get_inactive", "user:buy"};
    public static final String[] ADMIN_AUTHORITIES = { "user:read", "user:buy","user:create_organization"};
}
