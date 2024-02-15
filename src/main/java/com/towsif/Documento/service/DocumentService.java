package com.towsif.Documento.service;

import com.towsif.Documento.entity.Document;
import com.towsif.Documento.entity.UserEntity;
import com.towsif.Documento.repository.DocumentRepository;
import com.towsif.Documento.repository.UserEntityRepository;
import com.towsif.Documento.security.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DocumentService
{
    private final DocumentRepository documentRepository;

    private final FileStorageService fileStorageService;

    private final UserEntityRepository userEntityRepository;

    public DocumentService(DocumentRepository documentRepository, FileStorageService fileStorageService, UserEntityRepository userEntityRepository)
    {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.userEntityRepository = userEntityRepository;
    }


    public List<Document> findAllDocs(String email)
    {
        return documentRepository.findByUserEmail(email);
    }

    private Path getCurrentPath()
    {
        String currentUserEmail = SecurityUtil.getSessionUser();

        return Paths.get(fileStorageService.getRoot().toAbsolutePath() + "/" + currentUserEmail);
    }

    @Transactional
    public void upload(MultipartFile file, HttpServletRequest request) throws IOException
    {
        Path currentPath = getCurrentPath();

        Files.createDirectories(currentPath);

        fileStorageService.upload(file, currentPath);

        Document document = new Document();

        document.setName(file.getOriginalFilename());

        String url = ServletUriComponentsBuilder.fromRequestUri(request)
                        .replacePath(null)
                        .build()
                        .toUriString()
                        .concat("/documents/" + file.getOriginalFilename());

        document.setUrl(url);

        UserEntity user = userEntityRepository.findByEmail(SecurityUtil.getSessionUser())
                .orElseThrow();

        document.setUser(user);

        documentRepository.save(document);
    }

    public Resource download(String filename) throws MalformedURLException
    {
        Path currentPath = getCurrentPath();

        return fileStorageService.download(filename, currentPath);
    }
}
