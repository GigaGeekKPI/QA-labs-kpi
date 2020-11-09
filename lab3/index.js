const {Builder, By, Key, util, Condition} = require("selenium-webdriver");

async function browser(browserName) {
    let driver = await new Builder().forBrowser(browserName).build();

    try {
        await driver.get("http://todomvc.com/examples/angularjs/#/");
        
        // Add new Task
        setTimeout(async () => {
            // Add new Task
            setTimeout(async () => {
                await driver.findElement(By.className("new-todo")).sendKeys("New TAsk", Key.RETURN);
            }, 1000);
            
            // Task Double Click
            setTimeout(async () => {
                await driver.executeScript(`document.querySelector('.ng-binding').dispatchEvent(new Event('dblclick'))`);
            }, 1500); 
            
            // Edit Task
            setTimeout(async () => {
                await driver.findElement(By.className("edit")).sendKeys("New TAskahsdgiasg", Key.RETURN);
            }, 2000); 
            
            // Complete task
            setTimeout(async () => {
                await (await driver.findElement(By.className("toggle"))).click();
            }, 2500);

            // Add new Task
            setTimeout(async () => {
                await driver.findElement(By.className("new-todo")).sendKeys("New TAsk", Key.RETURN);
            }, 3000);
            
            // Show active
            setTimeout(async () => {
                await driver.get("http://todomvc.com/examples/angularjs/#/active");
            }, 3500);
            
            // Show completed
            setTimeout(async () => {
                await driver.get("http://todomvc.com/examples/angularjs/#/completed");
            }, 4000);
            
            // Remove completed task
            setTimeout(async () => {
                await driver.executeScript(`
                document.querySelector('.ng-binding').dispatchEvent(new Event('hover'))
                document.querySelector('.destroy').dispatchEvent(new Event('click'))`
                );
            }, 4500);
        }, 3000);
      } finally {
        // await driver.quit();
      }
}

browser("firefox");