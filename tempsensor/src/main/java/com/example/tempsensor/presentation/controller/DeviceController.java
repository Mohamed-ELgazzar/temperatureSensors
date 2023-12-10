package com.example.tempsensor.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.tempsensor.presentation.DTO.DeviceDTO;
import com.example.tempsensor.presentation.DTO.SensorDataDTO;
import com.example.tempsensor.presentation.DTO.TemperatureReadingDto;
import com.example.tempsensor.service.DeviceService;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final SimpMessagingTemplate template;

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService, SimpMessagingTemplate template) {
        this.deviceService = deviceService;
        this.template = template;

    }

    @PostMapping("/data")
    public ResponseEntity<?> processData(@RequestBody SensorDataDTO sensorData) {
        try {
            deviceService.processData(sensorData.getHexCode());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<TemperatureReadingDto>> getHistoricalReadings(@PathVariable Long deviceId) {
        List<TemperatureReadingDto> historicalReadings = deviceService.getHistoricalReadings(deviceId);
        if (historicalReadings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(historicalReadings);
    }

    @MessageMapping("/subscribe")
    public void handleTemperatureUpdates() {
        try {
            template.convertAndSend("/topic/temperature", deviceService.getAllDevices());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
