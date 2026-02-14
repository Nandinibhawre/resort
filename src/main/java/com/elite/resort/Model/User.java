package com.elite.resort.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User
{
    @Id
    private String userId;

    private String name;
    private String email;
    private String password;
}
