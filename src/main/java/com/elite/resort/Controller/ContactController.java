package com.elite.resort.Controller;

import com.elite.resort.Model.Contact;
import com.elite.resort.Repository.ContactRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepo contactRepository;

    @PostMapping
    public String sendMessage(@RequestBody Contact contact)
    {
        contactRepository.save(contact);
        return "Message submitted successfully";
    }
}
