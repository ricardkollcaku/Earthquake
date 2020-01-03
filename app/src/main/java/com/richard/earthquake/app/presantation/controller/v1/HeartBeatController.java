package com.richard.earthquake.app.presantation.controller.v1;

import com.richard.earthquake.app.data.dto.Device;
import com.richard.earthquake.app.domain.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/heartBeat")
public class HeartBeatController {
    @Autowired
    DeviceService deviceService;

    @PostMapping("/{device}")
    public Mono<ResponseEntity<String>> heartBeat(@PathVariable String device) {
        return deviceService.save(new Device(device))
                .map(Device::getDeviceId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/allDevices")
    public Flux<String> getDeviceIds() {
        return deviceService.findAll()
                .map(Device::getDeviceId);
    }
}
