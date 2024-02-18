package com.towsif.Documento.service;

import com.towsif.Documento.entity.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileStorageService
{
    private final Path root = Paths.get("./uploads");

    public void init() throws IOException
    {
        Files.createDirectories(root);
    }

    public void upload(MultipartFile file, String username) throws IOException
    {
        Path currentPath = root.resolve(username);
        Files.createDirectories(currentPath);

        System.out.println("current path = " + currentPath);

        Files.copy(file.getInputStream(), currentPath.resolve(Objects.requireNonNull(file.getOriginalFilename())));

    }

    public Resource download(Document document) throws MalformedURLException
    {
        String filename = document.getName();

        Path currentPath = root.resolve(document.getUser().getUsername());
        Path file = currentPath.resolve(filename);

        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable())
        {
            return resource;
        }
        else
        {
            throw new RuntimeException("Could not read the file!");
        }
    }

    public void delete(Document document) throws IOException
    {
        String filename = document.getName();

        Path currentPath = root.resolve(document.getUser().getUsername());
        Path file = currentPath.resolve(filename);

        Files.deleteIfExists(file);
    }

}
