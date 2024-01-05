package com.ecommerce.api.eshopper.controller.admin.author_controller;

import com.ecommerce.api.eshopper.dto.AuthorDto;
import com.ecommerce.api.eshopper.entity.Author;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.service.author_service.IAuthorService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorApi {

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

    @PostMapping("/insert")
    public ResponseEntity<?> insertAuthor(@RequestBody AuthorDto authorDto) {

        try {
            Author author = new Author();
            author.setName(authorDto.getName());
            author.setStory(authorDto.getStory());
            Author authorInserted = authorService.saveAuthor(author);
            return new ResponseEntity<>(authorInserted, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAuthor(@RequestParam(name = "id") Long id, @RequestBody AuthorDto authorDto) {

        try {
            Author author = authorService.findAuthorById(id).orElseThrow();
            author.setName(authorDto.getName());
            author.setStory(authorDto.getStory());
            Author authorUpdated = authorService.saveAuthor(author);
            return new ResponseEntity<>(authorUpdated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAuthor(@RequestParam(name = "id") Long id) {

        try {
            Author author = authorService.findAuthorById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find author with id = " + id));
            authorService.deleteAuthor(author);
            return new ResponseEntity<>("Author deleted successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
