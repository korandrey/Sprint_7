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

public class LoginTest {
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

    //В тесте проверяется:
    //    курьер может авторизоваться;
    //    успешный запрос возвращает id.
    //    для авторизации нужно передать все обязательные поля;
    @Test
    @Feature("Проверка логина валидного курьера")
    public void validCourierCanLogin() {
        //Создаем курьера через пост запрос
        ValidatableResponse response = courierClient.create(courier);
        //Получаем код ответа
        int statusCode = response.extract().statusCode();
        //Проверяем код ответа
        Assert.assertEquals("Вернулся неподходящий для автотеста код ответа", 201, statusCode);
        //Смотрим логин нашего курьера(что курьер создан)
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        //Получаем код ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        //Проверяем код ответа
        Assert.assertEquals("Вернулся неподходящий для автотеста код ответа", 200, loginStatusCode);
        //Получаем из ответа id
        id = loginResponse.extract().path("id");
        //Проверяем, что id не равно нулю
        Assert.assertNotEquals("Значения не должны быть равны: value1=" + id + ", value2=" + 0, 0, id);
    }
}
