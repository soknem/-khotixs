package com.khotixs.media_service.feature.image;

import com.khotixs.media_service.feature.image.dto.ImageResponse;
import com.khotixs.media_service.feature.image.dto.ImageViewResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import io.minio.errors.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.List;

public interface ImageService {

    ImageResponse uploadSingleImage(MultipartFile file);

    List<ImageResponse> loadAllImages();

    ImageResponse loadImageByName(String mediaName);

    ImageResponse deleteImageByName(String mediaName);

    Resource downloadImageByName(String mediaName);

    ImageViewResponse viewImageByFileName(String fileName) throws InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException;


}
