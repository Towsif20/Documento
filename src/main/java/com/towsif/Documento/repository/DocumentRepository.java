package com.towsif.Documento.repository;

import com.towsif.Documento.entity.Document;
import com.towsif.Documento.entity.Role;
import com.towsif.Documento.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>
{
    List<Document> findByUserEmail(String email);

    void removeByUserEmailAndName(String currentUserEmail, String filename);

    List<Document> findByUserRole(Role role);

    List<Document> findByUser(UserEntity user);
}
