package com.elite.resort.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contacts")
@Data
public class Contact {

    @Id
    private String contactId;

    private String name;
    private String email;
    private String phoneNo;
    private String subject;
    private String message;
}