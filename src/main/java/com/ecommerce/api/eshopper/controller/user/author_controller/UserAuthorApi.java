package com.ecommerce.api.eshopper.controller.user.author_controller;

import com.ecommerce.api.eshopper.entity.Author;
import com.ecommerce.api.eshopper.service.author_service.IAuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/author")
public class UserAuthorApi {

    private final IAuthorService authorService;

    @GetMapping("/get")
    public ResponseEntity<?> getAuthor(@RequestParam(name = "id", required = false) Long id) {
        try {
            if (id != null) {
                Author author = authorService.findAuthorById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find author with id = " + id));
                return new ResponseEntity<>(author, HttpStatus.OK);
            } else {
                List<Author> authors = authorService.getAllAuthor();
                return new ResponseEntity<>(authors, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
