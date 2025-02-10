package com.example.gestionressourcebanque.configuration;

import com.example.gestionressourcebanque.converters.MultipartFileToByteArrayConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MultipartFileToByteArrayConverter multipartFileToByteArrayConverter;

    public WebConfig(MultipartFileToByteArrayConverter multipartFileToByteArrayConverter) {
        this.multipartFileToByteArrayConverter = multipartFileToByteArrayConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(multipartFileToByteArrayConverter);
        registry.addFormatter(new Formatter<Date>() {

            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public Date parse(String text, Locale locale) throws ParseException {
                return dateFormat.parse(text);
            }

            @Override
            public String print(Date object, Locale locale) {
                return dateFormat.format(object);
            }
        });
    }
}