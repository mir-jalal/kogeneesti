package net.mirjalal.kogeneesti.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;
import net.mirjalal.kogeneesti.model.dto.ImageUploadedResponseDto;
import net.mirjalal.kogeneesti.service.ImageService;
import net.mirjalal.kogeneesti.storage.GeneralStorage;
import net.mirjalal.kogeneesti.upload.ImageUploadHandler;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final GeneralStorage storage;
    private final Map<ImageType, ImageUploadHandler> imageUploadHandlers;

    @Autowired
    public ImageServiceImpl(
            GeneralStorage storage,
            List<ImageUploadHandler> handlers) {

        this.storage = storage;
        this.imageUploadHandlers = handlers.stream()
                .collect(Collectors.toMap(
                        ImageUploadHandler::getImageType,
                        Function.identity()
                ));
    }

    //TODO: Implement a better algorithm for uploading images based on the image type and the associated entity
    @Override
    public ImageUploadedResponseDto uploadImage(BigInteger id, ImageType type, MultipartFile file) {
        ImageUploadHandler handler = getImageUploadHandler(type);

        ImageUpload imageUpload = handler.findImageUploadById(id);

        if(imageUpload.isImage()) {
            String name = storage.uploadFile(file);
            imageUpload.setFileName(name);
            handler.saveImageUpload(imageUpload);
            return new ImageUploadedResponseDto(id, name, type);
        }

        throw new NotFoundException("Are you scam?");
    }

    private ImageUploadHandler getImageUploadHandler(ImageType type) {
        ImageUploadHandler handler = imageUploadHandlers.get(type);
        if(handler == null) {
            throw new RuntimeException("No handler found for image type: " + type);
        }
        return handler;
    }

    @Override
    public Resource getImage(String id) {
        return storage.serveFile(id);
    }
}
