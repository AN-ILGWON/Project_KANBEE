package com.kanbee.project.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class SchemaUpdater implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            jdbcTemplate.execute("ALTER TABLE reservations ADD COLUMN reference_url VARCHAR(500)");
            System.out.println("Schema updated: Added reference_url column to reservations table.");
        } catch (Exception e) {
            System.out.println("Schema update skipped: " + e.getMessage());
        }
    }
}
