package latexlk.cib.test;

import com.alibaba.fastjson.JSON;
import latexlk.cib.test.beans.WebContent;
import latexlk.cib.test.beans.WellotRegistBean;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by Bud on 2017/1/25.
 */
public class mainTest {
    static final Logger logger=LoggerFactory.getLogger(mainTest.class);
    private static ChromeDriverService service;
    public static void main(String[] args){
        mainTest test = new mainTest();
        WebDriver driver = test.getDriver();
        WellotRegistBean json = test.readJSON();
        test.jspTest(json,driver);
    }

    public WellotRegistBean readJSON(){
        WellotRegistBean metaDate =null;
        try{
            InputStream in = this.getClass().getResourceAsStream("/jsp/wellot_regiest.json");
            metaDate = JSON.parseObject(IOUtils.toString(in,"utf-8"),WellotRegistBean.class);
            WebContent content = metaDate.getContent().get(0);
            System.out.print(content.getLabelTarget());
        }catch(Exception e){
            logger.error("json file not found or press error accerd.");
            e.printStackTrace();
        }
        return metaDate;
    }

    public String jspTest(WellotRegistBean jsonbean,WebDriver driver){
        String url = jsonbean.getUrl();
        if (url!=null&&url!=""){
        driver.get(url);
        logger.info("start testing url :{}",url);
        }
        else{
            logger.error("fail:url is not defined");
            return "fail:url is not defined";
        }
        for(Iterator<WebContent>  i=jsonbean.getContent().iterator();i.hasNext();){
            WebContent label = i.next();
            String labelFindWay = label.getLabelFindWay();
            switch(labelFindWay) {
                case "name":
                    WebElement name = driver.findElement(By.name(label.getLabelTarget()));
                    name.sendKeys(label.getLabelsendKey());
                    break;
                case "id":
                    WebElement id = driver.findElement(By.id(label.getLabelTarget()));
                    id.sendKeys(label.getLabelsendKey());
                    break;
                case "xpath":
                    WebElement xpath = driver.findElement(By.xpath(label.getLabelTarget()));
                    xpath.sendKeys(label.getLabelsendKey());
                    break;
                case "button":
                    WebElement button = driver.findElement(By.xpath(label.getLabelTarget()));
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
