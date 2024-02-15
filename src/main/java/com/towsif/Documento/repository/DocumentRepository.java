package com.towsif.Documento.repository;

import com.towsif.Documento.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>
{
    List<Document> findByUserEmail(String email);

    void removeByUserEmailAndName(String currentUserEmail, String filename);
}
