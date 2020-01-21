package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.dto.ChangePasswordDto;
import com.richard.earthquake.app.data.model.DummyError;
import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.data.repo.UserRepo;
import com.richard.earthquake.app.domain.Util;
import com.richard.earthquake.app.domain.security.TokenProvider;
import com.richard.earthquake.app.presantation.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ErrorUtil<User> errorUtil;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    EmailService<User> emailService;

    public Mono<User> createUser(User user) {
        user = setUserData(user);
        user = setUserNewPassword(user, user.getPassword());
        return userRepo.findById(user.getEmail())
                .flatMap(user1 -> errorUtil.performDummyError(new DummyError(ErrorMessage.USER_USER_EXIST, user1.getEmail(), HttpStatus.CONFLICT)))
                .switchIfEmpty(save(user));
    }


    public Mono<User> save(User user) {
        return userRepo.save(user);
    }

    public Mono<User> initToken(Mono<String> userId, String token) {
        return findUser(userId)
                .flatMap(user1 -> setTokenToUser(user1, token))
                .flatMap(this::save);
    }

    private Mono<User> setTokenToUser(User user, String token) {
        if (user.getTokens().contains(token))
            return Mono.empty();
        user.getTokens().add(token);
        return Mono.just(user);
    }

    public Mono<User> findUser(Mono<String> userId) {
        return userRepo.findById(userId);
    }

    public Flux<User> findAll() {
        return userRepo.findAll();
    }

    public Mono<String> login(String email, String password) {
        return findUser(Mono.just(email))
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> tokenProvider.createToken(user));
    }

    private User setUserData(User user) {
        if (user.getFilters() == null)
            user.setFilters(new ArrayList<>());
        if (user.getTokens() == null)
            user.setTokens(new HashSet<>());
        return user;
    }

    public Mono<String> registerAutoLogin(User usr) {
        return createUser(usr)
                .map(user -> tokenProvider.createToken(user));
    }

    public Mono<User> forgotPassword(String email) {
        return findUser(Mono.just(email))
                .flatMap(this::generateNewPassSendingEmail)
                .flatMap(this::save);
    }

    private Mono<User> generateNewPassSendingEmail(User user) {
        String password = Util.generateRandomPassword();
        user = setUserNewPassword(user, password);
        return emailService.sendEmail(user.getEmail(), ErrorMessage.FORGOT_PASSWORD_SUBJECT, ErrorMessage.FORGOT_PASSWORD_SMS(user.getEmail(), password), user);
    }

    public Mono<User> changePassword(Mono<String> userId, ChangePasswordDto changePasswordDto) {
        return findUser(userId)
                .filter(user -> user.getPassword().matches(changePasswordDto.getNewPassword()))
                .map(user -> setUserNewPassword(user, changePasswordDto.getNewPassword()))
                .flatMap(this::save);
    }

    private User setUserNewPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    public Mono<User> setNotification(Mono<String> userId, Boolean notification) {
        return findUser(userId)
                .map(user -> setNotificationToUser(user, notification))
                .flatMap(this::save);
    }

    private User setNotificationToUser(User user, Boolean notification) {
        user.setIsNotificationEnabled(notification);
        return user;
    }
}
