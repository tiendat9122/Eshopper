package com.ecommerce.api.eshopper.controller.user.contact_controller;

import com.ecommerce.api.eshopper.dto.ContactDto;
import com.ecommerce.api.eshopper.entity.Contact;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.contact_service.IContactService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/contact")
public class UserContactApi {

    private final IContactService contactService;

    private final IUserService userService;


     @PostMapping("/send")
     public ResponseEntity<?> userSendContact(@RequestParam(name = "id") Long id, @RequestBody ContactDto contactDto) {

         try {

             if (id != null) {
                 Contact contact = new Contact();
                 contact.setTitle(contactDto.getTitle());
                 contact.setContent(contactDto.getContent());

                 User user = userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + id));
                 contact.setUser(user);

                 LocalDateTime now = LocalDateTime.now();
                 contact.setContactDate(now);

                 Contact contactSent = contactService.saveContact(contact);

                 return new ResponseEntity<>(contactSent, HttpStatus.OK);
             } else {
                 return new ResponseEntity<>("Cannot send your contact", HttpStatus.BAD_REQUEST);
             }

         } catch (EntityNotFoundException e) {
             return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
         }
     }
    
}
