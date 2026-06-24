package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.model.dto.ImageUploadedResponseDto;

public interface ImageService {
    public ImageUploadedResponseDto uploadImage(BigInteger id, ImageType type, MultipartFile file);
	public Resource getImage(String id);
}
