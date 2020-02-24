package project;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoggingExample extends BaseClass{

    @BeforeTest
    public void Setup()
    {
        SetupLogging();
    }

    @Test
    public void LoggingTestPositive(){

        GoToHonda();

        String matchedItem = findLogEntry(uniquePhrase);
        Assert.assertNotNull(matchedItem);
    }

    @Test
    public void LoggingTestNegative(){

        GoToHonda();

        String matchedItem = findLogEntry(uniquePhrase + "123123");
        Assert.assertNotNull(matchedItem);
    }
}
