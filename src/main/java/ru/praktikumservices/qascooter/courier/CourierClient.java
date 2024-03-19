package ru.praktikumservices.qascooter.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikumservices.qascooter.RestClient;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {
    private static final String CREATE_COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_COURIER_PATH = "/api/v1/courier/login";
    private static final String DELETE_COURIER_PATH = "/api/v1/courier/{id}";

    //Создание курьера по эндпоинту /api/v1/courier
    @Step("Создать курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then();
    }

    //Логин курьера в системе
    @Step("Получить информацию о курьере")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(LOGIN_COURIER_PATH)
                .then();
    }

    //Удалить курьера
    @Step("Удалить курьера")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getSpec())
                .pathParam("id", id) // передаем id как параметр пути
                .when()
                .delete(DELETE_COURIER_PATH)
                .then();
    }
}
