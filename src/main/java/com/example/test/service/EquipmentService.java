package com.example.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EquipmentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getEquipmentById(Long id) {
        String sql = "SELECT * FROM equipment WHERE id = ?";
        return jdbcTemplate.queryForMap(sql, id);
    }
}
