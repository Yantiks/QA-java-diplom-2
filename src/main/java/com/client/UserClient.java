package com.client;

import com.dto.User;
import com.dto.UserLogin;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    //create
    public ValidatableResponse create(User user) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(user)
                .post("auth/register")
                .then().log().all();
    }

    //login
    public ValidatableResponse login(UserLogin userLogin) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(userLogin)
                .post("auth/login")
                .then().log().all();
    }

    //change
    public ValidatableResponse change(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .body(user)
                .patch("auth/user")
                .then();
    }

    public ValidatableResponse change(User user) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(user)
                .patch("auth/user")
                .then();
    }

    //delete
    public ValidatableResponse delete(String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .delete("auth/user")
                .then();
    }
}
