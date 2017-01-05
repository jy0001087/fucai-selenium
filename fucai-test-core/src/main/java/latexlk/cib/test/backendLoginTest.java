package latexlk.cib.test;

import org.openqa.selenium.WebDriver;
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
        String url = "http://www.baidu.com";
        dr.get(url);
        dr.quit();
    }
}
