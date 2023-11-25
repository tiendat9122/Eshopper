package com.ecommerce.api.eshopper.controller.user.product_controller;
import com.ecommerce.api.eshopper.dto.ProductDto;
import com.ecommerce.api.eshopper.entity.Author;
import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.OrderDetail;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.service.author_service.IAuthorService;
import com.ecommerce.api.eshopper.service.category_service.ICategoryService;
import com.ecommerce.api.eshopper.service.orderdetail_service.IOrderDetailService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/product")
public class UserProductApi {

    private final IProductService productService;

    private final ICategoryService categoryService;

    private final IAuthorService authorService;

    private final IOrderDetailService orderDetailService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @GetMapping("/get")
    public ResponseEntity<?> userGetProduct(@RequestParam(name = "id", required = false) Long id) {
        
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

    @GetMapping("/find")
    public ResponseEntity<?> userFindProductByNameAndAuthor(@RequestParam(name = "name") String name) {

        try {
            if(name != null) {
                List<Product> products = productService.findProductByKeyWord(name);
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                List<Product> products = productService.findAllProduct();
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/download/{picture}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getPicture(@PathVariable(name = "picture") String picture) {

        if (!picture.equals("") || picture != null) {
            try {
                Path filename = Paths.get(FILE_DIRECTORY, "products", picture);
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
    
}
