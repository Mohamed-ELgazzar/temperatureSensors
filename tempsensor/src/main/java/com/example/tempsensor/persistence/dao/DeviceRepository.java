package com.example.tempsensor.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tempsensor.persistence.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
}