package com.example.tempsensor.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")

public class Device {

    @Id
    @Column(name = "device_id")

    private Long deviceId;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TemperatureReading> readings= new ArrayList<>();

    public void addReading(TemperatureReading reading) {
        readings.add(reading);
        reading.setDevice(this);
    }
  

    public Device(Long deviceId) {
        this.deviceId = deviceId;
    }

}
