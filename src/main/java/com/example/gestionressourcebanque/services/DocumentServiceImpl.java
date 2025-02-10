package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Document;
import com.example.gestionressourcebanque.repositories.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private DocumentRepository DocumentRepository ;
    @Override
    public Document saveDocument(Document document) {
        return DocumentRepository.save(document);
    }

    @Override
    public Document updateDocument(Document document) {
        return DocumentRepository.save(document);
    }

    @Override
    public void deleteDocumentById(long id) {
        DocumentRepository.deleteById(id);
    }

    @Override
    public void deleteAllDocuments() {
        DocumentRepository.deleteAll();
    }

    @Override
    public Document getDocumentById(long id) {
        return DocumentRepository.findById(id).get();
    }

    @Override
    public List<Document> getAllDocuments() {
        return DocumentRepository.findAll();
    }


}
