package com.dto;

public class LoginGenerator {
    public static UserLogin from(User user) {
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(user.getEmail());
        userLogin.setPassword(user.getPassword());
        return userLogin;
    }
}
