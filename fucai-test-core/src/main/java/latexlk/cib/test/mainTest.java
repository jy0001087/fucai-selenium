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

import javax.swing.*;

/**
 * Created by Bud on 2017/1/25.
 */
public class mainTest {
    static final Logger logger=LoggerFactory.getLogger(mainTest.class);
    private static ChromeDriverService service;
    private static String SMSauthCode;
    public static void main(String[] args){
        mainTest test = new mainTest();
        WebDriver driver = test.getDriver();
        WellotRegistBean json = test.readJSON();
        test.jspTest(json,driver);
    }

    public WellotRegistBean readJSON(){
        WellotRegistBean metaDate =null;
        try{
            InputStream in = this.getClass().getResourceAsStream("/jsp/1_Login.json");
            metaDate = JSON.parseObject(IOUtils.toString(in,"utf-8"),WellotRegistBean.class);
        }catch(Exception e){
            logger.error("json file not found or press error accerd. /n  {} ",e.toString());
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
                    if(setSMSauthCode(name,label.getLabelsendKey())){
                    name.sendKeys(label.getLabelsendKey());}
                    break;
                case "id":
                    WebElement id = driver.findElement(By.id(label.getLabelTarget()));
                    if(setSMSauthCode(id,label.getLabelsendKey())){
                    id.sendKeys(label.getLabelsendKey());}
                    break;
                case "xpath":
                    WebElement xpath = driver.findElement(By.xpath(label.getLabelTarget()));
                    if(setSMSauthCode(xpath,label.getLabelsendKey())){
                    xpath.sendKeys(label.getLabelsendKey());}
                    break;
                //如果是短息验证码输入按钮，则弹出对话框输入收到的短信验证码，保存全局变量SMSauthCode
                case "button":
                    WebElement button = driver.findElement(By.xpath(label.getLabelTarget()));
                    button.click();
                    if(label.getLabelsendKey().equals("SMSauthCode")){
                        SMSauthCode = JOptionPane.showInputDialog("收到的短息你验证码为");
                    }
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
//json中labelsendKey值等于SMSauthCode的非button类即为输入短信验证码的element，判断后取全局变量输入。
    public boolean setSMSauthCode(WebElement element,String labelsendKey){
        if(labelsendKey.equals("SMSauthCode")){
            element.sendKeys(SMSauthCode);
            return false;}
        else {
            return true;
        }
    }
}
