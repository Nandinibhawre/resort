package com.elite.resort.Repository;

import com.elite.resort.Model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepo extends MongoRepository<Contact, String>
{


}