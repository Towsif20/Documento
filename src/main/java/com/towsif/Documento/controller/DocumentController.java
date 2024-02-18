package com.towsif.Documento.controller;

import com.towsif.Documento.entity.Document;
import com.towsif.Documento.entity.Role;
import com.towsif.Documento.entity.UserEntity;
import com.towsif.Documento.repository.DocumentRepository;
import com.towsif.Documento.repository.UserEntityRepository;
import com.towsif.Documento.service.DocumentService;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/documents")
public class DocumentController
{
    private final DocumentService documentService;

    private final DocumentRepository documentRepository;

    private final UserEntityRepository userEntityRepository;

    public DocumentController(DocumentService documentService, DocumentRepository documentRepository, UserEntityRepository userEntityRepository)
    {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.userEntityRepository = userEntityRepository;
    }

    @ModelAttribute("documents")
    public List<Document> addDocumentsToModel(Principal principal) throws BadRequestException
    {
        if (principal == null)
            throw new BadRequestException("Bad request");

        String currentUserEmail = principal.getName();

        UserEntity user = userEntityRepository.findByEmail(currentUserEmail).orElseThrow();

        if(user.getRole().equals(Role.ROLE_ADMIN))
            return documentService.findAllForAdminUser(user);

        return documentService.findAllDocs(currentUserEmail);
    }

    @GetMapping
    public String documents()
    {
        return "documents";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file,
                             Principal principal) throws IOException
    {
        if (principal == null)
            throw new BadRequestException("Bad request");

        String username = principal.getName();

        documentService.upload(file, username);

        return "redirect:/documents";
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id,
                                                 Principal principal) throws MalformedURLException, BadRequestException
    {
        if (principal == null)
            throw new BadRequestException("Bad request");

        Document document = documentRepository.findById(id).orElseThrow();

        String username = principal.getName();
        UserEntity user = userEntityRepository.findByEmail(username).orElseThrow();

        if(!username.equals(document.getUser().getUsername()))
        {
            if(user.isUser())
                throw new AccessDeniedException("Access denied!");

            if(user.isAdmin() && document.getUser().isAdmin())
                throw new AccessDeniedException("Access denied!");
        }

        Resource file = documentService.download(document);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/{id}/delete")
    public String deleteFile(@PathVariable Long id,
                             Principal principal) throws IOException
    {
        if (principal == null)
            throw new BadRequestException("Bad request");

        Document document = documentRepository.findById(id).orElseThrow();

        String username = principal.getName();
        UserEntity user = userEntityRepository.findByEmail(username).orElseThrow();

        if(!username.equals(document.getUser().getUsername()))
        {
            if(user.isUser())
                throw new AccessDeniedException("Access denied!");

            if(user.isAdmin() && document.getUser().isAdmin())
                throw new AccessDeniedException("Access denied!");
        }

        documentService.delete(document);

        return "redirect:/documents";
    }
}
