package org.example.service;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final IdGeneratorService idGeneratorService;

    public UserService(UserRepository userRepository, IdGeneratorService idGeneratorService) {
        this.userRepository = userRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) throws IOException {

        String personId = user.getPersonId();
        if (personId == null || personId.isEmpty()) {
            personId = idGeneratorService.getRandomPersonId();
            user.setPersonId(personId);
        } else {
            ClassPathResource resource = new ClassPathResource("dataPersonId.txt");
            List<String> validIds;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                validIds = reader.lines().collect(Collectors.toList());
            }
            if (!validIds.contains(personId)) {
                throw new IllegalArgumentException("Zadané personId není v seznamu povolených.");
            }
        }


        if (userRepository.existsByPersonId(personId)) {
            throw new IllegalArgumentException("Toto personId je již použito.");
        }

        user.setUuid(UUID.randomUUID().toString());

        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User updatedUser) {
        return userRepository.findById(updatedUser.getId()).map(user -> {
            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            user.setGender(updatedUser.getGender());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}


