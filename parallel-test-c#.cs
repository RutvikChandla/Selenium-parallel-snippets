using System.Threading;
using System;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium.Support.UI;
using System.Collections.Generic;

namespace SeleniumTest
{
    class Program1
    {
        static void Main(string[] args)
        {
            Thread device1 = new Thread(obj => runSession("safari", "latest", "OS X", "Sierra", "parallel test c# 1", "parallel-snippet-test", "4.0.0"));
            Thread device2 = new Thread(obj => runSession("chome", "latest", "OS X", "Sierra", "parallel test c# 2", "parallel-snippet-test", "4.0.0"));
            Thread device3 = new Thread(obj => runSession("edge", "latest", "OS X", "Sierra", "parallel test c# 3", "parallel-snippet-test", "4.0.0"));
            Thread device4 = new Thread(obj => runSession("firefox", "latest", "OS X", "Sierra", "parallel test c# 4", "parallel-snippet-test", "4.0.0"));
            Thread device5 = new Thread(obj => runSession("Safari", "latest", "OS X", "Sierra", "parallel test c# 5", "parallel-snippet-test", "4.0.0"));

            device1.Start();
            device2.Start();
            device3.Start();
            device4.Start();
            device5.Start();
            device1.Join();
            device2.Join();
            device3.Join();
            device4.Join();
            device5.Join();
        }

        static DriverOptions getBrowserOption(String browser)
        {
            switch (browser)
            {
                case "chrome":
                    return new OpenQA.Selenium.Chrome.ChromeOptions();
                case "firefox":
                    return new OpenQA.Selenium.Firefox.FirefoxOptions();
                case "safari":
                    return new OpenQA.Selenium.Safari.SafariOptions();
                case "edge":
                    return new OpenQA.Selenium.Edge.EdgeOptions();
                default:
                    return new OpenQA.Selenium.Chrome.ChromeOptions();
            }
        }

        //executetestwithcaps function takes capabilities from 'sampleTestCase' function and executes the test
        static void runSession(String browser, String browser_version, String os, String os_version, String test_name, String build_name, String seleniumVersion)
        {
            DriverOptions capability = getBrowserOption(browser);

            capability.BrowserVersion = browser_version;
            System.Collections.Generic.Dictionary<string, object> browserstackOptions = new Dictionary<string, object>();
            browserstackOptions.Add("os", os);
            browserstackOptions.Add("osVersion", os_version);
            browserstackOptions.Add("buildName", build_name);
            browserstackOptions.Add("sessionName", test_name);
            browserstackOptions.Add("local", "false");
            browserstackOptions.Add("seleniumVersion", seleniumVersion);
            browserstackOptions.Add("userName", "");
            browserstackOptions.Add("accessKey", "");
            capability.AddAdditionalOption("bstack:options", browserstackOptions);

            IWebDriver driver = new RemoteWebDriver(new Uri("https://hub-cloud.browserstack.com/wd/hub/"), capability);
            try
            {
                WebDriverWait wait = new WebDriverWait(driver, TimeSpan.FromSeconds(10));
                driver.Navigate().GoToUrl("https://bstackdemo.com/");
                // getting name of the product
                IWebElement product = driver.FindElement(By.XPath("//*[@id='1']/p"));
                wait.Until(driver => product.Displayed);
                String product_on_page = product.Text;
                // clicking the 'Add to Cart' button
                IWebElement cart_btn = driver.FindElement(By.XPath("//*[@id='1']/div[4]"));
                wait.Until(driver => cart_btn.Displayed);
                cart_btn.Click();
                // waiting for the Cart pane to appear
                wait.Until(driver => driver.FindElement(By.ClassName("float-cart__content")).Displayed);
                // getting name of the product in the cart
                String product_in_cart = driver.FindElement(By.XPath("//*[@id='__next']/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")).Text;
                if (product_on_page == product_in_cart)
                {
                    ((IJavaScriptExecutor)driver).ExecuteScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"passed\", \"reason\": \" Product has been successfully added to the cart!\"}}");
                }
            }
            catch
            {
                ((IJavaScriptExecutor)driver).ExecuteScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \" Some elements failed to load.\"}}");
            }
            driver.Quit();
        }
    }
}

