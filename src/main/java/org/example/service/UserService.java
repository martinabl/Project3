package org.example.service;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        List<String> validIds = Files.readAllLines(Paths.get("dataPesronId.txt"));

        if (!validIds.contains(user.getPersonId())) {
            throw new IllegalArgumentException("Zadané personId není v seznamu povolených.");
        }

        if (userRepository.existsByPersonId(user.getPersonId())) {
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

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            user.setGender(updatedUser.getGender());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

}


