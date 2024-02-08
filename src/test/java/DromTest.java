import com.codeborne.selenide.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DromTest {
    private static String LOGIN = ConfigReader.getLogin();
    private static String PASSWORD = ConfigReader.getPassword();


    @BeforeClass
    public static void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--disable-blink-features=AutomationControlled");
        Configuration.timeout = 5000;
        Configuration.baseUrl = "https://www.drom.ru";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        open("/");
        loginUser();
    }

    @AfterClass
    public static void tearDown() {
        closeWebDriver();
    }

    public static void loginUser() {
        $("[data-ftid=\"component_header_login\"]").click();
        $("[for=\"radio_sign\"]");
        $("[id=\"sign\"]").val(LOGIN);
        $("[name=\"password\"]").val(PASSWORD);
        $("[id=\"signbutton\"]").click();
    }

    @Test
    public void checkSearchCar() {
        $("[data-ga-stats-name=\"HomeRegionChange\"]").click();

        String region = "Москва";
        $$("[class=\"b-selectCars__item\"]")
                .filter(text(region))
                .first()
                .find("a")
                .click();

        $("[data-ga-stats-name=\"topmenu_sales\"]").click();
        $("[data-ga-stats-name=\"geoCity\"]").click();
        $("[placeholder=\"Марка\"]").click();
        $("[placeholder=\"Марка\"]").val("Audi");
        $(By.xpath("//div[@aria-label=\"Марка\"]//div[@role=\"option\"]")).shouldHave(text("Audi")).click();
        $("[placeholder=\"Модель\"]").click();
        $("[placeholder=\"Модель\"]").val("Q7");
        $(By.xpath("//div[@aria-label=\"Модель\"]//div[@role=\"option\"]")).shouldHave(text("Q7")).click();
        $(By.xpath("//*[@data-ftid=\"sales__filter\"]//*[@for=\"photo\"]")).click();
        $("[data-ftid=\"sales__filter_submit-button\"]").click();
        $("[aria-label=\"Сначала показывать\"]").click();
        String sorting = "с отличной ценой";
        $$(By.xpath("//div[@aria-label=\"Сначала показывать\"]//div[@role=\"option\"]"))
                .filter(text(sorting))
                .first()
                .click();

        $$("[data-ftid=\"bulls-list_bull\"]").stream().limit(3).forEach(elCar -> System.out.println(elCar.getText()));

        try {
            Thread.sleep(5000); // Пауза в 5 секунд (5000 миллисекунд)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
