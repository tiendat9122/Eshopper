package com.ecommerce.api.eshopper.controller.admin.contact_controller;

import com.ecommerce.api.eshopper.dto.ContactDto;
import com.ecommerce.api.eshopper.dto.FeedbackDto;
import com.ecommerce.api.eshopper.entity.Contact;
import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.contact_service.IContactService;
import com.ecommerce.api.eshopper.service.feedback_service.IFeedbackService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactApi {

    private final IContactService contactService;

    private final IUserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getContact(@RequestParam(name = "id", required = false) Long id) {

        try {
            if(id != null) {
                Contact contact = contactService.findContactById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find contact with id = " + id));
                return new ResponseEntity<>(contact, HttpStatus.OK);
            } else {
                List<Contact> contacts = contactService.getAllContact();
                return new ResponseEntity<>(contacts, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertContact(@RequestBody ContactDto contactDto) {

        try {
            Contact contact = new Contact();
            contact.setTitle(contactDto.getTitle());
            contact.setContent(contactDto.getContent());

            contact.setContactDate(contactDto.getContactDate());

            Long userId = contactDto.getUserId();
            if(userId != null) {
                User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("Cannot find user with userid = " + userId));
                contact.setUser(user);
            } else {
                contact.setUser(null);
            }

            Contact contactInserted = contactService.saveContact(contact);

            return new ResponseEntity<>(contactInserted, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateContact(@RequestParam(name = "id") Long id, @RequestBody ContactDto contactDto) {

        try {
            Contact contact = contactService.findContactById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find contact with id = " + id));
            contact.setTitle(contactDto.getTitle());
            contact.setContent(contactDto.getContent());
            contact.setContactDate(contactDto.getContactDate());

            Long userId = contactDto.getUserId();
            if(id != null) {
                User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));
                contact.setUser(user);
            } else {
                contact.setUser(null);
            }

            Contact contactUpdated = contactService.saveContact(contact);

            return new ResponseEntity<>(contactUpdated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContact(@RequestParam(name = "id") Long id) {

        try {
            if(id != null) {
                Contact contact = contactService.findContactById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find contact with id = " + id));
                contactService.deleteContact(contact);
                return new ResponseEntity<>("Contact deleted successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("required for id of contact!", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("delete contact fail!", HttpStatus.NOT_FOUND);
        }

    }

}
