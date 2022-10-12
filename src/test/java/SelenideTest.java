import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


class ChromeTest {

    String generateDate(int days, int month, int year) {
        return LocalDate.now().plusDays(days).plusMonths(month).plusYears(year).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldRegisterByAccount() {
        String date = generateDate(5, 2, 1);
        open("http://localhost:9999/");
        $("[data-test-id=city] input").setValue("Тюмень");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $("[data-test-id=agreement]").click();
        $$("[type=button]").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldHave(exactText("Успешно!\nВстреча успешно забронирована на " + date), Duration.ofMillis(15000)).shouldBe(exist);
    }
}