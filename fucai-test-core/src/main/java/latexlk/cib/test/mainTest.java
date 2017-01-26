package latexlk.cib.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import latexlk.cib.test.beans.wellot_regiest;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
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
       // WebDriver driver = test.getDriver();
        //WebElementMap.put("url","http://wellot.wind4us.com:8080/xnlottery/resources/wechat/bind/bind.jsp");
        //WebElementMap.put("name","signContractName:李科");
        test.readJSON();
//        test.jspTest(WebElementMap,driver);

    }

    public wellot_regiest readJSON(){
        wellot_regiest metaDate =null;
        try{
            InputStream in = this.getClass().getResourceAsStream("/jsp/wellot_regiest.json");
            metaDate = JSON.parseObject(IOUtils.toString(in,"utf-8"),wellot_regiest.class);
            System.out.print(metaDate.getUrl());

        }catch(Exception e){
            e.printStackTrace();
        }
        return metaDate;
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
