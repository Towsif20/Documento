package com.towsif.Documento.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileStorageService
{
    private final Path root = Paths.get("./uploads");

    public Path getRoot()
    {
        return root;
    }

    public void init() throws IOException
    {
        Files.createDirectories(root);
    }

    public void upload(MultipartFile file, Path currentPath) throws IOException
    {

        Files.copy(file.getInputStream(), currentPath.resolve(Objects.requireNonNull(file.getOriginalFilename())));

    }

    public Resource download(String filename, Path currentPath) throws MalformedURLException
    {
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

}
