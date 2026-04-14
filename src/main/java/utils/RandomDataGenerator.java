package utils;


import com.github.javafaker.Faker;

public class RandomDataGenerator {
    private static Faker faker = new Faker();

    public static String getRandomFirstName() {
        return faker.name().firstName();
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomPassword() {
        return faker.internet().password(8, 12, true, true);
    }
}
