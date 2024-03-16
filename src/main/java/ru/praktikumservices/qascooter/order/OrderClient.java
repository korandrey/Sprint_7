package ru.praktikumservices.qascooter.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikumservices.qascooter.RestClient;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создать заказ")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получить список заказов по page")
    public ValidatableResponse get(int page) {
        return given()
                .spec(getSpec())
                .queryParam("page", page)
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
