package latexlk.cib.test;

import com.alibaba.fastjson.JSON;
import latexlk.cib.test.beans.JspOrderBean;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.WebDriverWait;
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
    public static void main(String[] args) throws Exception{
        mainTest test = new mainTest();
        test.orderController();
    }

    public WellotRegistBean readJSON(String location){
        WellotRegistBean metaDate =null;
        try{
            InputStream in = this.getClass().getResourceAsStream(location);
            metaDate = JSON.parseObject(IOUtils.toString(in,"utf-8"),WellotRegistBean.class);
        }catch(Exception e){
            logger.error("json file not found or press error accerd. /n  {} ",e.toString());
            e.printStackTrace();
        }
        return metaDate;
    }

    public String jspTest(WellotRegistBean jsonbean,WebDriver driver) throws Exception{
        for(Iterator<WebContent>  i=jsonbean.getContent().iterator();i.hasNext();){
            WebContent label = i.next();
            String labelFindWay = label.getLabelFindWay();
            logger.trace("==== process element {}",label.getLabelTarget());
            switch(labelFindWay) {
                case "name":
                    WebElement name = driver.findElement(By.name(label.getLabelTarget()));
                    if(beforeSendKey(name,label.getLabelsendKey())){
                    name.sendKeys(label.getLabelsendKey());}
                    this.afterSendKey(label.getLabelsendKey());
                    break;
                case "id":
                    WebElement id = driver.findElement(By.id(label.getLabelTarget()));
                    if(beforeSendKey(id,label.getLabelsendKey())){
                    id.sendKeys(label.getLabelsendKey());}
                    this.afterSendKey(label.getLabelsendKey());
                    break;
                case "xpath":
                    WebElement xpath = driver.findElement(By.xpath(label.getLabelTarget()));
                    if(beforeSendKey(xpath,label.getLabelsendKey())){
                    xpath.sendKeys(label.getLabelsendKey());}
                    this.afterSendKey(label.getLabelsendKey());
                    break;
                //如果是短息验证码输入按钮，则弹出对话框输入收到的短信验证码，保存全局变量SMSauthCode
                case "button":
                    WebElement button = driver.findElement(By.xpath(label.getLabelTarget()));
                    if(beforeSendKey(button,label.getLabelsendKey())){
                        button.click();
                    }
                    this.afterSendKey(label.getLabelsendKey());
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
    public boolean beforeSendKey(WebElement element,String labelsendKey) throws Exception{
        switch (labelsendKey) {
            case "SMSauthCode":
                element.sendKeys(SMSauthCode);
                return false;
            case "doubleclick":
                element.click();
                Thread.sleep(2000);
                element.click();
                return false;
            case "sleep":
                Thread.sleep(10000);
                return true;
            case "getSMSauthCode":
                element.click();
                SMSauthCode = JOptionPane.showInputDialog("收到的短息你验证码为");
                return false;
            default:
                return true;
        }
    }

    /**
     * 控制当前显示页面的url地址，传入order.json中的次序用以判断是否为新一轮的测试。
     * @param driver
     * @param jspBean
     * @return
     * @throws Exception
     */
    public boolean jspRedirect(WebDriver driver,WellotRegistBean jspBean,int jsonOrder) throws Exception{
        logger.info("entry jspRedirect jspBean url is {}",jspBean.getUrl());
        if(driver.getCurrentUrl().equals("data:,") || jsonOrder==0) {
            String url = jspBean.getUrl();
            if (url != null && url != "") {
                driver.get(url);
                logger.info("start testing url :{}", url);
                return true;
            } else {
                logger.error("fail:url is not defined");
                return false;
            }
        }

        while(!driver.getCurrentUrl().equals(jspBean.getUrl())){
            Thread.sleep(1000);
        }
        String url = driver.getCurrentUrl();
        logger.info("current url : {}",url);
        return true;
    }

    public JspOrderBean readJspOrder(){
        JspOrderBean jspOrderBean =null;
        try{
            InputStream in = this.getClass().getResourceAsStream("/jsp/order.json");
            jspOrderBean = JSON.parseObject(IOUtils.toString(in,"utf-8"),JspOrderBean.class);
        }catch(Exception e){
            logger.error("json file not found or press error accerd. /n  {} ",e.toString());
            e.printStackTrace();
        }
        return jspOrderBean;
    }

    public boolean orderController() throws Exception{
        WebDriver driver = this.getDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        JspOrderBean order = this.readJspOrder();
        ArrayList<String> jsonOrder=order.getJspOrder();
        int totalCount=-1;
        while(totalCount <1){
        try {
             totalCount = Integer.parseInt(JOptionPane.showInputDialog("要测试几次"));
        }catch (Exception e) {
            totalCount = Integer.parseInt(JOptionPane.showInputDialog("输入数字啊亲，要测几次"));
        }
        }
        for(int currentCount = 0;currentCount<totalCount;currentCount++){
        for(int index=0;index<jsonOrder.size();index++){
            String filelocation = "/jsp/"+jsonOrder.get(index);
            logger.info("json location is {}",filelocation);
            WellotRegistBean jspBean = this.readJSON(filelocation);
            if(!this.jspRedirect(driver,jspBean,index)){
                return false;
            }else{
                this.jspTest(jspBean,driver);
            }
        }
        }
        return true;
    }

    public void afterSendKey(String labelsendKey) throws Exception{
        switch (labelsendKey){
            case "sleepAfter":
                Thread.sleep(5000);
                break;
            default:
                break;
        }
    }
}
