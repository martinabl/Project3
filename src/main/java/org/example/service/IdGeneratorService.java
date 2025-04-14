package org.example.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class IdGeneratorService {

    private final List<String> ids;

    public IdGeneratorService() throws Exception {
        ClassPathResource resource = new ClassPathResource("dataPersonId.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            ids = reader.lines().collect(Collectors.toList());
        }
    }

    public String getRandomPersonId() {
        Random random = new Random();
        return ids.get(random.nextInt(ids.size()));
    }
}

