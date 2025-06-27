package com.example.scrollbooker.entity.auth.data.remote

enum class RoleNameEnum(val key: String) {
    BUSINESS("business"),
    SUPER_ADMIN("super_admin"),
    EMPLOYEE("employee"),
    MANAGER("manager"),
    CLIENT("client");

    companion object {
        fun fromKey(key: String): RoleNameEnum? =
            RoleNameEnum.entries.find { it.key == key }
    }
}