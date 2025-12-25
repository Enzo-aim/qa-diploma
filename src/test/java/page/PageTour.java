package page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

//описание всех элементов на странице
public class PageTour {
    // Кнопка "Купить" - находится по тексту на кнопке
    private SelenideElement buyButton = $$(".button__text").find(exactText("Купить"));
    // Кнопка "Купить в кредит" - находится по тексту на кнопке
    private SelenideElement buyCreditButton = $$(".button__text").find(exactText("Купить в кредит"));
    // Поле для ввода номера карты - ищется по заголовку "Номер карты", затем находится поле ввода внутри
    private SelenideElement cardNumberField = $$(".input__inner").findBy(exactText("Номер карты")).$(".input__control");
    // Поле для ввода месяца срока действия карты - ищется по заголовку "Месяц"
    private SelenideElement monthField = $$(".input__inner").findBy(exactText("Месяц")).$(".input__control");
    // Поле для ввода года срока действия карты - ищется по заголовку "Год"
    private SelenideElement yearField = $$(".input__inner").findBy(exactText("Год")).$(".input__control");
    // Поле для ввода имени владельца карты - ищется по заголовку "Владелец"
    private SelenideElement userField = $$(".input__inner").findBy(exactText("Владелец")).$(".input__control");
    // Поле для ввода CVC/CVV кода карты - ищется по заголовку "CVC/CVV"
    private SelenideElement cvcField = $$(".input__inner").findBy(exactText("CVC/CVV")).$(".input__control");
    // Заголовок раздела оплаты кредитной картой - проверка отображения формы кредита
    private SelenideElement payCreditCard = $$(".heading").find(exactText("Кредит по данным карты"));
    // Заголовок раздела обычной оплаты картой - проверка отображения формы оплаты
    private SelenideElement payCard = $$(".heading").find(exactText("Оплата по карте"));
    // Сообщение об успешной операции - уведомление с заголовком "Успешно"
    private SelenideElement messageSuccess = $$(".notification__title").find(exactText("Успешно"));
    // Сообщение об ошибке от банка - уведомление с текстом отказа банка
    private SelenideElement messageError = $$(".notification__content").find(exactText("Ошибка! Банк отказал в проведении операции."));
    // Кнопка "Продолжить" для подтверждения введенных данных
    private SelenideElement continueButton = $$(".button__text").find(exactText("Продолжить"));
    // Сообщение об ошибке: истек срок действия карты (валидация поля)
    private SelenideElement cardExpired = $$(".input__sub").find(exactText("Истёк срок действия карты"));
    // Сообщение об ошибке: неверно указан срок действия карты (валидация поля)
    private SelenideElement invalidCardExpirationDate = $$(".input__sub").find(exactText("Неверно указан срок действия карты"));
    // Сообщение об ошибке: неверный формат введенных данных (валидация поля)
    private SelenideElement incorretFormat = $$(".input__sub").find(exactText("Неверный формат"));
    // Сообщение об ошибке: обязательное поле не заполнено (валидация поля)
    private SelenideElement requiredFormat = $$(".input__sub").find(exactText("Поле обязательно для заполнения"));

    // Метод для покупки с обычной оплатой
    public void buyWithCash() {
        buyButton.click(); // Нажимаем кнопку "Купить"
        payCard.shouldBe(visible); // Проверяем, что появилась форма оплаты картой
    }

    // Метод для покупки в кредит
    public void buyInCredit() {
        buyCreditButton.click();    // Нажимаем кнопку "Купить в кредит"
        payCreditCard.shouldBe(visible);  // Проверяем, что появилась форма кредита по карте
    }

    // Метод для заполнения номера карты
    public void setCardNumberField(String number) {
        cardNumberField.setValue(number);
    }

    // Метод для заполнения месяца срока действия карты
    public void setMonthField(String month) {
        monthField.setValue(month);
    }

    // Метод для заполнения года срока действия карты
    public void setYearField(String year) {
        yearField.setValue(year);
    }

    // Метод для заполнения имени владельца карты
    public void setUserField(String user) {
        userField.setValue(user);
    }

    // Метод для заполнения CVC/CVV кода
    public void setCvcField(String cvc) {
        cvcField.setValue(cvc);
    }


    // Метод для нажатия кнопки "Продолжить"
    public void clickContinueButton() {
        continueButton.click();// Нажимаем кнопку для отправки формы
    }

    // Метод проверки отображения сообщения об успешной операции (с таймаутом 15 секунд)
    public void messageSuccess() {
        messageSuccess.shouldBe(visible, Duration.ofSeconds(15));
    }

    // Метод проверки отображения сообщения об ошибке от банка (с таймаутом 15 секунд)
    public void messageError() {
        messageError.shouldBe(visible, Duration.ofSeconds(15));
    }

    // Метод проверки отображения сообщения "Истёк срок действия карты" (с таймаутом 15 секунд)
    public void cardExpired() {
        cardExpired.shouldBe(visible, Duration.ofSeconds(15));
    }

    // Метод проверки отображения сообщения "Неверно указан срок действия карты" (с таймаутом 15 секунд)
    public void invalidCardExpirationDate() {
        invalidCardExpirationDate.shouldBe(visible, Duration.ofSeconds(15));
    }

    // Метод проверки отображения сообщения "Неверный формат" (с таймаутом 15 секунд)
    public void incorretFormat() {
        incorretFormat.shouldBe(visible, Duration.ofSeconds(15));
    }

    // Метод проверки отображения сообщения "Поле обязательно для заполнения" (с таймаутом 15 секунд)
    public void requiredFormat() {
        requiredFormat.shouldBe(visible, Duration.ofSeconds(15));
    }
}
