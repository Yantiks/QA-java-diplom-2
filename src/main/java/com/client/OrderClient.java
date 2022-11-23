package com.client;

import com.dto.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    @Step("Create a random order without authorization")
    public ValidatableResponse create(Order ingredients) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post("orders")
                .then();
    }

    @Step("Create a random order with authorization")
    public ValidatableResponse create(Order ingredients, String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post("orders")
                .then();
    }

    @Step("Get order's data with authorization")
    public ValidatableResponse get(String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .get("orders")
                .then();
    }

    @Step("Get order's data without authorization")
    public ValidatableResponse get() {
        return given()
                .spec(getDefaultRequestSpec())
                .get("orders")
                .then();
    }
}
