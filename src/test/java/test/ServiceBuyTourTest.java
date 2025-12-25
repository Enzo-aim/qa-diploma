
// Пакет, в котором находится тестовый класс
package test;

// Импорт необходимых библиотек и классов
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DataSql;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.PageTour;

// Статический импорт для упрощения синтаксиса
import static com.codeborne.selenide.Selenide.open;

// Тестовый класс для проверки функциональности покупки тура
// Название класса соответствует тестируемой функциональности - ServiceBuyTourTest
public class ServiceBuyTourTest {

    // Аннотация @BeforeAll указывает, что этот метод должен быть выполнен
    // один раз перед всеми тестами в этом классе

    // подключение слушателя allure
    @BeforeAll
    static void setUpAll() {
// Добавление слушателя Allure для интеграции Selenide с Allure Reports
        // Это позволит сохранять скриншоты, HTML-код страниц и другую информацию
        // в отчет Allure при выполнении тестов
        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    // Аннотация @AfterAll указывает, что этот метод должен быть выполнен
    // один раз после всех тестов в этом классе

    // Откулючение слушателя allure
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    // Аннотация @BeforeEach указывает, что этот метод должен быть выполнен
    // перед каждым тестом в этом классе
    @BeforeEach
    void clearDatabasesTables() {
        DataSql.cleanTables();
    }

    // Аннотация @Test указывает, что это тестовый метод, который будет выполняться фреймворком тестирования
    @Test
    // Аннотация @DisplayName задает человекочитаемое название теста, которое отображается в отчетах
    @DisplayName("Оплата тура с валидной картой статусом APPROVED")
    public void testCashValidCardApproved1() {
        // 1. ОТКРЫТИЕ СТРАНИЦЫ
        // Открываем веб-страницу приложения по указанному URL и инициализируем Page Object
        var pageTour = open("http://localhost:8080", PageTour.class);

        // 2. ВЫБОР СПОСОБА ОПЛАТЫ
        // Выбираем способ оплаты "наличными" (или покупку через кнопку "Купить")
        pageTour.buyWithCash();

        // 3. ЗАПОЛНЕНИЕ ФОРМЫ ДАННЫМИ КАРТЫ
        // Заполняем поле номера карты валидным номером карты со статусом APPROVED
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());

        // Заполняем поле месяца действия карты (текущий или валидный месяц)
        pageTour.setMonthField(DataHelper.getMonth());

        // Заполняем поле года действия карты (текущий или будущий год)
        pageTour.setYearField(DataHelper.getYear());

        // Заполняем поле имени владельца карты (валидное имя)
        pageTour.setUserField(DataHelper.getUser());

        // Заполняем поле CVC/CVV кода карты (валидный код)
        pageTour.setCvcField(DataHelper.getCvc());

        // 4. ОТПРАВКА ФОРМЫ
        // Нажимаем кнопку "Продолжить" для отправки формы
        pageTour.clickContinueButton();

        // 5. ПРОВЕРКА ОТОБРАЖЕНИЯ УВЕДОМЛЕНИЯ
        // Проверяем, что отображается сообщение об успешной операции
        pageTour.messageSuccess();

        // 6. ВЕРИФИКАЦИЯ В БАЗЕ ДАННЫХ
        // Проверяем, что в базе данных статус платежа соответствует "APPROVED"
        // Это интеграционная проверка, которая подтверждает, что данные корректно сохранились
        Assertions.assertEquals("APPROVED", DataSql.findPayStatus());
    }

    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом APPROVED")
    public void testCreditValidCardApproved2() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageSuccess();
        Assertions.assertEquals("APPROVED", DataSql.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура с валидной картой с статусом DECLINED")
    public void testCashValidCardDeclined3() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberDeclined());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        Assertions.assertEquals("DECLINED", DataSql.findPayStatus());
    }

    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом DECLINED")
    public void testCreditValidCardDeclined4() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberDeclined());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        Assertions.assertEquals("DECLINED", DataSql.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура по не существующей карте")
    public void testCashInvalidCard1() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberNothing());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по не существующей карте")
    public void testCreditInvalidCard2() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberNothing());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по не полностью заполненной карте")
    public void testCashCardNotFilled3() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberNotFilled());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по не полностью заполненной карте")
    public void testCreditCardNotFilled4() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberNotFilled());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с пустым полем карты")
    public void testCashEmptyCard5() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberEmpty());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с пустым полем карты")
    public void testCreditEmptyCard6() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberEmpty());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с истекшим сроком действия (Месяц)")
    public void testCashInvalidMonthCard7() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonthOneMonth());
        pageTour.setYearField(DataHelper.getCurrentYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с истёкшим сроком действия (Месяц)")
    public void testCreditInvalidMonthCard8() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonthOneMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с невалидным месяцем 00")
    public void testCashInvalidMonthNullCard9() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getInvalidMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с невалидным месяцем 00")
    public void testCreditInvalidMonthCard10() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getInvalidMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненным полем месяц")
    public void testCashMonthEmptyCard11() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getEmptyMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с незаполненным полем месяц")
    public void testCreditMonthEmptyCard12() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getEmptyMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с истёкшим сроком действия (Год)")
    public void testCashInvalidYearCard13() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getPreviousYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.cardExpired();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тура по карте с истёкшим сроком действия (Год)")
    public void testCreditInvalidYearCard14() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getPreviousYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.cardExpired();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с годом + 6 лет от текущего")
    public void testCashYearPlus6Card15() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getCurrentYearPlus6());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с годом + 6 лет от текущего")
    public void testCreditYearPlus6Card16() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getCurrentYearPlus6());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненным полем год")
    public void testCashYearEmptyCard17() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getEmptyYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с незаполненным полем год")
    public void testCreditYearEmptyCard18() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getEmptyYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с вводом цифр в поле Владелец")
    public void testCashNumberUserCard19() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getNumberUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с вводом цифр в поле Владелец")
    public void testCreditNumberUserCard20() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getNumberUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с вводом специальных символов в поле Владелец")
    public void testCashSpecialCharactersUserCard21() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getSpecialCharactersUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с вводом специальных символов в поле Владелец")
    public void testCreditSpecialCharactersUserCard22() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getSpecialCharactersUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с пустым поле Владелец")
    public void testCashEmptyUserUserCard23() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getEmptyUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с пустым поле Владелец")
    public void testCreditEmptyUserUserCard24() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getEmptyUser());
        pageTour.setCvcField(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с водом одной цифры поле (CVC/CVV)")
    public void testCashNumberCVC1Card25() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.get1Cvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с водом одной цифры поле (CVC/CVV)")
    public void testCreditNumberCVC1Card26() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.get1Cvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с водом двух цифр поле (CVC/CVV)")
    public void testCashNumberCVC2Card27() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.get2Cvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с водом двух цифр поле (CVC/CVV)")
    public void testCashNumberCVC2Card28() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.get2Cvc());
        pageTour.clickContinueButton();
        pageTour.incorretFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с пустым полем (CVC/CVV)")
    public void testCashEmptyCvcCard29() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с пустым полем (CVC/CVV)")
    public void testCreditEmptyCvcCard30() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberApproved());
        pageTour.setMonthField(DataHelper.getMonth());
        pageTour.setYearField(DataHelper.getYear());
        pageTour.setUserField(DataHelper.getUser());
        pageTour.setCvcField(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с незаполненными полями")
    public void testCashEmptyCard31() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumberField(DataHelper.getCardNumberEmpty());
        pageTour.setMonthField(DataHelper.getEmptyMonth());
        pageTour.setYearField(DataHelper.getEmptyYear());
        pageTour.setUserField(DataHelper.getEmptyUser());
        pageTour.setCvcField(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с незаполненными полями")
    public void testCashEmptyCard32() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumberField(DataHelper.getCardNumberEmpty());
        pageTour.setMonthField(DataHelper.getEmptyMonth());
        pageTour.setYearField(DataHelper.getEmptyYear());
        pageTour.setUserField(DataHelper.getEmptyUser());
        pageTour.setCvcField(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.requiredFormat();
        Assertions.assertEquals(0, DataSql.getOrderEntityCount());

    }
}