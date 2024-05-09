package com.min204.coseproject.home;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/")
public class HomeController {
    @GetMapping
    public ResponseEntity helloCose() {
        return ResponseEntity.ok().body(
                Map.of("hello", "cose")
        );
    }
}
