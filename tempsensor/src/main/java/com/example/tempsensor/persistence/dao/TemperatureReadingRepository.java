package com.example.tempsensor.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tempsensor.persistence.entity.Device;
import com.example.tempsensor.persistence.entity.TemperatureReading;

public interface TemperatureReadingRepository extends JpaRepository<TemperatureReading, Long> {
    TemperatureReading findTopByDeviceOrderByTimestampDesc(Device device);
}
