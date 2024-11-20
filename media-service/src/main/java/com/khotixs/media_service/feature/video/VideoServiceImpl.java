package com.khotixs.media_service.feature.video;

import com.khotixs.media_service.domain.Video;
import com.khotixs.media_service.feature.minio.MinioService;
import com.khotixs.media_service.feature.video.dto.VideoResponse;
import com.khotixs.media_service.feature.video.dto.VideoViewResponse;
import com.khotixs.media_service.utils.MediaUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService {

    private final MinioService minioService;

    private final VideoRepository videoRepository;

    private final MinioClient minioClient;

    @Value("${media.base-uri}")
    private String baseUri;

    //endpoint that handle manage medias
    @Value("${media.video-end-point}")
    private String videoEndpoint;

    @Value("${minio.bucket-name}")
    String bucketName;

    String folderName = "video";

    @Override
    public VideoResponse uploadVideo(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("video/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type.");
        }

        String extension = MediaUtil.extractExtension(Objects.requireNonNull(file.getOriginalFilename()));

        String newName;
        do {
            newName = UUID.randomUUID().toString();
        } while (videoRepository.existsByFileName(newName + "." + extension));


        String objectName = folderName + "/" + newName + "." + extension;

        try {
            minioService.uploadFile(file, objectName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        //create new object that store file metadata
        Video video = new Video();

        //set all field
        video.setFileName(newName + "." + extension);

        video.setFileSize(file.getSize());

        video.setContentType(file.getContentType());

        video.setFolder(folderName);

        video.setExtension(extension);

        //save file metadata to database
        videoRepository.save(video);


        //response to DTO
        return VideoResponse.builder()
                .name(newName + "." + extension)
                .contentType(file.getContentType())
                .extension(extension)
                .size(file.getSize())
                .uri(baseUri + videoEndpoint + "/view/" + newName + "." + extension)
                .build();
    }

    @Override
    public List<VideoResponse> loadAllVideos() {
        // Fetch all images from the repository
        List<Video> images = videoRepository.findAll();

        // Map each Video entity to an VideoResponse DTO
        List<VideoResponse> responses = new ArrayList<>();
        for (Video video : images) {
            VideoResponse response = VideoResponse.builder()
                    .name(video.getFileName())
                    .contentType(video.getContentType())
                    .extension(video.getExtension())
                    .size(video.getFileSize())
                    .uri(baseUri + videoEndpoint + "/view/" + video.getFileName())
                    .build();
            responses.add(response);
        }

        return responses;
    }

    @Override
    public VideoResponse loadVideoByName(String mediaName) {

        try {
            String contentType = getContentType(mediaName);

            String extension = MediaUtil.extractExtension(mediaName);

            return VideoResponse.builder()
                    .name(mediaName)
                    .contentType(contentType)
                    .extension(extension)
                    .uri(baseUri + videoEndpoint + "/view/" + mediaName)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Override
    public VideoResponse deleteVideoByName(String mediaName) {

        try {

            String objectName = folderName + "/" + mediaName;

            minioService.deleteFile(objectName);

            return VideoResponse.builder()
                    .name(mediaName)
                    .extension(MediaUtil.extractExtension(mediaName))
                    .build();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Override
    public Resource downloadVideoByName(String mediaName) {

        try {
            String contentType = getContentType(mediaName);

            String folderName = contentType.split("/")[0];

            String objectName = folderName + "/" + mediaName;

            InputStream inputStream = minioService.getFile(objectName);

            Path tempFile = Files.createTempFile("minio", mediaName);

            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            return new UrlResource(tempFile.toUri());

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media has not been found!");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public VideoViewResponse viewVideoByFileName(String fileName) {

        // Fetch file metadata from the repository
        Video video = videoRepository.findByFileName(fileName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "File has not been found!"));

        // Construct the object path in MinIO
        Path path = Path.of(video.getFileName());
        String objectPath = video.getFolder() + "/" + path;

        // Fetch object metadata from MinIO
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectPath)
                .build();

        StatObjectResponse imageMinioMetaData;
        try {
            imageMinioMetaData = minioClient.statObject(statObjectArgs);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching file metadata from storage", e);
        }

        // Fetch the object from MinIO
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectPath)
                .build();

        InputStream inputStream;
        try {
            inputStream = minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching file from storage", e);
        }

        // Wrap the InputStream in an InputStreamResource
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        // Construct and return the response
        return VideoViewResponse.builder()
                .fileName(video.getFileName())
                .fileSize(video.getFileSize())
                .contentType(video.getContentType())
                .stream(inputStreamResource)
                .build();
    }



    public static String getContentType(String fileName) {
        Path path = Paths.get(fileName);
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            return "Unknown type";
        }
    }
}
