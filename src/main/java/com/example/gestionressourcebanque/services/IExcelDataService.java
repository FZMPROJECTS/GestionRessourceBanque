package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Materiel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface IExcelDataService {

    int saveExcelData(List<Materiel> materials);
    void processUploadedFile(MultipartFile file) throws IOException, ParseException;
    List<Materiel> getExcelDataAsList() throws IOException, ParseException;
}
