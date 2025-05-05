package com.pingpongapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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


    @GetMapping("/pingpong")
    public String pingPong() {
        jdbcTemplate.update("UPDATE counter SET value = value + 1");
        Integer value = jdbcTemplate.queryForObject("SELECT value FROM counter LIMIT 1", Integer.class);
        return String.valueOf(value);
    }
}

