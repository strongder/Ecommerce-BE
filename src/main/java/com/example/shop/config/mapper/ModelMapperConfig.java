package com.example.shop.config.mapper;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ModelMapperConfig {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Bean
    public ModelMapper modelMapper() {
       ModelMapper modelMapper = new ModelMapper();
       modelMapper.getConfiguration()
       .setSourceNamingConvention(NamingConventions.NONE)
       .setDestinationNamingConvention(NamingConventions.NONE);


        Converter<String, Map<String, Object>> stringToMapConverter = new Converter<String, Map<String, Object>>() {
            @Override
            public Map<String, Object> convert(MappingContext<String, Map<String, Object>> context) {
                String source = context.getSource();
                try {
                    return objectMapper.readValue(source, new TypeReference<Map<String, Object>>() {});
                } catch (Exception e) {
                    throw new RuntimeException("Failed to convert JSON string to Map", e);
                }
            }
        };

        // Converter từ Map<String, Object> sang String (JSON)
        Converter<Map<String, Object>, String> mapToStringConverter = new Converter<Map<String, Object>, String>() {
            @Override
            public String convert(MappingContext<Map<String, Object>, String> context) {
                Map<String, Object> source = context.getSource();
                try {
                    return objectMapper.writeValueAsString(source);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to convert Map to JSON string", e);
                }
            }
        };

        modelMapper.addConverter(stringToMapConverter);
        modelMapper.addConverter(mapToStringConverter);

       return modelMapper;
    }


}
