package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Demande;
import com.example.gestionressourcebanque.entities.Materiel;
import com.example.gestionressourcebanque.entities.Mouvement;
import com.example.gestionressourcebanque.entities.User;
import com.example.gestionressourcebanque.enums.EtatMateriel;
import com.example.gestionressourcebanque.enums.StatutMateriel;
import com.example.gestionressourcebanque.enums.TypeMateriel;
import com.example.gestionressourcebanque.repositories.MaterielRepository;
import com.example.gestionressourcebanque.services.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.logging.Logger;

@Controller
@AllArgsConstructor
public class MaterielController {

    @Autowired
    private MaterielService materielService;
    @Autowired
    private MaterielRepository materielRepository;

    @Autowired
    private DemandeService demandeService;
    @Autowired
    private MouvementService mouvementService;
    private IExcelDataService excelDataService;

    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(MaterielController.class.getName());


    @RequestMapping("/CreateMateriel")
    public String createMateriel(ModelMap modelMap) {
        modelMap.addAttribute("materiel", new Materiel());
        modelMap.addAttribute("TypeMaterielOptions", TypeMateriel.values());
        return "/pages/CreateMateriel";
    }

    @RequestMapping("/CreateMateriel2")
    public String createMateriel2(ModelMap modelMap) {
        modelMap.addAttribute("materiel", new Materiel());
        modelMap.addAttribute("TypeMaterielOptions", TypeMateriel.values());
        return "/pages/addM";
    }

