package com.elite.resort.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO
{

    private String phone;
    private String address;
    private String idProof;

    private String imageUrl;
}
