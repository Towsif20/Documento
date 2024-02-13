package com.towsif.Documento.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/documents")
public class DocumentController
{
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public String documents()
    {
        return "documents";
    }
}