    @RequestMapping("/saveMateriel")
    public String saveMateriel(@Valid @ModelAttribute("materiel") Materiel materiel,
                               BindingResult bindingResult,
                               @RequestParam(value = "image", required = false) MultipartFile imageFile,
                               ModelMap modelMap,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur de validation du formulaire.");
            return "redirect:/CreateMateriel";
        }
        if (materiel.getStatut() == null) {
            materiel.setStatut(StatutMateriel.Libre);
        }
        if (materiel.getEtat() == null) {
            materiel.setEtat(EtatMateriel.Fonctionnel);
        }
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                materiel.setImage(imageFile.getBytes());
                materiel.setImageName(imageFile.getOriginalFilename());
                materiel.setImageType(imageFile.getContentType());
            } else {
                try (InputStream defaultImageStream = getClass().getResourceAsStream("/static/images/no_image.jpg")) {
                    if (defaultImageStream != null) {
                        materiel.setImage(defaultImageStream.readAllBytes());
                        materiel.setImageName("default-image.jpg");
                        materiel.setImageType("image/jpeg");
                    } else {
                        throw new IOException("Image par défaut introuvable.");
                    }
                }
            }
            materielService.saveMateriel(materiel);
        } catch (Exception e) {
            String errorMessage = "Une erreur est survenue lors de l'enregistrement du matériel : Le numero de serie ne peut pas etre duplique ";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/CreateMateriel";
        }
        return "redirect:/MaterielsList";
    }


    @PostMapping("/addM")
    public String saveTheMateriel(@RequestParam("file") MultipartFile file,
                                  @RequestParam("type") TypeMateriel type,
                                  ModelMap modelMap) {

        try {
            materielService.saveTheMateriel(file, type);
        } catch (IOException e) {
            e.printStackTrace();
            modelMap.addAttribute("errorMessage", "Error saving materiel: " + e.getMessage());
            return "/pages/Erreur";
        }

        return "redirect:/MaterielsList";
    }

    @RequestMapping("/MaterielsList")
    public String materielsList(@RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "9") int size,
                                ModelMap modelMap, Authentication authentication) {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        Pageable pageable = PageRequest.of(page, size);
        if (roles.contains("ROLE_ADMIN")||roles.contains("ROLE_ADMINISTRATEUR")){
            Page<Materiel> materielPage = materielService.getAllMateriels(pageable);

            modelMap.addAttribute("materiels", materielPage.getContent());
            modelMap.addAttribute("currentPage", page);
            modelMap.addAttribute("totalPages", materielPage.getTotalPages());}
        else{
            Page<Materiel> materielPage = materielService.getMaterielsByStatutLibre(pageable);

            modelMap.addAttribute("materiels", materielPage.getContent());
            modelMap.addAttribute("currentPage", page);
            modelMap.addAttribute("totalPages", materielPage.getTotalPages());
        }

        return "/pages/MaterielsList";
    }
    @RequestMapping("/deleteMateriel")
    public String deleteMateriel(@RequestParam("id") long id, ModelMap modelMap) {
        materielService.deleteMaterielById(id);
        return "redirect:/MaterielsList";
    }

    @RequestMapping("/EditMateriel")
    public String editMateriel(@RequestParam("id") long id, ModelMap modelMap) {
        Materiel materiel = materielService.getMaterielById(id);
        modelMap.addAttribute("materiel", materiel);
        modelMap.addAttribute("TypeMaterielOptions", TypeMateriel.values());
        modelMap.addAttribute("StatutMaterielOptions", StatutMateriel.values());
        modelMap.addAttribute("EtatMaterielOptions", EtatMateriel.values());
        return "pages/MaterielDetails"; // Vérifiez que le chemin vers la vue est correct
    }



    @RequestMapping("/updateMateriel")
    public String updateMateriel(@Valid @ModelAttribute("materiel") Materiel materiel,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                 ModelMap modelMap) {
        modelMap.addAttribute("materiel", materiel);
        modelMap.addAttribute("TypeMaterielOptions", TypeMateriel.values());
        modelMap.addAttribute("StatutMaterielOptions", StatutMateriel.values());
        modelMap.addAttribute("EtatMaterielOptions", EtatMateriel.values());
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("TypeMaterielOptions", TypeMateriel.values());
            return "pages/MaterielDetails";
        }

        Materiel existingMateriel = materielService.getMaterielById(materiel.getId());

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                // Update the image if a new one is provided
                materiel.setImage(imageFile.getBytes());
                materiel.setImageName(imageFile.getOriginalFilename());
                materiel.setImageType(imageFile.getContentType());
            } else {
                // Preserve the existing image data if no new image is uploaded
                materiel.setImage(existingMateriel.getImage());
                materiel.setImageName(existingMateriel.getImageName());
                materiel.setImageType(existingMateriel.getImageType());
            }

            materielService.updateMateriel(materiel);
        } catch (IOException e) {
            e.printStackTrace();
            modelMap.addAttribute("errorMessage", "Error updating materiel: " + e.getMessage());
            return "pages/Erreur";
        }
        if(materiel.getStatut()==StatutMateriel.Libre){
            Demande demande=demandeService.getMateriel(materiel);
            if(demande!=null){
            demandeService.deleteDemandeById(demande.getId());}
            Mouvement mouvement=mouvementService.getMateriel(materiel);
            if(mouvement!=null){
                mouvementService.deleteMouvementById(mouvement.getId());}

        }
        return "redirect:/MaterielsList";
    }
    @RequestMapping("/MaterielDetails")
    public String materielDetails(@RequestParam("id") long id, ModelMap modelMap) {
        modelMap.addAttribute("TypeMaterielOptions", TypeMateriel.values());
        modelMap.addAttribute("StatutMaterielOptions", StatutMateriel.values());
        modelMap.addAttribute("EtatMaterielOptions", EtatMateriel.values());
        Materiel materiel = materielService.getMaterielById(id);
        modelMap.addAttribute("materiel", materiel);
        return "/pages/MaterielDetails";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Materiel materiel = materielService.getMaterielById(id);
        byte[] imageBytes = materiel.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "/pages/MaterielsList";
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<List<String>> data = new ArrayList<>();

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValue(cell));
                }
                data.add(rowData);
            }

            model.addAttribute("data", data);
            excelDataService.processUploadedFile(file);
            model.addAttribute("message", "File uploaded successfully!");

        } catch (IOException e) {
            // Gestion spécifique de l'IOException
            model.addAttribute("message", "Failed to upload file due to an I/O error: " + e.getMessage());
            return "redirect:/MaterielsList";
        } catch (ParseException e) {
            // Gestion spécifique de la ParseException
            String errorMessage = "Failed to parse the file: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/MaterielsList";
        } catch (Exception e) {
            // Gestion globale des autres exceptions
            String errorMessage = "Un erreur a ete survenue lors du telechargement des informations a partir de ce fichier excel est cela est du soit au redendance des materiaux avec le meme numero de serie soit au mal structure de ce fichier  " ;
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/MaterielsList";
        }

        return "/pages/MaterielsList";
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }



    @GetMapping("/searchMaterielByType")
    public ModelAndView searchMaterielByType(@RequestParam("type") String typeString) {
        ModelAndView mav = new ModelAndView("/pages/MaterielsList");
        TypeMateriel type;
        try {
            type = TypeMateriel.valueOf(typeString); // Conversion de la chaîne en enum
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Invalid TypeMateriel value: " + typeString);
            mav.addObject("error", "Type de matériel invalide.");
            return mav;
        }

        List<Materiel> materiels = materielService.getMaterielsByType(type);
        if (materiels.isEmpty()) {
            LOGGER.info("No materiels found for type: " + type);
            mav.addObject("error", "Aucun matériel trouvé pour ce type.");
        }
        mav.addObject("materiels", materiels);
        return mav;}





}