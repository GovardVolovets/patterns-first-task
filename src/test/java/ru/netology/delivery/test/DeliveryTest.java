package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        Configuration.holdBrowserOpen = true;
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[placeholder='Город']").setValue(validUser.getCity());
        $(".menu-item").click();

        $("[data-test-id=date]").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(firstMeetingDate);
        $("[name='name']").setValue(validUser.getName());
        $("[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);

        $("[data-test-id=date]").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(secondMeetingDate);
        $$("button").find(exactText("Запланировать")).click();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(15));
        $$("button").find(exactText("Перепланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);;
    }

}



