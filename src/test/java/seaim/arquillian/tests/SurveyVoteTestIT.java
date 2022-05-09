
package seaim.arquillian.tests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import java.util.List;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Seaim Khan
 */
public class SurveyVoteTestIT {
    @Test
    public void testSurvey() throws Exception{
        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        
        driver.get("http://localhost:8080/MusicStore/");
        try{
            Thread.sleep(2000);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        List<WebElement> radio = driver.findElements(By.id("form:surveySelect"));
        radio.get(0).click();
        driver.findElement(By.id("form:voteBtn")).sendKeys(Keys.RETURN);
        (new WebDriverWait(driver,10)).until((WebDriver d)-> d.findElement(By.id("form:chartPanel")));
        driver.quit();
    }
}
