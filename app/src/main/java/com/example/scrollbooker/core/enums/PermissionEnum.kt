package com.example.scrollbooker.core.enums

enum class PermissionEnum(val key: String) {
    NOMENCLATURES_VIEW("NOMENCLATURES_VIEW"),
    ADMIN_BUTTON_VIEW("ADMIN_BUTTON_VIEW"),
    EDIT_USER_PROFESSION("EDIT_USER_PROFESSION"),
    BOOK_BUTTON_VIEW("BOOK_BUTTON_VIEW"),
    POST_CREATE("POST_CREATE"),

    PRODUCT_EDIT("PRODUCT_EDIT"),
    PRODUCT_DELETE("PRODUCT_DELETE"),

    GENDER_EDIT("GENDER_EDIT"),
    BIRTHDATE_EDIT("BIRTHDATE_EDIT"),

    FILTER_APPOINTMENTS_VIEW("FILTER_APPOINTMENTS_VIEW"),

    // My Business
    MY_BUSINESS_ROUTES_VIEW("MY_BUSINESS_ROUTES_VIEW"),

    MY_SUBSCRIPTION_VIEW("MY_SUBSCRIPTION_VIEW"),

    MY_BUSINESS_LOCATION_VIEW("MY_BUSINESS_LOCATION_VIEW"),
    MY_SCHEDULES_VIEW("MY_SCHEDULES_VIEW"),

    MY_PRODUCTS_VIEW("MY_PRODUCTS_VIEW"),
    MY_SERVICES_VIEW("MY_SERVICES_VIEW"),

    MY_CALENDAR_VIEW("MY_CALENDAR_VIEW"),
    MY_CURRENCIES_VIEW("MY_CURRENCIES_VIEW"),

    MY_EMPLOYEES_VIEW("MY_EMPLOYEES_VIEW"),
    MY_EMPLOYMENT_REQUESTS_VIEW("MY_EMPLOYMENT_REQUESTS_VIEW"),

    NO_PROTECTION("NO_PROTECTION");

    companion object {
        fun fromKey(key: String): PermissionEnum? =
            entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<PermissionEnum> =
            keys.mapNotNull { fromKey(it) }
    }
}

fun List<PermissionEnum>.has(permission: PermissionEnum): Boolean {
    return this.contains(permission)
}