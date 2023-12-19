package com.ecommerce.api.eshopper.controller.admin.advertise_controller;

import com.ecommerce.api.eshopper.dto.AdvertiseDto;
import com.ecommerce.api.eshopper.dto.SlideDto;
import com.ecommerce.api.eshopper.entity.Advertise;
import com.ecommerce.api.eshopper.entity.Slide;
import com.ecommerce.api.eshopper.service.advertise_service.IAdvertiseService;
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
@RequestMapping("/advertise")
public class AdvertiseApi {

    private final IAdvertiseService advertiseService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @GetMapping("/get")
    public ResponseEntity<?> getAdvertise(@RequestParam(name = "id", required = false) Long id) {

        try {
            if (id != null) {
                Advertise advertise = advertiseService.findAdvertiseById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find advertise with id = " + id));
                return new ResponseEntity<>(advertise, HttpStatus.OK);
            } else {
                List<Advertise> advertises = advertiseService.getAllAdvertise();
                return new ResponseEntity<>(advertises, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertAdvertise(@ModelAttribute AdvertiseDto advertiseDto) {

        try {
            Advertise advertise = new Advertise();
            advertise.setName(advertiseDto.getName());
            advertise.setBrand(advertiseDto.getBrand());
            advertise.setActive(advertiseDto.isActive());

            // add picture for product
            if (advertiseDto.getPicture_file() == null || advertiseDto.getPicture_file().isEmpty()) {
                advertise.setPicture(null);
            } else {
                Path path = Paths.get(FILE_DIRECTORY, "advertises");

                try {
                    InputStream inputStream = advertiseDto.getPicture_file().getInputStream();
                    long timeStamp = new Date().getTime();
                    String fileName = advertiseDto.getPicture_file().getOriginalFilename();
                    int lastDotIndex = fileName.lastIndexOf('.');
                    String extension;
                    if (lastDotIndex > 0) {
                        extension = fileName.substring(lastDotIndex);
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    String fileSave = advertiseDto.getName() + "_" + advertiseDto.getPicture_file().getName() + timeStamp
                            + extension;

                    Files.copy(inputStream, path.resolve(fileSave),
                            StandardCopyOption.REPLACE_EXISTING);

                    advertise.setPicture(fileSave);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Advertise advertiseInserted = advertiseService.saveAdvertise(advertise);

            return new ResponseEntity<>(advertiseInserted, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAdvertise(@RequestParam(name = "id") Long id, @ModelAttribute AdvertiseDto advertiseDto) {

        try {

            if (id != null) {

                Advertise advertise = advertiseService.findAdvertiseById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find advertise with id = " + id));
                advertise.setName(advertiseDto.getName());
                advertise.setBrand(advertiseDto.getBrand());
                advertise.setActive(advertiseDto.isActive());

                // update picture for product
                File fileOld = new File(FILE_DIRECTORY + "/advertises/" + advertise.getPicture());
                if (advertiseDto.getPicture_file() != null && !advertiseDto.getPicture_file().isEmpty()) {
                    try {
                        Path path = Paths.get(FILE_DIRECTORY, "advertises");
                        InputStream inputStream = advertiseDto.getPicture_file().getInputStream();
                        long timeStamp = new Date().getTime();
                        String fileName = advertiseDto.getPicture_file().getOriginalFilename();
                        int lastDotIndex = fileName.lastIndexOf('.');
                        String extension;
                        if(lastDotIndex > 0) {
                            extension = fileName.substring(lastDotIndex);
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                        String fileSave = advertiseDto.getName() + "_" + advertiseDto.getPicture_file().getName() + timeStamp + extension;

                        if(fileOld.exists()) {
                            fileOld.delete();
                            Files.copy(inputStream, path.resolve(fileSave), StandardCopyOption.REPLACE_EXISTING);
                            advertise.setPicture(fileSave);
                        } else {
                            Files.copy(inputStream, path.resolve(fileSave), StandardCopyOption.REPLACE_EXISTING);
                            advertise.setPicture(fileSave);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (advertiseDto.getPicture().isBlank()) {
                    if(fileOld.exists()) {
                        fileOld.delete();
                    }
                    advertise.setPicture(null);
                }

                Advertise advertiseUpdated = advertiseService.saveAdvertise(advertise);

                return new ResponseEntity<>(advertiseUpdated, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Cannot find any slide", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAdvertise(@RequestParam(name = "id") Long id) {

        try {

            if (id != null) {
                Advertise advertise = advertiseService.findAdvertiseById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find advertise with id = " + id));

                // delete picture on server
                try {
                    String picture = advertise.getPicture();
                    Path filePath = Paths.get(FILE_DIRECTORY, "advertises", picture);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                advertiseService.deleteAdvertise(advertise);
                return new ResponseEntity<>("Deleted advertise successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus. NOT_FOUND);
        }

    }

    @PutMapping("/active")
    public ResponseEntity<?> activeAdvertise(@RequestBody AdvertiseDto advertiseDto) {

        try {
            Long advertiseId = advertiseDto.getId();
            Advertise advertise = advertiseService.findAdvertiseById(advertiseId).orElseThrow(() -> new EntityNotFoundException("Cannot find advertise with id = " + advertiseId));

            advertise.setActive(advertiseDto.isActive());
            Advertise advertiseActived = advertiseService.saveAdvertise(advertise);
            return new ResponseEntity<>(advertiseActived, HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/download/{picture}")
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
