package project;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterTest;

import java.util.List;
import java.util.logging.Level;

public class BaseClass {

    WebDriver driver;
    BrowserMobProxy proxy;
    String uniquePhrase = "sd=24-bit&sr=2560x1440";

    @AfterTest
    public void After(){
        driver.quit();
    }

    protected void GoToHonda(){
        driver.get("https://www.honda.com");
    }

    protected String findHarEntry(String searchQuery){

        // get the HAR data
        Har har = proxy.getHar();
        List<HarEntry> entries = har.getLog().getEntries();
        System.out.println(entries.size() + " Requests found");

        for(int i=0; i<entries.size(); i++){

            String entry = entries.get(i).getRequest().getUrl();
            System.out.println(entry);

            if(entry.contains(searchQuery)){
                System.out.println(" --- MATCHED --- ");
                return entry;
            }
        }
        return null;
    }

    protected String findLogEntry(String search){
        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
        System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");

        for(int i=0; i<entries.size(); i++){
            String entry = entries.get(i).toString();
            System.out.println(entry);

            if(entry.contains(search)){
                System.out.println(" --- MATCHED --- ");
                return entry;
            }

        }
        return null;
    }

    protected void SetupLogging() {
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable( LogType.PERFORMANCE, Level.ALL );
        options.setCapability( "goog:loggingPrefs", logPrefs );
        driver = new ChromeDriver(options);
    }

    protected void SetupProxy(){

        proxy = new BrowserMobProxyServer();
        proxy.start(0);

        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // configure it as a Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        options.setCapability("proxy", seleniumProxy);

        // start the browser up
        driver = new ChromeDriver(options);

        // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        // create a new HAR with the label "honda-har"
        proxy.newHar("honda-har");
    }
}
