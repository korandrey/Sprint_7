package ru.praktikum_services.qa_scooter.courier;

public class CourierGenerator {

    public static Courier valid() {
        final String login = "kor10";
        final String password = "kor10";
        final String firstName = "kor10";
        return new Courier(login, password, firstName);
    }

}
