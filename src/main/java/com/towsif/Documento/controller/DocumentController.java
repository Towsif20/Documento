package com.towsif.Documento.controller;

import com.towsif.Documento.entity.Document;
import com.towsif.Documento.security.SecurityUtil;
import com.towsif.Documento.service.DocumentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public String documents(Model model)
    {
        String currentUserEmail = SecurityUtil.getSessionUser();

        List<Document> documents = documentService.findAllDocs(currentUserEmail);

        model.addAttribute("documents", documents);

        return "documents";
    }
}
