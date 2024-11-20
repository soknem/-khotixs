package com.khotixs.media_service.feature.image;

import com.khotixs.media_service.domain.Image;
import com.khotixs.media_service.feature.image.dto.ImageViewResponse;
import com.khotixs.media_service.feature.minio.MinioService;
import com.khotixs.media_service.feature.image.dto.ImageResponse;
import com.khotixs.media_service.utils.MediaUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final MinioService minioService;

    private final ImageRepository imageRepository;

    private final MinioClient minioClient;

    @Value("${media.base-uri}")
    private String baseUri;

    //endpoint that handle manage medias
    @Value("${media.image-end-point}")
    private String imageEndpoint;

    @Value("${minio.bucket-name}")
    String bucketName;

    String folderName = "image";

    @Override
    public ImageResponse uploadSingleImage(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type.");
        }

        String extension = MediaUtil.extractExtension(Objects.requireNonNull(file.getOriginalFilename()));

        String newName;
        do {
            newName = UUID.randomUUID().toString();
        } while (imageRepository.existsByFileName(newName + "." + extension));


        String objectName = folderName + "/" + newName + "." + extension;

        try {
            minioService.uploadFile(file, objectName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        //create new object that store file metadata
        Image image = new Image();

        //set all field
        image.setFileName(newName + "." + extension);

        image.setFileSize(file.getSize());

        image.setContentType(file.getContentType());

        image.setFolder(folderName);

        image.setExtension(extension);

        //save file metadata to database
        imageRepository.save(image);


        //response to DTO
        return ImageResponse.builder()
                .name(newName + "." + extension)
                .contentType(file.getContentType())
                .extension(extension)
                .size(file.getSize())
                .uri(baseUri + imageEndpoint + "/view/" + newName + "." + extension)
                .build();
    }

    @Override
    public List<ImageResponse> loadAllImages() {
        // Fetch all images from the repository
        List<Image> images = imageRepository.findAll();

        // Map each Image entity to an ImageResponse DTO
        List<ImageResponse> responses = new ArrayList<>();
        for (Image image : images) {
            ImageResponse response = ImageResponse.builder()
                    .name(image.getFileName())
                    .contentType(image.getContentType())
                    .extension(image.getExtension())
                    .size(image.getFileSize())
                    .uri(baseUri + imageEndpoint + "/view/" + image.getFileName())
                    .build();
            responses.add(response);
        }

        return responses;
    }

    @Override
    public ImageResponse loadImageByName(String mediaName) {

        try {
            String contentType = getContentType(mediaName);

            String extension = MediaUtil.extractExtension(mediaName);

            return ImageResponse.builder()
                    .name(mediaName)
                    .contentType(contentType)
                    .extension(extension)
                    .uri(baseUri + imageEndpoint + "/view/" + mediaName)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Override
    public ImageResponse deleteImageByName(String mediaName) {

        try {

            String objectName = folderName + "/" + mediaName;

            minioService.deleteFile(objectName);

            return ImageResponse.builder()
                    .name(mediaName)
                    .extension(MediaUtil.extractExtension(mediaName))
                    .build();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Override
    public Resource downloadImageByName(String mediaName) {

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
    public ImageViewResponse viewImageByFileName(String fileName) {

        // Fetch file metadata from the repository
        Image image = imageRepository.findByFileName(fileName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "File has not been found!"));

        // Construct the object path in MinIO
        Path path = Path.of(image.getFileName());
        String objectPath = image.getFolder() + "/" + path;

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
        return ImageViewResponse.builder()
                .fileName(image.getFileName())
                .fileSize(image.getFileSize())
                .contentType(image.getContentType())
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
