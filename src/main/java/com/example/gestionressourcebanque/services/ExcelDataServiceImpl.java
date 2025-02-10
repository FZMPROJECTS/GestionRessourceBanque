package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.enums.EtatMateriel;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import com.example.gestionressourcebanque.repositories.MaterielRepository;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Service
public class ExcelDataServiceImpl implements IExcelDataService{

    @Value("${app.upload.file:}")
    public String EXCEL_FILE_PATH;


    @Autowired
    MaterielRepository materielRepository;

    Workbook workbook;

    public List<Materiel> getExcelDataAsList() throws ParseException {

        List<String> list = new ArrayList<String>();

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // Create the Workbook
        try {
            workbook = WorkbookFactory.create(new File(EXCEL_FILE_PATH));
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }

        // Retrieving the number of sheets in the Workbook
        System.out.println("-------Workbook has '" + workbook.getNumberOfSheets() + "' Sheets-----");

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Getting number of columns in the Sheet
        int noOfColumns = sheet.getRow(0).getLastCellNum();
        System.out.println("-------Sheet has '"+noOfColumns+"' columns------");

        // Using for-each loop to iterate over the rows and columns
        for (Row row : sheet) {
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                list.add(cellValue);
            }
        }

        // filling excel data and creating list as List<Invoice>
        List<Materiel> invList = createList(list, noOfColumns);

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return invList;
    }

    private ArrayList<Materiel> createList(List<String> excelData, int noOfColumns) throws ParseException {

        ArrayList<Materiel> invList = new ArrayList<Materiel>();

        int i = noOfColumns;
        do {
            Materiel inv = new Materiel();

            // Mapping data to the Inventory fields
            //inv.setId(Long.parseLong(excelData.get(i)));
            inv.setImage(excelData.get(i).getBytes());
            //inv.setType(TypeMateriel.valueOf(excelData.get(i + 2)));

            // Convert the Excel string value to TypeMateriel enum
            try {
                inv.setType(TypeMateriel.valueOf(excelData.get(i + 1)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                // Handle the case where the Excel value does not match any enum value
                // You might want to set a default value or handle the error differently
            }

            // Set other fields similarly
            inv.setStatut(StatutMateriel.valueOf(excelData.get(i + 2)));
            inv.setEtat(EtatMateriel.valueOf(excelData.get(i + 3)));
            inv.setNum_serie(Long.parseLong(excelData.get(i + 4)));
            inv.setDescription_mat(excelData.get(i + 5));
            inv.setSite_A_concerne(excelData.get(i + 6));
            inv.setSite_B_concerne(excelData.get(i + 7));
            inv.setEntre_sortie_A(Boolean.parseBoolean(excelData.get(i + 8)));
            inv.setEntre_sortie_B(Boolean.parseBoolean(excelData.get(i + 9)));
            inv.setDate_evenement_A(new SimpleDateFormat("yyyy/MM/dd").parse(excelData.get(i + 10)));
            inv.setDate_evenement_B(new SimpleDateFormat("yyyy/MM/dd").parse(excelData.get(i + 11)));


            invList.add(inv);
            i = i + (noOfColumns);

        } while (i < excelData.size());
        return invList;
    }

    @Override
    public int saveExcelData(List<Materiel> invoices) {
        invoices = materielRepository.saveAll(invoices);
        return invoices.size();
    }

    @Override
    public void processUploadedFile(MultipartFile file) throws IOException, ParseException {

        InputStream inputStream = file.getInputStream();
        if (file.getOriginalFilename().endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream); // For .xls files
        } else if (file.getOriginalFilename().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream); // For .xlsx files
        } else {
            throw new IllegalArgumentException("The specified file is not an Excel file");
        }

        List<Materiel> materials = getExcelDataAsList();
        saveExcelData(materials);

    }
}
