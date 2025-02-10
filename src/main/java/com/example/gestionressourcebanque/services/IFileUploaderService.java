package com.example.gestionressourcebanque.services;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploaderService {

    void uploadFile(MultipartFile file);
}
