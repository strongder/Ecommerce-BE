package com.example.shop.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AddressRequest {
    private Long id;
    private String city;
    private String district;
    private String ward;
    private String addressDetail;
    private Long userId;
}
