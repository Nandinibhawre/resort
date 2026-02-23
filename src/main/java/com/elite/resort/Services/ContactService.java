package com.elite.resort.Services;


import com.elite.resort.Model.Contact;
import com.elite.resort.Repository.ContactRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepo contactRepository;

    // ✅ Save contact message (User)
    public String saveMessage(Contact contact) {
        contactRepository.save(contact);
        return "Message submitted successfully";
    }

    // ✅ Get all contact messages (Admin)
    public List<Contact> getAllMessages() {
        return contactRepository.findAll();
    }

    // ✅ Delete contact message (Admin)
    public String deleteMessage(String id) {

        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Message not found");
        }

        contactRepository.deleteById(id);
        return "Message deleted successfully";
    }
}