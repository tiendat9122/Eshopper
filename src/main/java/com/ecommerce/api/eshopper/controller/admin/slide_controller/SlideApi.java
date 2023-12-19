package com.ecommerce.api.eshopper.controller.admin.slide_controller;

import com.ecommerce.api.eshopper.dto.SlideDto;
import com.ecommerce.api.eshopper.entity.Slide;
import com.ecommerce.api.eshopper.service.slide_service.ISlideService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slide")
public class SlideApi {

    private final ISlideService slideService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @GetMapping("/get")
    public ResponseEntity<?> getSlide(@RequestParam(name = "id", required = false) Long id) {

        try {
            if (id != null) {
                Slide slide = slideService.findSlideById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find slide with id = " + id));
                return new ResponseEntity<>(slide, HttpStatus.OK);
            } else {
                List<Slide> slides = slideService.getAllSlide();
                return new ResponseEntity<>(slides, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertSlide(@ModelAttribute SlideDto slideDto) {

        try {
            Slide slide = new Slide();
            slide.setName(slideDto.getName());
            slide.setTheme(slideDto.getTheme());
            slide.setActive(slideDto.isActive());

            // add picture for product
            if (slideDto.getPicture_file() == null || slideDto.getPicture_file().isEmpty()) {
                slide.setPicture(null);
            } else {
                Path path = Paths.get(FILE_DIRECTORY, "slides");

                try {
                    InputStream inputStream = slideDto.getPicture_file().getInputStream();
                    long timeStamp = new Date().getTime();
                    String fileName = slideDto.getPicture_file().getOriginalFilename();
                    int lastDotIndex = fileName.lastIndexOf('.');
                    String extension;
                    if (lastDotIndex > 0) {
                        extension = fileName.substring(lastDotIndex);
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    String fileSave = slideDto.getName() + "_" + slideDto.getPicture_file().getName() + timeStamp
                            + extension;

                    Files.copy(inputStream, path.resolve(fileSave),
                            StandardCopyOption.REPLACE_EXISTING);

                    slide.setPicture(fileSave);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Slide slideInserted = slideService.saveSlide(slide);

            return new ResponseEntity<>(slideInserted, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSlide(@RequestParam(name = "id") Long id, @ModelAttribute SlideDto slideDto) {

        try {

            if (id != null) {

                Slide slide = slideService.findSlideById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find slide with id = " + id));
                slide.setName(slideDto.getName());
                slide.setTheme(slideDto.getTheme());
                slide.setActive(slideDto.isActive());

                // update picture for product
                File fileOld = new File(FILE_DIRECTORY + "/slides/" + slide.getPicture());
                if (slideDto.getPicture_file() != null && !slideDto.getPicture_file().isEmpty()) {
                    try {
                        Path path = Paths.get(FILE_DIRECTORY, "slides");
                        InputStream inputStream = slideDto.getPicture_file().getInputStream();
                        long timeStamp = new Date().getTime();
                        String fileName = slideDto.getPicture_file().getOriginalFilename();
                        int lastDotIndex = fileName.lastIndexOf('.');
                        String extension;
                        if(lastDotIndex > 0) {
                            extension = fileName.substring(lastDotIndex);
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                        String fileSave = slideDto.getName() + "_" + slideDto.getPicture_file().getName() + timeStamp + extension;

                        if(fileOld.exists()) {
                            fileOld.delete();
                            Files.copy(inputStream, path.resolve(fileSave), StandardCopyOption.REPLACE_EXISTING);
                            slide.setPicture(fileSave);
                        } else {
                            Files.copy(inputStream, path.resolve(fileSave), StandardCopyOption.REPLACE_EXISTING);
                            slide.setPicture(fileSave);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (slideDto.getPicture().isBlank()) {
                    if(fileOld.exists()) {
                        fileOld.delete();
                    }
                    slide.setPicture(null);
                }

                Slide slideUpdated = slideService.saveSlide(slide);

                return new ResponseEntity<>(slideUpdated, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Cannot find any slide", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSlide(@RequestParam(name = "id") Long id) {

        try {

            if (id != null) {
                Slide slide = slideService.findSlideById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find slide with id = " + id));

                // delete picture on server
                try {
                    String picture = slide.getPicture();
                    Path filePath = Paths.get(FILE_DIRECTORY, "slides", picture);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                slideService.deleteSlide(slide);
                return new ResponseEntity<>("Deleted slide successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus. NOT_FOUND);
        }

    }

    @PutMapping("/active")
    public ResponseEntity<?> activeSlide(@RequestBody SlideDto slideDto) {

        try {
            Long slideId = slideDto.getId();
            Slide slide = slideService.findSlideById(slideId).orElseThrow(() -> new EntityNotFoundException("Cannot find product with id = " + slideId));

            slide.setActive(slideDto.isActive());
            Slide slideActived = slideService.saveSlide(slide);
            return new ResponseEntity<>(slideActived, HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/download/{picture}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getPicture(@PathVariable(name = "picture") String picture) {

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

}
