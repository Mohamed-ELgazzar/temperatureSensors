package com.example.tempsensor.presentation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceDTO {
    private Long deviceId;
    private Integer lastTemperature;


}