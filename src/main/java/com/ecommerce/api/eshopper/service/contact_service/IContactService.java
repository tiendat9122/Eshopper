package com.ecommerce.api.eshopper.service.contact_service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.api.eshopper.entity.Contact;

public interface IContactService {
    
    List<Contact> getAllContact();

    Optional<Contact> findContactById(Long id);

    Contact saveContact(Contact contact);

    void deleteContact(Contact contact);

    void deleteContactById(Long id);
    
}
