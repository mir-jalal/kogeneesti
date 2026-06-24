package net.mirjalal.kogeneesti.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import net.mirjalal.kogeneesti.exception.InvalidFileException;
import net.mirjalal.kogeneesti.properties.StorageProperties;

@Component
@EnableConfigurationProperties(StorageProperties.class)
public class FileSystemStorage implements GeneralStorage {
    private Path rootLocation;
    private final Tika tika;

    private static final Set<String> ALLOWED_FILE_TYPES = Set.of("image/jpeg", "image/png", "image/jpg", "image/gif");

    public FileSystemStorage(Tika tika, StorageProperties properties) {
        this.tika = tika;
        this.rootLocation = Paths.get(properties.location());
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(rootLocation);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        try {
            String fileType = tika.detect(file.getInputStream());
            if (!ALLOWED_FILE_TYPES.contains(fileType)) {
                throw new InvalidFileException("Unsupported file type: " + fileType);
            }
        } catch (IOException e) {
            throw new InvalidFileException("Could not determine file type");
        }
    }

    private String createFileName(String originalFilename, String name) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        return name + "." + fileExtension;
    }

    private Path getFilePath(String name) {
        Path filePath = rootLocation.resolve(name).normalize();
        if (!filePath.startsWith(rootLocation)) {
            throw new InvalidFileException("Cannot store file outside the current directory.");
        }
        return filePath;
    }

    private void copyFile(MultipartFile file, Path destination) {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InvalidFileException("Failed to store file " + file.getOriginalFilename());
        }
    }

    private Resource loadFileAsResource(Path filePath) {
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new InvalidFileException("Could not read file: " + filePath.getFileName());
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String name) {        
        validateFile(file);
        
        String fullFileName = createFileName(file.getOriginalFilename(), name);
        Path destionationFile = getFilePath(fullFileName).toAbsolutePath();

        copyFile(file, destionationFile);

        return fullFileName;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        UUID name = UUID.randomUUID();
        return uploadFile(file, name.toString());
    }

    @Override
    public Resource serveFile(String name) {
        Path location = getFilePath(name);
        Resource resource = loadFileAsResource(location);
        
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new InvalidFileException("Could not read file: " + location.getFileName());
        }
    }
}
