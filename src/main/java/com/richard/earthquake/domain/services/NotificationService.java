package com.richard.earthquake.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NotificationService {
    @Autowired
    StreamProvider streamProvider;

    @PostConstruct
    void sendNotifications() {

    }
}
