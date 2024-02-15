package com.towsif.Documento.controller;

import com.towsif.Documento.entity.Document;
import com.towsif.Documento.security.SecurityUtil;
import com.towsif.Documento.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequestMapping("/documents")
public class DocumentController
{
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService)
    {
        this.documentService = documentService;
    }

    @ModelAttribute("documents")
    public List<Document> addDocumentsToModel()
    {
        String currentUserEmail = SecurityUtil.getSessionUser();

        return documentService.findAllDocs(currentUserEmail);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public String documents(@ModelAttribute("documents") List<Document> documents)
    {
        return "documents";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file, HttpServletRequest request) throws IOException
    {
        documentService.upload(file, request);

        return "redirect:/documents";
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException
    {
        Resource file = documentService.download(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
