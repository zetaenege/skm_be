package nl.wtrlmn.skm.controllers;

import nl.wtrlmn.skm.dto.UserInputDTO;
import nl.wtrlmn.skm.dto.UserOutputDTO;
import nl.wtrlmn.skm.models.User;
import nl.wtrlmn.skm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<UserOutputDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserOutputDTO getUserById(@PathVariable Long id) {

        return userService.findByIdDTO(id);
    }
    @GetMapping("/me")
    public ResponseEntity<UserOutputDTO> getMyUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserOutputDTO user = userService.findByEmailDTO(principal.getName());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public UserOutputDTO updateMyUser(@RequestBody UserInputDTO userInputDTO, Principal principal) {
        UserOutputDTO user = userService.findByEmailDTO(principal.getName());
        return userService.updateUserFromDTO(user.getId(), userInputDTO);
    }


    @PostMapping
    public UserOutputDTO createUser(@RequestBody UserInputDTO userInputDTO) {
        return userService.createUserFromDTO(userInputDTO);
    }

    @PutMapping("/{id}")
    public UserOutputDTO updateUser(@PathVariable Long id, @RequestBody UserInputDTO userInputDTO) {
        return userService.updateUserFromDTO(id, userInputDTO);
    }



    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}