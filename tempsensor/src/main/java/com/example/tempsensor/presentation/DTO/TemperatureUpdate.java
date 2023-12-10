package com.example.tempsensor.presentation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class TemperatureUpdate {
    private long deviceId;
    private int temperature;
}