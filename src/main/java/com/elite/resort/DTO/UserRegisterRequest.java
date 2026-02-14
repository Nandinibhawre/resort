package com.elite.resort.DTO;

import lombok.Data;


    @Data
    public class UserRegisterRequest {
        private String name;
        private String email;
        private String password;
    }

