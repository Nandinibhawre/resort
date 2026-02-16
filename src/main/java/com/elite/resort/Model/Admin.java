package com.elite.resort.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")
@Data
public class Admin
{
    @Id
    private String adminId;
    private String name;
    private String email;
    private String password;
}