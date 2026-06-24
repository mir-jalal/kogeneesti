package net.mirjalal.kogeneesti.model.dto;

import java.math.BigInteger;

import net.mirjalal.kogeneesti.enums.ImageType;

public record ImageUploadedResponseDto(
    BigInteger id,
    String name,
    ImageType imageType
) {}
