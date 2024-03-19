package ru.praktikumservices.qascooter.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikumservices.qascooter.courier.Courier;
import ru.praktikumservices.qascooter.courier.CourierClient;
import ru.praktikumservices.qascooter.courier.CourierCredentials;

@RunWith(Parameterized.class)
public class LoginRequestValidationTest {
    private CourierClient courierClient;
    private final String login;
    private final String password;
    private final String message;
    private final int statusCode;

    @Before
    public void createTestData() {
        courierClient = new CourierClient();
    }

    public LoginRequestValidationTest(String login, String password, String message, int statusCode) {
        this.login = login;
        this.password = password;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getComparisonTestData() {
        return new Object[][]{
                {"test", null, "Недостаточно данных для входа", 400},
                {null, "test", "Недостаточно данных для входа", 400},
                {"несуществующийвсистемелогин", "test", "Учетная запись не найдена", 404},
                {"test", "несуществующийвсистемепароль", "Учетная запись не найдена", 404},
        };
    }

    //Проверяется в тесте
//        система вернёт ошибку, если неправильно указать логин или пароль;
//        если какого-то поля нет, запрос возвращает ошибку;
//        если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    @Issue("BUG-123 (получаешь 504 вместо 400, когда не передаешь Login в post запрос на эндпоинт /api/v1/courier/login)")
    @Feature("Проверка невалидных данных при проверке курьера")
    public void invalidCourierCreationRequestIsNotAllowed() {
        ValidatableResponse invalidCourierResponse = courierClient.login(CourierCredentials.from(new Courier(login, password)));
        int statusCodeResponse = invalidCourierResponse.extract().statusCode();
        Assert.assertEquals("Вернулся неподходящий для автотеста код ответа", statusCode, statusCodeResponse);
        String messageForInvalid = invalidCourierResponse.extract().path("message");
        Assert.assertEquals(message, messageForInvalid);
    }
}
