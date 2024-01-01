package com.ecommerce.api.eshopper.controller.user.home_controller;

import com.ecommerce.api.eshopper.dto.ProductQuantityDto;
import com.ecommerce.api.eshopper.entity.Advertise;
import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.Slide;
import com.ecommerce.api.eshopper.service.advertise_service.IAdvertiseService;
import com.ecommerce.api.eshopper.service.category_service.ICategoryService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import com.ecommerce.api.eshopper.service.slide_service.ISlideService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/home")
public class HomeApi {

    private final ISlideService slideService;

    private final ICategoryService categoryService;

    private final IAdvertiseService advertiseService;

    private final IProductService productService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    // Slide Carousel
    @GetMapping("/carousel")
    public ResponseEntity<?> getSlide() {

        try {

            List<Slide> slides = slideService.getAllSlideActiveTrue();
            return new ResponseEntity<>(slides, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/slide/download/{picture}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> userGetSlidePicture(@PathVariable(name = "picture") String picture) {

        if (!picture.equals("") || picture != null) {
            try {
                Path filename = Paths.get(FILE_DIRECTORY, "slides", picture);
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

    // Categories
    @GetMapping("/category")
    public ResponseEntity<?> getHomeCategory() {

        try {

            List<Category> categories = categoryService.find6Category();
            return new ResponseEntity<>(categories, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/category/download/{picture}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getCategoryPicture(@PathVariable(name = "picture") String picture) {

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

    // Trending
    @GetMapping("/trending")
    public ResponseEntity<?> getHomeTrending() {

        try {

            List<Product> products = productService.getAllProductHotActiveTrue();
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    // Best Seller
    @GetMapping("/bestseller")
    public ResponseEntity<?> getProductQuantitySold() {

        try {
            List<ProductQuantityDto> productQuantityDtos = productService.getAllProductQuantitySold();
            return new ResponseEntity<>(productQuantityDtos, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }

    // Advertisements
    @GetMapping("/advertise")
    public ResponseEntity<?> getHomeAdvertise() {

        try {

            List<Advertise> advertises = advertiseService.getAllAdvertiseActiveTrue();
            return new ResponseEntity<>(advertises, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/advertise/download/{picture}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getPicture(@PathVariable(name = "picture") String picture) {

        if (!picture.equals("") || picture != null) {
            try {
                Path filename = Paths.get(FILE_DIRECTORY, "advertises", picture);
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
