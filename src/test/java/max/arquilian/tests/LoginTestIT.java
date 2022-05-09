
package max.arquilian.tests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Max
 */
public class LoginTestIT {
    
    @Test
    public void testLogin() throws Exception {
        ChromeDriverManager.getInstance().setup();
       
        WebDriver driver = new ChromeDriver();

        
        driver.get("http://localhost:8080/MusicStore/");
       

        driver.findElement(By.id("navBarForm:loginButton")).click();
        
        (new WebDriverWait(driver,10)).until((WebDriver d)-> d.getTitle().contains("Login"));
        // Check the title of the page
        // Wait for the page to load, timeout after 10 seconds
       
        //Close the browser
        driver.quit();
    }
    
}
