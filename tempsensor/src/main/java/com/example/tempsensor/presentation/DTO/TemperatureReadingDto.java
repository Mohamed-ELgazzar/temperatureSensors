package com.example.tempsensor.presentation.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemperatureReadingDto {
    private int temperature;
    private LocalDateTime timestamp;

}
