package com.example.shop.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String password;
    private String avatar;
    private String fullName;
    private String email;
    private String phone;
    private Set<RoleResponse> roles;
    //private List<AddressResponse> addresses;
}
