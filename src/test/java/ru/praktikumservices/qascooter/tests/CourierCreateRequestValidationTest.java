package ru.praktikumservices.qascooter.tests;

import io.qameta.allure.Feature;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikumservices.qascooter.courier.Courier;
import ru.praktikumservices.qascooter.courier.CourierClient;


@RunWith(Parameterized.class)
public class CourierCreateRequestValidationTest {
    private CourierClient courierClient;
    private final String login;
    private final String password;
    private final String message;

    @Before
    public void createTestData() {
        courierClient = new CourierClient();
    }

    public CourierCreateRequestValidationTest(String login, String password, String message) {
        this.login = login;
        this.password = password;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] compareText() {
        return new Object[][]{
                {"test", null, "Недостаточно данных для создания учетной записи"},
                {null, "test", "Недостаточно данных для создания учетной записи"},
        };
    }

    //Проверяется в тесте
    //    если одного из полей нет, запрос возвращает ошибку;+
    //    если создать пользователя с логином, который уже есть, возвращается ошибка.+
    @Test
    @Feature("Создание и проверка курьера без обязательного поля")
    public void invalidCourierCreationRequestIsNotAllowed() {
        //Создаем курьера без обязательного поля
        ValidatableResponse invalidCourierResponse = courierClient.create(new Courier(login, password));
        //Проверяем что код 400 и message
        int invalidCourierStatusCode = invalidCourierResponse.extract().statusCode();
        Assert.assertEquals("Статус кода != 400", invalidCourierStatusCode, 400);
        String messageForInvalid = invalidCourierResponse.extract().path("message");
        Assert.assertEquals(messageForInvalid, message);
    }
}
