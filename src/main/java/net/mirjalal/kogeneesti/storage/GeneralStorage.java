package net.mirjalal.kogeneesti.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralStorage {
    public String uploadFile(MultipartFile file);
    public String uploadFile(MultipartFile file, String name);
    public Resource serveFile(String name);
}
