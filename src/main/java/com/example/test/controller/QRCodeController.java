package com.example.test.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class QRCodeController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "adam";

    @GetMapping(value = "/generate-qr-code/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long id) {
        String qrContent = getEquipmentById(id);
        if (qrContent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
        } catch (WriterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(byteArrayOutputStream.toByteArray());
    }

    private String getEquipmentById(Long id) {
        String sql = "SELECT * FROM equipment WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long equipmentId = rs.getLong("id");
                    String name = rs.getString("nom");
                    String category = rs.getString("categorie");
                    String result = "ID: " + equipmentId + ", Name: " + name + ", categorie: " + category;
                    //return "ID: " + equipmentId + ", Name: " + name;
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
