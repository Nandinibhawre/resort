package com.elite.resort.Controller;

import com.elite.resort.Model.Contact;
import com.elite.resort.Repository.ContactRepo;
import com.elite.resort.Services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepo contactRepository;


    private final ContactService contactService;

    @PostMapping
    public String sendMessage(@RequestBody Contact contact) {
        return contactService.saveMessage(contact);
    }

    // 👀 ADMIN → View all contact messages
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Contact> getAllMessages() {
        return contactService.getAllMessages();
    }

    // ❌ ADMIN → Delete contact message
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteMessage(@PathVariable String id) {
        return contactService.deleteMessage(id);
    }
}