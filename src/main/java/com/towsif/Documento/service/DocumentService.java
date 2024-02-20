package com.towsif.Documento.service;

import com.towsif.Documento.entity.Document;
import com.towsif.Documento.entity.Role;
import com.towsif.Documento.entity.UserEntity;
import com.towsif.Documento.repository.DocumentRepository;
import com.towsif.Documento.repository.UserEntityRepository;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.ArrayList;
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

    @Transactional
    public void upload(MultipartFile file, String username) throws IOException
    {
        fileStorageService.upload(file, username);

        Document document = new Document();

        document.setName(file.getOriginalFilename());

        UserEntity user = userEntityRepository.findByEmail(username)
                .orElseThrow();

        document.setUser(user);

        documentRepository.save(document);
    }

    public Resource download(Document document) throws MalformedURLException
    {
        return fileStorageService.download(document);
    }

    @Transactional
    public void delete(Document document) throws IOException
    {
        String username = document.getUser().getUsername();
        String filename = document.getName();

        fileStorageService.delete(document);

        documentRepository.removeByUserEmailAndName(username, filename);
    }

    public List<Document> findAllForAdminUser(UserEntity user)
    {
        List<Document> allUserDocs = documentRepository.findByUserRole(Role.ROLE_USER);
        List<Document> currentAdminDocs = documentRepository.findByUser(user);

        List<Document> availableDocsForCurrentAdmin = new ArrayList<>();

        availableDocsForCurrentAdmin.addAll(allUserDocs);
        availableDocsForCurrentAdmin.addAll(currentAdminDocs);

        return availableDocsForCurrentAdmin;
    }

    public void verifyUser(Principal principal, Document document)
    {
        String username = principal.getName();
        UserEntity user = userEntityRepository.findByEmail(username).orElseThrow();

        if(!username.equals(document.getUser().getUsername()))
        {
            if(user.isUser())
                throw new AccessDeniedException("Access denied!");

            if(user.isAdmin() && document.getUser().isAdmin())
                throw new AccessDeniedException("Access denied!");
        }
    }
}
