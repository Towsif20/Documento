package com.towsif.Documento.service;

import com.towsif.Documento.entity.Document;
import com.towsif.Documento.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService
{
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository)
    {
        this.documentRepository = documentRepository;
    }


    public List<Document> findAllDocs(String email)
    {
        return documentRepository.findByUserEmail(email);
    }
}
