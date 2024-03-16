package ru.praktikumservices.qascooter.tests;

import io.qameta.allure.Feature;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.courier.Courier;
import ru.praktikumservices.qascooter.courier.CourierClient;
import ru.praktikumservices.qascooter.courier.CourierCredentials;
import ru.praktikumservices.qascooter.courier.CourierGenerator;

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;
    private int id;

    @Before
    public void createTestData() {
        courierClient = new CourierClient();
        courier = CourierGenerator.valid();
    }

    @After
    public void cleanup() {
        courierClient.delete(id);

    }

    //Проверяется в тесте courierCanBeCreate
//    курьера можно создать;+
//    чтобы создать курьера, нужно передать в ручку все обязательные поля;+
//    запрос возвращает правильный код ответа;+
//    успешный запрос возвращает ok: true;+
    @Test
    @Feature("Создание и проверка валидного курьера")
    public void courierCanBeCreate() {
        //Создаем курьера через пост запрос
        ValidatableResponse response = courierClient.create(courier);
        //получаем статус код
        int statusCode = response.extract().statusCode();
        //Получаем значение ключа "ok" из боди респонса
        boolean statusInJsonResponse = response.extract().path("ok");
        //Проверяем код ответа
        Assert.assertEquals("Статус кода != 201", statusCode, 201);
        //проверяем полученное значение в боди
        Assert.assertTrue("Значение ключа 'ok' в JSON != true", statusInJsonResponse);
        //Смотрим логин нашего курьера(что курьер создан)
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        //Получаем из ответа id
        id = loginResponse.extract().path("id");
        //Получаем код ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        //Проверяем, что id не равно нулю
        Assert.assertNotEquals("Значения не должны быть равны: value1=" + id + ", value2=" + 0, 0, id);
        //Проверяем код ответа
        Assert.assertEquals("Статус кода != 200", loginStatusCode, 200);
    }

    //Проверяется в тесте duplicateCourierCannotBeCreated
    //    нельзя создать двух одинаковых курьеров;+
    @Test
    @Feature("Создание двух одинаковых курьеров и их проверка")
    public void duplicateCourierCannotBeCreated() {
        //Создаем курьера
        ValidatableResponse courierResponse = courierClient.create(courier);
        //получаем статус код
        int statusCode = courierResponse.extract().statusCode();
        Assert.assertEquals("Статус кода != 201", statusCode, 201);
        //Смотрим логин нашего курьера(что курьер создан)
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        //Получаем из ответа id
        id = loginResponse.extract().path("id");
        //Повторно создаем такого же курьера
        ValidatableResponse duplicateCourierResponse = courierClient.create(courier);
        //Проверяем, что код ответа 409
        int duplicateStatusCode = duplicateCourierResponse.extract().statusCode();
        Assert.assertEquals("Статус кода != 409", duplicateStatusCode, 409);
        //Проверям, что в боди есть "message": "Этот логин уже используется"
        String messageForDuplicate = duplicateCourierResponse.extract().path("message");
        Assert.assertEquals(messageForDuplicate, "Этот логин уже используется. Попробуйте другой.");
    }
}
