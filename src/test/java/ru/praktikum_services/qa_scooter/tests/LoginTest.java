package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.Feature;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.courier.Courier;
import ru.praktikum_services.qa_scooter.courier.CourierClient;
import ru.praktikum_services.qa_scooter.courier.CourierCredentials;
import ru.praktikum_services.qa_scooter.courier.CourierGenerator;

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
        Assert.assertEquals("Статус кода != 201", statusCode, 201);
        //Смотрим логин нашего курьера(что курьер создан)
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        //Получаем код ответа
        int loginStatusCode = loginResponse.extract().statusCode();
        //Проверяем код ответа
        Assert.assertEquals("Статус кода != 200", loginStatusCode, 200);
        //Получаем из ответа id
        id = loginResponse.extract().path("id");
        //Проверяем, что id не равно нулю
        assert (id != 0) : "Значения не должны быть равны: value1=" + id + ", value2=" + 0;
    }
}
