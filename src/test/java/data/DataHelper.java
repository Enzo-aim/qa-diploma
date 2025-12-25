package data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Утилитный класс
public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));

    private DataHelper() {
    }

    //Метод возвращает заранее заданный номер карты, который должен успешно проходить при оплате.
    public static String getCardNumberApproved() {
        return "4444444444444441";
    }

    //Метод возвращает заранее заданный номер карты, который должен отклоняться при оплате.
    public static String getCardNumberDeclined() {
        return "4444444444444442";
    }

    //Метод генерирует случайный 16-значный номер карты с помощью библиотеки Java Faker.
    public static String getCardNumberNothing() {
        return faker.number().digits(16);
    }

    //Метод генерирует номер карты с неправильной длиной (незаполненный до конца)
    public static String getCardNumberNotFilled() {
        int randomNumberLength = faker.random().nextInt(16);
        return faker.number().digits(randomNumberLength);
    }

    //Метод возвращает пустую строку в качестве номера карты.
    public static String getCardNumberEmpty() {
        return "";
    }

    //Метод возвращает номер месяца, который был 1 месяц назад от текущей даты.
    public static String getMonthOneMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthAgo = currentDate.minusMonths(1);
        return oneMonthAgo.format(DateTimeFormatter.ofPattern("MM"));
    }

    //Метод возвращает текущий год в двузначном формате.
    public static String getCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        return String.format("%02d", currentYear % 100);
    }

    //Метод возвращает год, который на 6 лет больше текущего, в двузначном формате.
    public static String getCurrentYearPlus6() {
        int currentYear = Integer.parseInt(getCurrentYear());
        int yearPlus6 = currentYear + 6;
        return String.format("%02d", yearPlus6 % 100);
    }

    //Метод возвращает предыдущий год в двузначном формате.
    public static String getPreviousYear() {
        int currentYear = Integer.parseInt(getCurrentYear());
        int previousYear = currentYear - 1;
        return String.format("%02d", previousYear % 100);
    }

    //Метод генерирует случайный номер месяца в двузначном формате.
    public static String getMonth() {
        return String.format("%02d", faker.number().numberBetween(1, 13));
    }

    //Метод возвращает невалидный номер месяца "00".
    public static String getInvalidMonth() {
        return "00";
    }

    //Метод возвращает пустую строку в качестве месяца.
    public static String getEmptyMonth() {
        return "";
    }

    //Метод возвращает пустую строку в качестве года.
    public static String getEmptyYear() {
        return "";
    }

    //Метод генерирует случайный год в диапазоне от 2026 до 2030 в двузначном формате.
    public static String getYear() {
        return String.format("%02d", faker.number().numberBetween(26, 31));
    }

    //Метод генерирует случайное полное имя пользователя.
    public static String getUser() {
        return faker.name().fullName();
    }

    //Метод генерирует случайную одну цифру в виде строки.
    public static String getNumberUser() {
        return faker.number().digit();
    }

    //Метод возвращает строку со специальными символами.
    public static String getSpecialCharactersUser() {
        return "(~!@#$%^&*-=_/><{|)";
    }

    //Метод возвращает пустую строку в качестве имени пользователя.
    public static String getEmptyUser() {
        return "";
    }

    //Метод генерирует случайный 3-значный CVC/CVV код.
    public static String getCvc() {
        return faker.number().digits(3);
    }

    //Метод генерирует случайную одну цифру вместо CVC кода.
    public static String get1Cvc() {
        return faker.number().digits(1);
    }

    //Метод генерирует случайные две цифры вместо CVC кода.
    public static String get2Cvc() {
        return faker.number().digits(2);
    }

    //Метод возвращает пустую строку в качестве CVC кода.
    public static String getEmptyCvc() {
        return "";
    }
}


