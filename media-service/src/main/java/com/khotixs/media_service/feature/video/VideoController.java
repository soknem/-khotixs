package com.khotixs.media_service.feature.video;

import com.khotixs.media_service.feature.video.dto.VideoResponse;
import com.khotixs.media_service.feature.image.dto.ImageViewResponse;
import com.khotixs.media_service.feature.video.dto.VideoViewResponse;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "",consumes = "multipart/form-data")
    VideoResponse uploadImage(@RequestPart MultipartFile file) {

        return videoService.uploadVideo(file);
    }

    @GetMapping()
    List<VideoResponse> loadMediaByName() {
        return videoService.loadAllVideos();
    }


    @GetMapping("/{mediaName}")
    VideoResponse loadMediaByName(@PathVariable String mediaName) {
        return videoService.loadVideoByName(mediaName);
    }

    @DeleteMapping("/{mediaName}")
    VideoResponse deleteMediaByName(@PathVariable String mediaName) {
        return videoService.deleteVideoByName(mediaName);
    }

    // produces = Accept
    // consumes = Content-Type
    @GetMapping(path = "/{mediaName}/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> downloadMediaByName(@PathVariable String mediaName) {

        System.out.println("Start download");

        Resource resource = videoService.downloadVideoByName(mediaName);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + mediaName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping(value = "/view/{fileName}")
    public ResponseEntity<InputStreamResource> viewByFileName(@PathVariable String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException {
        VideoViewResponse file = videoService.viewVideoByFileName(fileName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .contentLength(file.fileSize())
                .body(file.stream());
    }

}
