package nl.wtrlmn.skm.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/public")
    public ResponseEntity<String> getPublicData() {
        return ResponseEntity.ok("this public data!");
    }

    @GetMapping("/public/start")
    public ResponseEntity<String> getStartingPointPublicData() {
        return ResponseEntity.ok("this is the starting point of public data!");
    }

    @GetMapping("/public/more")
    public ResponseEntity<String> getMorePublicData() {
        return ResponseEntity.ok("this is more public data!");
    }
}
