package com.client;

import com.dto.Order;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    public ValidatableResponse create(Order ingredients) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post("orders")
                .then();
    }

    public ValidatableResponse create(Order ingredients, String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post("orders")
                .then();
    }

    public ValidatableResponse get(String token) {
        return given()
                .header("Authorization", token)
                .spec(getDefaultRequestSpec())
                .get("orders")
                .then();
    }

    public ValidatableResponse get() {
        return given()
                .spec(getDefaultRequestSpec())
                .get("orders")
                .then();
    }
}
