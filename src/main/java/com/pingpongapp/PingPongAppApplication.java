package com.pingpongapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class PingPongAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(PingPongAppApplication.class, args);
    }
}

@RestController
class PingPongController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS counter (id SERIAL PRIMARY KEY, value INT)");
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM counter", Integer.class);
        if (count == 0) {
            jdbcTemplate.update("INSERT INTO counter(value) VALUES (0)");
        }
    }

    @GetMapping("/")
    public String healthCheck() {
        return "PingPong App is up!";
    }

    @GetMapping("/healthz")
    public ResponseEntity<String> healthCheckDb() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return ResponseEntity.ok("OK - DB reachable");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database not reachable");
        }
    }


    @GetMapping("/pingpong")
    public String pingPong() {
        jdbcTemplate.update("UPDATE counter SET value = value + 1");
        Integer value = jdbcTemplate.queryForObject("SELECT value FROM counter LIMIT 1", Integer.class);
        return String.valueOf(value);
    }
}

