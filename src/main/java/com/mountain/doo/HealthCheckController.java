package com.mountain.doo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthCheckController {
    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck(){
        log.info("server is running");
        return ResponseEntity.ok()
                .body("It's Okay!");
    }
}
