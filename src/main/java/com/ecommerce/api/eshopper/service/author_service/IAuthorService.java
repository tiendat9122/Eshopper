package com.ecommerce.api.eshopper.service.author_service;

import com.ecommerce.api.eshopper.entity.Author;

import java.util.List;
import java.util.Optional;

public interface IAuthorService {

    // Get and find
    List<Author> getAllAuthor();

    Optional<Author> findAuthorById(Long id);

    Optional<Author> findAuthorByName(String name);

    // Save and update
    Author saveAuthor(Author author);

    // Remove
    void deleteAuthor(Author author);

    void deleteAuthorById(Long id);

}
