package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Materiel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileUploaderServiceImpl implements IFileUploaderService {

    public List<Materiel> invoiceExcelReaderService() {
        return null;
    }
    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;
    Workbook workbook;

    public void uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream); // For .xlsx files
            } else if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream); // For .xls files
            } else {
                throw new IllegalArgumentException("The specified file is not an Excel file");
            }
            // Rest of the code for processing the workbook and extracting data...
        } catch (IOException e) {
            // Handle exception
        }
    }
}