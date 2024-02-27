import com.codeborne.selenide.*;
import io.qameta.allure.Attachment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DromTest {
    private static String LOGIN = ConfigReader.getLogin();
    private static String PASSWORD = ConfigReader.getPassword();
    public static WebDriver driver;


    @BeforeClass
    public static void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--disable-blink-features=AutomationControlled");
        Configuration.timeout = 5000;
        Configuration.baseUrl = "https://www.drom.ru";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        open("/");
        driver = WebDriverRunner.getWebDriver(); // инициализация WebDriver
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
        saveScreenshot();

        String region = "Москва";
        $$("[class=\"b-selectCars__item\"]")
                .filter(text(region))
                .first()
                .find("a")
                .click();
        saveScreenshot();
        $("[data-ga-stats-name=\"topmenu_sales\"]").click();
        saveScreenshot();
        $("[data-ga-stats-name=\"geoCity\"]").click();
        saveScreenshot();
        $("[placeholder=\"Марка\"]").click();
        saveScreenshot();
        $("[placeholder=\"Марка\"]").val("Audi");
        saveScreenshot();
        $(By.xpath("//div[@aria-label=\"Марка\"]//div[@role=\"option\"]")).shouldHave(text("Audi")).click();
        saveScreenshot();
        $("[placeholder=\"Модель\"]").click();
        saveScreenshot();
        $("[placeholder=\"Модель\"]").val("Q7");
        saveScreenshot();
        $(By.xpath("//div[@aria-label=\"Модель\"]//div[@role=\"option\"]")).shouldHave(text("Q7")).click();
        saveScreenshot();
        $(By.xpath("//*[@data-ftid=\"sales__filter\"]//*[@for=\"photo\"]")).click();
        saveScreenshot();
        $("[data-ftid=\"sales__filter_submit-button\"]").click();
        saveScreenshot();
        $("[aria-label=\"Сначала показывать\"]").click();
        saveScreenshot();
        String sorting = "с отличной ценой";
        $$(By.xpath("//div[@aria-label=\"Сначала показывать\"]//div[@role=\"option\"]"))
                .filter(text(sorting))
                .first()
                .click();
        saveScreenshot();
        $$("[data-ftid=\"bulls-list_bull\"]").stream().limit(3).forEach(elCar -> System.out.println(elCar.getText()));
        try {
            Thread.sleep(3000); // Пауза в 3 секунд (3000 миллисекунд)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshot() {
        try {
            return makeScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] makeScreenshot() throws IOException {
        Screenshot screenshot = new AShot()
                .takeScreenshot(driver);
        BufferedImage image = screenshot.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);
        bos.close();

        return bos.toByteArray();
    }


}
