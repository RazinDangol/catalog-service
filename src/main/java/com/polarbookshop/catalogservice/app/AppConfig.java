package com.polarbookshop.catalogservice.app;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.polarbookshop.catalogservice")
@ConfigurationPropertiesScan(basePackages = "com.polarbookshop.catalogservice.config")
public class AppConfig {
}
