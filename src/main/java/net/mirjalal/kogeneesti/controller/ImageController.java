package net.mirjalal.kogeneesti.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.model.dto.ImageUploadedResponseDto;
import net.mirjalal.kogeneesti.service.ImageService;

import java.math.BigInteger;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{type}/{id}")
    public ResponseEntity<ImageUploadedResponseDto> uploadImage(@PathVariable BigInteger id, @PathVariable ImageType type, @RequestParam("file") MultipartFile file) {
        ImageUploadedResponseDto response = imageService.uploadImage(id, type, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{type}/{id}", produces = "image/*")
    public ResponseEntity<Resource> getImage(@PathVariable ImageType type, @PathVariable String id) {
        Resource response = imageService.getImage(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response);
    }
}
