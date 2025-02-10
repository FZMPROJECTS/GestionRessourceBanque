package com.example.gestionressourcebanque.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileToByteArrayConverter implements Converter<MultipartFile, byte[]> {

    @Override
    public byte[] convert(MultipartFile source) {
        try {
            return source.isEmpty() ? null : source.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
        }
    }
}
