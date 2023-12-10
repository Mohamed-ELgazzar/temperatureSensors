package com.example.tempsensor.exception;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(Long deviceId){
        super("Device with id "+deviceId+ " not found");
    }
    
}
