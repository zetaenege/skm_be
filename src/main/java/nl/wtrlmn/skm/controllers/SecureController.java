package nl.wtrlmn.skm.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/secure")
    public ResponseEntity<String> getSecureData() {
        return ResponseEntity.ok("this is a secret for login");
    }

    @GetMapping("/secure/admin")
    public ResponseEntity<String> getSecureAdminData() {
        return ResponseEntity.ok("this is a admin for login");
    }

}
