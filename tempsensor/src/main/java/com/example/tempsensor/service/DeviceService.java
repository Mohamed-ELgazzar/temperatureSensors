package com.example.tempsensor.service;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.tempsensor.exception.DeviceNotFoundException;
import com.example.tempsensor.persistence.entity.Device;
import com.example.tempsensor.persistence.entity.TemperatureReading;
import com.example.tempsensor.persistence.dao.DeviceRepository;
import com.example.tempsensor.persistence.dao.TemperatureReadingRepository;
import com.example.tempsensor.presentation.DTO.DeviceDTO;
import com.example.tempsensor.presentation.DTO.TemperatureReadingDto;
import com.example.tempsensor.presentation.DTO.TemperatureUpdate;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final TemperatureReadingRepository temperatureReadingRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, SimpMessagingTemplate messagingTemplate,
            TemperatureReadingRepository temperatureReadingRepository) {
        this.deviceRepository = deviceRepository;
        this.messagingTemplate = messagingTemplate;
        this.temperatureReadingRepository = temperatureReadingRepository;

    }

    private void sendTemperatureUpdate(long deviceId, int temperature) {
        messagingTemplate.convertAndSend("/topic/temperature", new TemperatureUpdate(deviceId, temperature));
    }

    public void processData(String hexCode) {

        // hexCode = hexCode.substring(2);
        byte[] bytes = hexStringToByteArray(hexCode);
        for (int i = 0; i < bytes.length; i += 5) {

            long deviceId = ByteBuffer.wrap(bytes, i, 4).getInt() & 0xFFFFFFFFL;
            int temperature = bytes[i + 4] & 0xFF;

            Device device = findOrCreateDevice(deviceId);
            device.addReading(new TemperatureReading(temperature, LocalDateTime.now()));
            deviceRepository.save(device);
        }
    }

    private static byte[] hexStringToByteArray(String code) {
        code = code.substring(2);
        int len = code.length();
        byte[] data = new byte[len / 2];
        System.out.print("the data is ");

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(code.charAt(i), 16) << 4)
                    + Character.digit(code.charAt(i + 1), 16));
            System.out.print(data[i / 2]);
        }
        return data;
    }

    private Device findOrCreateDevice(long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseGet(() -> deviceRepository.save(new Device(deviceId)));
    }

    public List<DeviceDTO> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream()
                .map(device -> {
                    TemperatureReading lastReading = temperatureReadingRepository
                            .findTopByDeviceOrderByTimestampDesc(device);
                    return new DeviceDTO(device.getDeviceId(),
                            lastReading != null ? lastReading.getTemperature() : null);
                })
                .collect(Collectors.toList());
    }

    public List<TemperatureReadingDto> getHistoricalReadings(Long deviceId) {
        Device device = getDevice(deviceId);
        List<TemperatureReading> readings = device.getReadings();

        // Convert TemperatureReading entities to DTOs
        return readings.stream()
                .map(reading -> new TemperatureReadingDto(reading.getTemperature(), reading.getTimestamp()))
                .collect(Collectors.toList());
    }

    public Device getDevice(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }
}
