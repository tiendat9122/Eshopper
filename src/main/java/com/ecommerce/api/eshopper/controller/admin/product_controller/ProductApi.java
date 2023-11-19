package com.ecommerce.api.eshopper.controller.admin.product_controller;

import com.ecommerce.api.eshopper.dto.ProductDto;
import com.ecommerce.api.eshopper.entity.Author;
import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.service.author_service.IAuthorService;
import com.ecommerce.api.eshopper.service.category_service.ICategoryService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductApi {

    private final IProductService productService;

    private final ICategoryService categoryService;

    private final IAuthorService authorService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @GetMapping("/get")
    public ResponseEntity<?> getProduct(@RequestParam(name = "id", required = false) Long id) {

        try {
            if (id != null) {
                Product product = productService.findProductById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find product by id = " + id));
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                List<Product> products = productService.findAllProduct();
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertProduct(@ModelAttribute ProductDto productDto) {

        try {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setRetail(productDto.getRetail());
            product.setInventory(productDto.getInventory());

            // add author for product
            Long authorId = productDto.getAuthorId();
            Author author = authorService.findAuthorById(authorId).orElseThrow();
            product.setAuthor(author);

            // add picture for product
            if (productDto.getPicture() == null || productDto.getPicture().isEmpty()) {
                product.setPicture(null);
            } else {
                Path path = Paths.get(FILE_DIRECTORY, "products");

                try {
                    InputStream inputStream = productDto.getPicture().getInputStream();
                    long timeStamp = new Date().getTime();
                    String fileName = productDto.getPicture().getOriginalFilename();
                    int lastDotIndex = fileName.lastIndexOf('.');
                    String extension;
                    if (lastDotIndex > 0) {
                        extension = fileName.substring(lastDotIndex);
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    String fileSave = productDto.getName() + "_" + productDto.getPicture().getName() + timeStamp
                            + extension;

                    Files.copy(inputStream, path.resolve(fileSave),
                            StandardCopyOption.REPLACE_EXISTING);

                    product.setPicture(fileSave);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // set categories for product
            List<Long> categoryIds = productDto.getCategoryIds();
            Set<Category> categories = new HashSet<>();
            if (categoryIds == null) {
                categories = new HashSet<>();
            } else {
                for (Long categoryId : categoryIds) {
                    Category category = categoryService.findCategoryById(categoryId).orElseThrow();
                    categories.add(category);
                }
            }
            product.setCategories(categories);

            Product productInserted = productService.saveProduct(product);
            return new ResponseEntity<>(productInserted, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestParam(name = "id") Long id, @ModelAttribute ProductDto productDto) {

        try {
            if (id != null) {
                Product product = productService.findProductById(id).orElseThrow();
                product.setName(productDto.getName());
                product.setRetail(productDto.getRetail());
                product.setInventory(productDto.getInventory());

                // update author for product
                Long authorId = productDto.getAuthorId();
                Author author = authorService.findAuthorById(authorId).orElseThrow();
                product.setAuthor(author);

                // update picture for product
                File fileOld = new File(FILE_DIRECTORY + "/products/" + product.getPicture());
                if (productDto.getPicture() == null || productDto.getPicture().isEmpty()) {
                    if (fileOld.exists()) {
                        fileOld.delete();
                    }
                    product.setPicture(null);
                } else {
                    try {
                        Path path = Paths.get(FILE_DIRECTORY, "products");
                        InputStream inputStream = productDto.getPicture().getInputStream();
                        long timeStamp = new Date().getTime();
                        String fileName = productDto.getPicture().getOriginalFilename();
                        int lastDotIndex = fileName.lastIndexOf('.');
                        String extension;
                        if (lastDotIndex > 0) {
                            extension = fileName.substring(lastDotIndex);
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                        String fileSave = productDto.getName() + "_" + productDto.getPicture().getName() + timeStamp
                                + extension;

                        if (fileOld.exists()) {
                            fileOld.delete();
                            Files.copy(inputStream, path.resolve(fileSave),
                                    StandardCopyOption.REPLACE_EXISTING);
                            product.setPicture(fileSave);
                        } else {
                            Files.copy(inputStream, path.resolve(fileSave),
                                    StandardCopyOption.REPLACE_EXISTING);
                            product.setPicture(fileSave);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // update categories for product
                List<Long> categoryIds = productDto.getCategoryIds();
                Set<Category> categories = new HashSet<>();
                if (categoryIds == null) {
                    categories = new HashSet<>();
                } else {
                    for (Long categoryId : categoryIds) {
                        Category category = categoryService.findCategoryById(categoryId).orElseThrow();
                        categories.add(category);
                    }
                }
                product.setCategories(categories);

                Product productInserted = productService.saveProduct(product);
                return new ResponseEntity<>(productInserted, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam(name = "id") Long id) {

        try {
            if (id != null) {
                Product product = productService.findProductById(id).orElseThrow();

                // delete book's picture on server
                try {
                    String picture = product.getPicture();
                    Path filePath = Paths.get(FILE_DIRECTORY, "products", picture);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                productService.deleteProduct(product);
                return new ResponseEntity<>("Deleted product successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
