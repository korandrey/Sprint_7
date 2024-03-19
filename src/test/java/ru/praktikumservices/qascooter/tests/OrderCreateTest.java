package ru.praktikumservices.qascooter.tests;

import io.qameta.allure.Feature;

import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikumservices.qascooter.order.Order;
import ru.praktikumservices.qascooter.order.OrderClient;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private OrderClient orderClient;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String deliveryDate;
    private final String comment;
    private final int rentTime;
    private final String[] color;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone, String deliveryDate, String comment, int rentTime, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.rentTime = rentTime;
        this.color = color;
    }

    @Before
    public void createTestData() {
        orderClient = new OrderClient();
    }

    @Parameterized.Parameters
    public static Object[][] getComparisonTestData() {
        return new Object[][]{
                {"Kor12", "Kor12", "Barri172", "Sokolnki", "8-800-700", "2024-03-14", "lol", 10, new String[]{"BLACK"}},
                {"Kor12", "Kor12", "Barri172", "Sokolnki", "8-800-700", "2024-03-14", "lol", 10, new String[]{"GREY"}},
                {"Kor12", "Kor12", "Barri172", "Sokolnki", "8-800-700", "2024-03-14", "lol", 10, new String[]{"GREY", "BLACK"}},
                {"Kor12", "Kor12", "Barri172", "Sokolnki", "8-800-700", "2024-03-14", "lol", 10, null},
        };
    }

    @Test
    @Feature("Создание и проверка валидных заказов")
    public void validOrderCreationRequest() {
        ValidatableResponse validOrderResponse = orderClient.create(new Order(firstName, lastName, address, metroStation, phone, deliveryDate, comment, rentTime, color));
        int statusCodeResponse = validOrderResponse.extract().statusCode();
        Assert.assertEquals("Вернулся неподходящий для автотеста код ответа", 201, statusCodeResponse);
        int trackInResponse = validOrderResponse.extract().path("track");
        Assert.assertNotEquals("Значения не должны быть равны: value1=" + trackInResponse + ", value2=" + 0, 0, trackInResponse);
    }
}
