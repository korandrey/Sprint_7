package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.Feature;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.order.OrderClient;

import java.util.List;


public class GetListOfOrdersTest {

    private OrderClient orderClient;

    @Before
    public void createTestData() {
        orderClient = new OrderClient();
    }

    @Test
    @Feature("Получаем список заказов и проверяем ответ")
    public void listOrdersCanBeGet() {
        ValidatableResponse response = orderClient.get(10);
        int statusCodeResponse = response.extract().statusCode();
        List<Object> orders = response.extract().jsonPath().getList("orders");
        Assert.assertEquals("Статус кода != " + 200, statusCodeResponse, 200);
        Assert.assertFalse(orders.isEmpty());
    }
}
