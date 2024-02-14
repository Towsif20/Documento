package com.towsif.Documento.controller;

import com.towsif.Documento.service.FileStorageService;
import org.springframework.stereotype.Controller;

@Controller
public class FileController
{
    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService)
    {
        this.fileStorageService = fileStorageService;
    }
}
