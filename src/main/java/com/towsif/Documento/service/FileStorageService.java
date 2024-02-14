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

    public void init() throws IOException
    {
        Files.createDirectories(root);
    }

    public void save(MultipartFile file)
    {
        try
        {
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        }
        catch (IOException e)
        {
            if(e instanceof FileAlreadyExistsException)
                throw new RuntimeException("File already exists");

            throw new RuntimeException(e.getMessage());
        }
    }

    public org.springframework.core.io.Resource load(String filename)
    {
        try
        {
            Path file = root.resolve(filename);
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
        catch (MalformedURLException e)
        {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
