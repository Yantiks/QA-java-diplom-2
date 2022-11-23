package com.client;

import com.dto.User;
import com.dto.UserLogin;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    @Step("Create a random client")
    //create
    public ValidatableResponse create(User user) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(user)
                .post("auth/register")
                .then().log().all();
    }

    @Step("Login with the created client")
    //login
    public ValidatableResponse login(UserLogin userLogin) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(userLogin)
                .post("auth/login")
                .then().log().all();
    }

    @Step("Change client's data with authorization")
    //change
    public ValidatableResponse change(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .body(user)
                .patch("auth/user")
                .then();
    }

    @Step("Change client's data without authorization")
    public ValidatableResponse change(User user) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(user)
                .patch("auth/user")
                .then();
    }

    @Step("Delete client")
    //delete
    public ValidatableResponse delete(String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .delete("auth/user")
                .then();
    }
}
