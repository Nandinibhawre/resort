package com.elite.resort.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "profiles")
public class Profile {

    @Id
    private String id;

    private String userId;   // from token
    private String name;     // from token
    private String email;    // from token

    private String phone;
    private String address;
    private String idProof;
    private String photoUrl;



}
