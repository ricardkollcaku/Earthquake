package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.dto.Device;
import com.richard.earthquake.app.data.repo.DeviceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DeviceService {
    @Autowired
    DeviceRepo deviceRepo;

    public Mono<Device> save(Device device) {
        return deviceRepo.save(device);
    }

    public Flux<Device> findAll() {
        return deviceRepo.findAll();
    }
}
