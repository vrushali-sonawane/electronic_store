package com.bikkadit.electronic_store.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Configuration
@Component
public class MyConfiguration {
    @Bean
    public ModelMapper modelMapper(){

        return new ModelMapper();
    }
}
