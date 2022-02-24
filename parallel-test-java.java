package newpackage;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

class DeviceOne implements Runnable {
	public void run() {
        ChromeOptions browserOptions = new ChromeOptions();
        HashMap<String, Object> bstackOptions = new HashMap<String, Object>();
        bstackOptions.put("os", "OS X");
        bstackOptions.put("osVersion", "Sierra");
        bstackOptions.put("buildName", "parallel-snippet-test");
        bstackOptions.put("sessionName", "java test 1");
        bstackOptions.put("seleniumVersion", "4.0.0");
        browserOptions.setCapability("bstack:options", bstackOptions);
		MyClass myClass = new MyClass();
		WebDriver driver;
		try {
			driver = new RemoteWebDriver(new URL(MyClass.URL), browserOptions);
			myClass.executeTest(driver);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class DeviceTwo implements Runnable {
	public void run() {
		FirefoxOptions browserOptions = new FirefoxOptions();
        HashMap<String, Object> bstackOptions = new HashMap<String, Object>();
        bstackOptions.put("os", "windows");
        bstackOptions.put("osVersion", "11");
        bstackOptions.put("buildName", "parallel-snippet-test");
        bstackOptions.put("sessionName", "java test 2");
        bstackOptions.put("seleniumVersion", "4.0.0");
        browserOptions.setCapability("bstack:options", bstackOptions);
		MyClass myClass = new MyClass();
		WebDriver driver;
		try {
			driver = new RemoteWebDriver(new URL(MyClass.URL), browserOptions);
			myClass.executeTest(driver);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class DeviceThree implements Runnable {
	public void run() {
        EdgeOptions browserOptions = new EdgeOptions();
        browserOptions.setPlatformName("MAC");
        browserOptions.setBrowserVersion("98");
        HashMap<String, Object> bstackOptions = new HashMap<String, Object>();
        bstackOptions.put("os", "OS X");
        bstackOptions.put("osVersion", "Sierra");
        bstackOptions.put("buildName", "parallel-snippet-test");
        bstackOptions.put("sessionName", "java test 3");
        bstackOptions.put("seleniumVersion", "4.0.0");
        browserOptions.setCapability("bstack:options", bstackOptions);
        WebDriver driver;
        MyClass deviceOne = new MyClass();
		try {
			driver = new RemoteWebDriver(new URL(MyClass.URL), browserOptions);
			deviceOne.executeTest(driver);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


    public class MyClass {
        public static final String USERNAME = "rutvikchandla_2MEern";
        public static final String AUTOMATE_KEY = "AXHzyg34Qr81Nep231pu";
        public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
        public static void main(String[] args) throws Exception {
            Thread threadOne = new Thread(new DeviceOne());
            threadOne.start();
            Thread threadTwo = new Thread(new DeviceTwo());
            threadTwo.start();
            Thread threadThree = new Thread(new DeviceThree());
            threadThree.start();


        }

        public void executeTest(WebDriver driver) {
            final JavascriptExecutor jse = (JavascriptExecutor) driver;
            try {
                // Searching for 'BrowserStack' on google.com
                driver.get("https://bstackdemo.com/");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.titleIs("StackDemo"));
                // Getting name of the product
                String product_name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'1\']/p"))).getText();
                //checking whether the Add to Cart button is clickable
                WebElement cart_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\'1\']/div[4]")));
                // clicking the 'Add to cart' button
                cart_btn.click();
                // checking if the Cart pane is visible
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("float-cart__content")));
                // getting the product's name added in the cart
                final String product_in_cart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'__next\']/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]"))).getText();
                // checking if the product added to cart is available in the cart
                if (product_name.equals(product_in_cart)) {
                    jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Product has been added to the cart!\"}}");
                }
            } catch (Exception e) {
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Some elements failed to load..\"}}");
            }
            driver.quit();
        }
}
