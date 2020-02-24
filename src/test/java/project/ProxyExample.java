package project;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ProxyExample extends BaseClass {

    @BeforeTest
    public void Setup()
    {
        SetupProxy();
    }

    @Test
    public void ProxyTestPositive(){

        GoToHonda();

        String matchedItem = findHarEntry(uniquePhrase);
        Assert.assertNotNull(matchedItem);
    }

    @Test
    public void ProxyTestNegative(){

        GoToHonda();

        String matchedItem = findHarEntry(uniquePhrase + "123123");
        Assert.assertNotNull(matchedItem);
    }
}
