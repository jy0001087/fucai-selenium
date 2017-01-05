package latexlk.cib.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;


/**
 * Created by Bud on 2017/1/5.
 */
public class backendLoginTest {

    private static ChromeDriverService service;

    public static void main(String[] args) {
        backendLoginTest bt = new backendLoginTest();
        String url = "http://www.cigage.com:9999/xnlottery/resources/backendLogin.jsp";
        for (int i = 0; i < 10; i++) {
            WebDriver dr = bt.getDriver();
            dr.get(url);
            bt.jspTest(dr);
        }
    }

    public WebDriver getDriver() {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("chromedriver.exe"))
                .usingAnyFreePort()
                .build();
        try {
            service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebDriver dr = new RemoteWebDriver(service.getUrl(),
                DesiredCapabilities.chrome());
        return dr;
    }

    public void jspTest(WebDriver dr) {
        WebElement phoneBox = dr.findElement(By.name("phoneNum"));
        phoneBox.sendKeys("18697265816");
        WebElement sendSMS = dr.findElement(By.className("send_vcode"));
        sendSMS.click();
    }
}
