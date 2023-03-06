package com.polarbookshop.catalogservice.controller;


import com.polarbookshop.catalogservice.config.PolarProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    private final PolarProperties polarProperties;

    public CatalogController(PolarProperties polarProperties) {
        this.polarProperties = polarProperties;
    }

    @GetMapping("/")
    public String getGreeting(){
        return polarProperties.getGreeting();
    }
}
