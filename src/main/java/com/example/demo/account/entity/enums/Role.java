package com.example.demo.account.entity.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    CLIENT("ROLE_CLIENT"), SUPERVISOR("ROLE_SUPERVISOR");
    private final String grantedAuthority;
}