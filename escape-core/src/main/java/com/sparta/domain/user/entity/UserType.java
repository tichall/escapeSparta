package com.sparta.domain.user.entity;

public enum UserType {
    USER,
    ADMIN,
    MANAGER;

        public String  getAuthority() {
            return switch (this) {
                case USER -> "USER";
                case ADMIN -> "ADMIN";
                case MANAGER -> "MANAGER";
                default -> throw new IllegalArgumentException("해당 권한은 존재하지 않습니다.");
            }; // 예외 변경
        }
}
