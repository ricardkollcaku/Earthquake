package com.richard.earthquake.app.data.repo;

import com.richard.earthquake.app.data.Device;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceRepo extends ReactiveMongoRepository<Device, String> {
}
