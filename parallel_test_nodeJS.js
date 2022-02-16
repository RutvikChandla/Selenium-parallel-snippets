const webdriver = require('selenium-webdriver');
const { By } = require('selenium-webdriver');
const assert = require('assert');

async function runTestWithCaps(cap) {
  let driver = new webdriver.Builder().usingServer(`https://hub-cloud.browserstack.com/wd/hub`).withCapabilities(cap).build();
  try{
    await driver.get("https://bstackdemo.com/");
    await driver.wait(webdriver.until.titleMatches(/StackDemo/i), 10000);
    // locating product on webpage
    const productOnScreen = await driver.wait(webdriver.until.elementLocated(By.xpath('//*[@id="1"]/p')), 10000)
    // getting name of the product when the product is visible
    const productText =  await driver.wait(webdriver.until.elementIsVisible(productOnScreen, 10000)).getText();
    // clicking the 'Add to cart' button
    await driver.wait(webdriver.until.elementIsVisible(driver.findElement(By.xpath('//*[@id="1"]/div[4]'), 10000))).click();
    // waiting until the Cart pane has been displayed on the webpage
    await driver.wait(webdriver.until.elementIsVisible(driver.findElement(By.className('float-cart__content'), 10000)));
    // locating product in cart
    const productInCart = await driver.wait(webdriver.until.elementLocated(By.xpath('//*[@id="__next"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]')), 10000);
    // getting name of the product in cart if the product is visible on web page
    const productCartText =  await driver.wait(webdriver.until.elementIsVisible(productInCart, 10000)).getText();
    // checking whether product has been added to cart by comparing product name
    assert(productText === productCartText);
    //marking the test as Passed if product has been added to the cart
    await driver.executeScript(
      'browserstack_executor: {"action": "setSessionStatus", "arguments": {"status":"passed","reason": "Product has been successfully added to the cart!"}}'
    );
  } catch(e) {
    //marking the test as Failed if product has not been added to the cart
    console.log("Error:", e.message)
    await driver.executeScript(
      'browserstack_executor: {"action": "setSessionStatus", "arguments": {"status":"failed","reason": "Some elements failed to load."}}'
    );
  }
  await driver.quit();
}

// Input capabilities
var capabilities = [
    {
        'bstack:options' : {
            "os" : "OS X",
            "osVersion" : "Sierra",
            "buildName" : "parallel-snippet-test",
            "sessionName" : "Selenium-4 Nodejs snippet test",
            "local" : "false",
            "seleniumVersion" : "4.0.0",
            "userName" : "",
            "accessKey" : "",
        },
        "browserName" : "Chrome",
        "browserVersion" : "latest",
    },
    {
        'bstack:options' : {
            "os" : "OS X",
            "osVersion" : "Sierra",
            "buildName" : "parallel-snippet-test",
            "sessionName" : "Selenium-4 Nodejs snippet test",
            "local" : "false",
            "seleniumVersion" : "4.0.0",
            "userName" : "",
            "accessKey" : "",
        },
        "browserName" : "Firefox",
        "browserVersion" : "latest",
    },
    {
        'bstack:options' : {
            "os" : "OS X",
            "osVersion" : "Sierra",
            "buildName" : "parallel-snippet-test",
            "sessionName" : "Selenium-4 Nodejs snippet test",
            "local" : "false",
            "seleniumVersion" : "4.0.0",
            "userName" : "",
            "accessKey" : "",
        },
        "browserName" : "Edge",
        "browserVersion" : "latest",
    }
]

capabilities.map(async (caps) => {
    await runTestWithCaps(caps);
});
