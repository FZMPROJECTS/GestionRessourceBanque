package com.example.gestionressourcebanque.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotPdfService {

    private static final String PDF_FILE_PATH = "static/pdf/Bonjour.pdf"; // Chemin relatif du fichier PDF

    public String getResponse(String message) {
        Map<String, String> questionResponseMap = readPdfResponses(PDF_FILE_PATH);
        String normalizedMessage = normalizeText(message);

        for (Map.Entry<String, String> entry : questionResponseMap.entrySet()) {
            String[] variants = entry.getKey().split("\\|"); // Split variants
            for (String variant : variants) {
                String normalizedVariant = normalizeText(variant);
                if (normalizedMessage.contains(normalizedVariant)) {
                    return entry.getValue();
                }
            }
        }

        return "Je ne comprends pas votre message. Pouvez-vous reformuler?(ps:Je n'accepte pas les messages avec des fautes)";
    }

    private Map<String, String> readPdfResponses(String filePath) {
        Map<String, String> responses = new HashMap<>();

        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            try (InputStream inputStream = resource.getInputStream();
                 PDDocument document = PDDocument.load(inputStream)) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);

                String[] lines = text.split("\n");

                String currentQuestion = null;
                StringBuilder currentResponse = new StringBuilder();

                for (String line : lines) {
                    // Ignore empty lines
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    // Split the line into question and response
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        if (currentQuestion != null) {
                            responses.put(currentQuestion, currentResponse.toString().trim());
                        }
                        currentQuestion = normalizeText(parts[0].trim());
                        currentResponse = new StringBuilder(parts[1].trim());
                    } else {
                        if (currentQuestion != null) {
                            currentResponse.append(" ").append(line.trim());
                        }
                    }
                }
                if (currentQuestion != null) {
                    responses.put(currentQuestion, currentResponse.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responses;
    }
    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        // Convert to lower case
        text = text.toLowerCase();
        // Remove accents
        text = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        // Remove non-alphanumeric characters (excluding spaces)
        text = text.replaceAll("\\bet\\b|\\bde plus\\b", "").replaceAll("[^a-zA-Z0-9 ]", " ");
        // Remove extra spaces
        text = text.replaceAll("\\s+", " ").trim();
        return text;
    }
}
