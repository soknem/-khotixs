package com.khotixs.media_service.feature.image;

import com.khotixs.media_service.feature.image.dto.ImageResponse;
import com.khotixs.media_service.feature.image.dto.ImageViewResponse;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/test")
    @PreAuthorize("hasAnyAuthority('user:read','file:read')")
    public String test(@RequestBody String test){
        return test;
    }

    @Operation(
            summary = "Upload an image",
            description = "Allows uploading a single image.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Image uploaded successfully"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('user:read','file:read')")
    @PostMapping(value = "",consumes = "multipart/form-data")
    ImageResponse uploadImage(@RequestPart MultipartFile file) {

        return imageService.uploadSingleImage(file);
    }

    @Operation(
            summary = "Load all images",
            description = "Retrieves a list of all uploaded images.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of images retrieved successfully"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content)
            }
    )
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('user:read','file:read')")
    List<ImageResponse> loadAllMedia() {
        return imageService.loadAllImages();
    }


    @GetMapping("/{mediaName}")
    @PreAuthorize("hasAnyAuthority('file:write')")
    ImageResponse loadMediaByName(@PathVariable String mediaName) {
        return imageService.loadImageByName(mediaName);
    }

    @DeleteMapping("/{mediaName}")
    ImageResponse deleteMediaByName(@PathVariable String mediaName) {
        return imageService.deleteImageByName(mediaName);
    }

    // produces = Accept
    // consumes = Content-Type
    @GetMapping(path = "/{mediaName}/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> downloadImageByName(@PathVariable String mediaName) {

        System.out.println("Start download");

        Resource resource = imageService.downloadImageByName(mediaName);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + mediaName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping(value = "/view/{fileName}")
    public ResponseEntity<InputStreamResource> viewByFileName(@PathVariable String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException {
        ImageViewResponse file = imageService.viewImageByFileName(fileName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .contentLength(file.fileSize())
                .body(file.stream());
    }

}
