package latexlk.cib.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.swing.*;
import java.io.File;


/**
 * Created by Bud on 2017/1/5.
 */
public class backendLoginTest {

    private static ChromeDriverService service;

    public static void main(String[] args) {
        backendLoginTest bt = new backendLoginTest();
        String url_web = "http://www.cigage.com:9999/xnlottery/resources/backendLogin.jsp";
        String url_wechat = "http://http://wellot.wind4us.com:8080/xnlottery/resources/wechat/bind/bind.jsp";
//          for (int i = 0; i < 10; i++) {
            WebDriver dr = bt.getDriver();
            dr.get(url_wechat);
//            dr.get(url_web);
//            bt.jspTest(dr);
  //      }
    }

    public  void wechat_jspTest(WebDriver dr){
        WebElement name = dr.findElement(By.name("signContractName"));
        name.sendKeys("李科");
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
        String sttringSMS = JOptionPane.showInputDialog("收到的短信验证码");
        WebElement inputSMS = dr.findElement(By.name("checkCode"));
        inputSMS.sendKeys(sttringSMS);
        WebElement submitButton = dr.findElement(By.xpath("/html/body/div[1]/form/button[2]"));
        submitButton.click();
    }
}
