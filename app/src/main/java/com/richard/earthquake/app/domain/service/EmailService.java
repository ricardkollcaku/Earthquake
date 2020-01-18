package com.richard.earthquake.app.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class EmailService<T> {
    @Autowired
    JavaMailSender emailSender;

    public Mono<T> sendEmail(String sendTo, String subject, String sms, T t) {
        SimpleMailMessage message = new SimpleMailMessage();
        Mono.just(new SimpleMailMessage())
                .map(simpleMailMessage -> sendEmail(sendTo, subject, sms, message, simpleMailMessage))
                .subscribeOn(Schedulers.single())
                .subscribe();
        return Mono.just(t);

    }

    private SimpleMailMessage sendEmail(String sendTo, String subject, String sms, SimpleMailMessage message, SimpleMailMessage simpleMailMessage) {
        message.setTo(sendTo);
        message.setSubject(subject);
        message.setText(sms);
        emailSender.send(message);
        return simpleMailMessage;
    }
}
