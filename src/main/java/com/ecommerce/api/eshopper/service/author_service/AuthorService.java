package com.ecommerce.api.eshopper.service.author_service;

import com.ecommerce.api.eshopper.entity.Author;
import com.ecommerce.api.eshopper.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorService implements IAuthorService {

    private final AuthorRepository authorRepository;

    // Get and find
    @Override
    public List<Author> getAllAuthor() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public Optional<Author> findAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    // Save and update
    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Remove
    @Override
    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }

    @Override
    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }
}
