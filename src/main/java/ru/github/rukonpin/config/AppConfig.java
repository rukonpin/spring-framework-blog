package ru.github.rukonpin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan(
        basePackages = "ru.github.rukonpin",
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
            @ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class)
        }
)
public class AppConfig { }
