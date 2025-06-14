package nl.wtrlmn.skm.controllers;

import nl.wtrlmn.skm.dto.UserInputDTO;
import nl.wtrlmn.skm.dto.UserOutputDTO;
import nl.wtrlmn.skm.models.User;
import nl.wtrlmn.skm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/me")
    public UserOutputDTO updateMyUser(@RequestBody UserInputDTO userInputDTO, Principal principal) {
        String email = principal.getName();
        User updatedUser = userService.updateUserFromDTO(email, userInputDTO);
        return userService.convertToUserOutputDTO(updatedUser);
    }


    @PostMapping
    public User createUser(@RequestBody UserInputDTO userInputDTO) {
        return userService.createUserFromDTO(userInputDTO);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserInputDTO userInputDTO) {
        return userService.updateUserFromDTO(id, userInputDTO);
    }



    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}