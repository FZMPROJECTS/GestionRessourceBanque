package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Document;
import com.example.gestionressourcebanque.entities.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {
    Document saveDocument (Document document);
    Document updateDocument(Document document);
    void deleteDocumentById(long id);
    void deleteAllDocuments();
    Document getDocumentById(long id);
    List<Document> getAllDocuments();
}
