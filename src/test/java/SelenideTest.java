import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;


class ChromeTest {

    String generateDate (int days, int month, int year) {
        return LocalDate.now().plusDays(days).plusMonths(month).plusYears(year).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldRegisterByAccount() {
        String date = generateDate(5, 2,1);
        open("http://localhost:9999/");
        $("[data-test-id=city] input").setValue("Тюмень");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $("[data-test-id=agreement]").click();
        $$("[type=button]").find(exactText("Забронировать")).click();

        String expected = "Успешно!\nВстреча успешно забронирована на " + date;
        String actual = $("[data-test-id=notification]").shouldBe(visible, Duration.ofMillis(15000)).getText();
        assertEquals(expected, actual);
    }
}