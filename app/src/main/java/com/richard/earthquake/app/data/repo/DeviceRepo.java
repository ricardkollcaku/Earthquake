package com.richard.earthquake.app.data.repo;

import com.richard.earthquake.app.data.dto.Device;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceRepo extends ReactiveMongoRepository<Device, String> {
}
