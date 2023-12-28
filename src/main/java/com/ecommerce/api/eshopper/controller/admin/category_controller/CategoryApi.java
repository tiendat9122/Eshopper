package com.ecommerce.api.eshopper.controller.admin.category_controller;

import com.ecommerce.api.eshopper.dto.CategoryDto;
import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.service.category_service.ICategoryService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryApi {

    private final ICategoryService categoryService;

    private final IProductService productService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @GetMapping("/get")
    public ResponseEntity<?> getCategory(@RequestParam(name = "id", required = false) Long id) {
        try {
            if (id != null) {
                Category category = categoryService.findCategoryById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));
                return new ResponseEntity<>(category, HttpStatus.OK);
            } else {
                List<Category> categories = categoryService.findAllCategory();
                return new ResponseEntity<>(categories, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/download/{picture}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getPicture(@PathVariable(name = "picture") String picture) {

        if (!picture.equals("") || picture != null) {
            try {
                Path filename = Paths.get(FILE_DIRECTORY, "categories", picture);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertCategory(@ModelAttribute CategoryDto categoryDto) {

        try {
            Category category = new Category();
            category.setName(categoryDto.getName());

            // add picture for category
            if (categoryDto.getPicture_file() == null || categoryDto.getPicture_file().isEmpty()) {
                category.setPicture(null);
            } else {
                Path path = Paths.get(FILE_DIRECTORY, "categories");

                try {
                    InputStream inputStream = categoryDto.getPicture_file().getInputStream();
                    long timeStamp = new Date().getTime();
                    String fileName = categoryDto.getPicture_file().getOriginalFilename();
                    int lastDotIndex = fileName.lastIndexOf('.');
                    String extension;
                    if (lastDotIndex > 0) {
                        extension = fileName.substring(lastDotIndex);
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    String fileSave = categoryDto.getName() + "_" + categoryDto.getPicture_file().getName() + timeStamp
                            + extension;

                    Files.copy(inputStream, path.resolve(fileSave),
                            StandardCopyOption.REPLACE_EXISTING);

                    category.setPicture(fileSave);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Category categoryInserted = categoryService.saveCategory(category);

            return new ResponseEntity<>(categoryInserted, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<?> updateCategory(@RequestParam(name = "id") Long id, @ModelAttribute CategoryDto categoryDto) {
        try {
            if (id != null) {
                Category category = categoryService.findCategoryById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));
                category.setName(categoryDto.getName());

                // update picture for category
                File fileOld = new File(FILE_DIRECTORY + "/categories/" + category.getPicture());
                if (categoryDto.getPicture_file() != null && !categoryDto.getPicture_file().isEmpty()) {
                    try {
                        Path path = Paths.get(FILE_DIRECTORY, "categories");
                        InputStream inputStream = categoryDto.getPicture_file().getInputStream();
                        long timeStamp = new Date().getTime();
                        String fileName = categoryDto.getPicture_file().getOriginalFilename();
                        int lastDotIndex = fileName.lastIndexOf('.');
                        String extension;
                        if(lastDotIndex > 0) {
                            extension = fileName.substring(lastDotIndex);
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                        String fileSave = categoryDto.getName() + "_" + categoryDto.getPicture_file().getName() + timeStamp + extension;

                        if(fileOld.exists()) {
                            fileOld.delete();
                            Files.copy(inputStream, path.resolve(fileSave), StandardCopyOption.REPLACE_EXISTING);
                            category.setPicture(fileSave);
                        } else {
                            Files.copy(inputStream, path.resolve(fileSave), StandardCopyOption.REPLACE_EXISTING);
                            category.setPicture(fileSave);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (categoryDto.getPicture().isBlank()) {
                    if(fileOld.exists()) {
                        fileOld.delete();
                    }
                    category.setPicture(null);
                }

                Category categoryUpdated = categoryService.saveCategory(category);

                return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("I don't know what role you want to for update", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam(name = "id") Long id) {

        try {
            if (id != null) {
                Category category = categoryService.findCategoryById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));

                // delete book's picture on server
                try {
                    String picture = category.getPicture();
                    Path filePath = Paths.get(FILE_DIRECTORY, "categories", picture);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                categoryService.deleteCategory(category);
                return new ResponseEntity<>("Deleted category successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("I don't know what category you want to for delete?",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
