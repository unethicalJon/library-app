package com.example.library.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //source of bean definitions
public class ModelMapperConfig {

    @Bean //initializes a new object to be managed by the Spring IoC container
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

