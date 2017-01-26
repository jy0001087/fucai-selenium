package latexlk.cib.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Bud on 2017/1/25.
 */
public class mainTest {
    private static ChromeDriverService service;
    public static void main(String[] args){
        mainTest test = new mainTest();
        LinkedHashMap<String,String> WebElementMap = new LinkedHashMap();
        WebDriver driver = test.getDriver();
        WebElementMap.put("url","http://wellot.wind4us.com:8080/xnlottery/resources/wechat/bind/bind.jsp");
        WebElementMap.put("name","signContractName:李科");
        test.jspTest(WebElementMap,driver);

    }

    public String jspTest(LinkedHashMap<String,String> WebElementMap,WebDriver driver){
        String url = WebElementMap.get("url");
        if (url!=null&&url!=""){
        driver.get(url);}
        else{
            return "fail:url is not defined";
        }
        for(Iterator<String> i =WebElementMap.keySet().iterator();i.hasNext();){
            String ElementType = i.next();
            String ElementValue = WebElementMap.get(ElementType);
            String[] Elements;
            if (ElementValue.contains(":")){
                Elements = ElementValue.split(":");
            }else{
                return "fail:value not current";
            }
            switch(ElementType) {
                case "name":
                    WebElement name = driver.findElement(By.name(Elements[0]));
                    name.sendKeys(Elements[1]);
                    break;
                case "id":
                    WebElement id = driver.findElement(By.id(Elements[0]));
                    id.sendKeys(Elements[1]);
                    break;
                case "xpath":
                    WebElement xpath = driver.findElement(By.xpath(Elements[0]));
                    xpath.sendKeys(Elements[1]);
                    break;
                case "button":
                    WebElement button = driver.findElement(By.xpath(Elements[0]));
                    button.click();
                    break;
            }
        }
    return "success";
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
}
