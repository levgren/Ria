import org.junit.Assert;
        import org.junit.Before;
        import org.openqa.selenium.*;
        import org.junit.Test;
        import org.openqa.selenium.chrome.ChromeDriver;
        import org.openqa.selenium.edge.EdgeDriver;
        import org.openqa.selenium.firefox.FirefoxDriver;
        import org.openqa.selenium.interactions.Actions;
        import org.openqa.selenium.support.ui.ExpectedCondition;
        import org.openqa.selenium.support.ui.Wait;
        import org.openqa.selenium.support.ui.WebDriverWait;
        import java.util.concurrent.TimeUnit;
        import org.openqa.selenium.support.ui.Select;
        import utils.ConfProp;

public class DomRiaTest {
    private static WebDriver driver;
    private int a = 10000;
    private int b = 3000;     // завожу переменные задержки (если поменяется инвайромент, можно будет заменить значения здесь)
    private int c = 5000;
    private int d = 1000;
    public void Setup() throws InterruptedException {

        String chrome = ConfProp.getTestProperty("chromedriver");
        System.setProperty("webdriver.chrome.driver", chrome);
        driver = new ChromeDriver();
        //System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver-v0.23.0-win64\\geckodriver.exe");      //при необходимости раскоментить для прогона тестов в Firefox
        //WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  //доп. настройки, при надобности раскоментить

        String url = "https://dom.ria.com/ru/search/";
        driver.get(url);                   //открываем страницу
        System.out.println("Page opens!");
        Thread.sleep(b);
    }
    public void setParameters() throws InterruptedException {     // задаем необходимые параметры вбоковом фильтре и проверяем соответствует ли им поисковая выдача
        driver.findElement(By.xpath("//*[@class=\"item-pseudoselect selected catTypeName\"]")).click(); // поле Недвижимость
        System.out.println("Field 'Property types' found and clicked");
        driver.findElement(By.xpath("//*[@id=\"catType_4_5_1\"]")).click();     //тип Продажа домов
        System.out.println("The parameter is set - 'House sale'");
        driver.findElement(By.id("autocompleteSearch")).click();        // Поле Города
        System.out.println("field 'City' found and selected");
        Thread.sleep(b);     //повременим чтобы все загрузилось
        driver.findElement(By.cssSelector("[title='Винница']")).click();    //Винница
        System.out.println("City-Hero 'Vinnitsia' selected");
        Thread.sleep(b);
        JavascriptExecutor jekaA = (JavascriptExecutor) driver;   //скролл вниз
        jekaA.executeScript("window.scrollBy(0,250)", "");
        driver.findElement(By.id("pseudomodalRegion")).click();        //Районы города
        System.out.println("Menu 'Areas of Vinnitsia' selected and open");
        Thread.sleep(a);
        driver.findElement(By.xpath("//div[@id='leftFilterDistricts']/div[1]/div[5]/label[.='Вишенка']")).click();     //Вишенка
        driver.findElement(By.xpath("//div[@id='leftFilterDistricts']/div[2]/div[4]/label[.='Зарванцы']")).click();     //Зарванцы
        driver.findElement(By.xpath("//div[@id='leftFilterDistricts']/div[3]/div[2]/label[.='Березина']")).click();    //Шкуринцы
        System.out.println("Successfully established areas of the city");
        JavascriptExecutor jekaB = (JavascriptExecutor) driver;   //скролл вниз
        jekaB.executeScript("window.scrollBy(0,250)", "");
        Thread.sleep(a); // увы, но нужно ждать, мой комп и интернет очень медленный, сорри
        driver.findElement(By.xpath("/html//div[@id='pseudomodalRegion']//label[.='Выбрать']")).click();  //жмем кнопочку "Выбрать"
        System.out.println("Press button 'Select'");
        Thread.sleep(b);
        driver.findElement(By.xpath("//*[@id=\"roomsCountBtns\"]/span/label[4]")).click();    //Количество комнат
        System.out.println("Number of rooms set");
        driver.findElement(By.xpath("/html//div[@id='additional_characteristics']/div[3]/p[1]/span[1]//input[@name='characteristic[215][from]']")).sendKeys("100");   // площадь дома, пишем от 100м2
        System.out.println("The minimum area of the house is established");
        driver.findElement(By.xpath("/html//div[@id='additional_characteristics']/div[4]/p[1]/span//input[@name='characteristic[219][to]']")).sendKeys("6");    // площадь участка до 6 соток
        System.out.println("Established a limit on the area of the homestead");
        driver.findElement(By.xpath("/html//div[@id='additional_characteristics']/div[9]/p[1]/span//input[@name='characteristic[234][to]']")).sendKeys("100000");    // цена до 100000
        System.out.println("Set the maximum price of the object");
        System.out.println("All parameters required are installed successfully! Hooray!");  //все параметры в боковом фильтре
        Thread.sleep(a);
    }
    public void Check() throws InterruptedException {   //проверка на соответствие заданным параметрам
        WebElement Type1 = driver.findElement(By.cssSelector("#leftFilter .css-pseudoselect > [for='selectType']:nth-of-type(1)"));
        String field = Type1.getText(); //Поиск текста на странице
        Assert.assertEquals("Продажа домов", field); //проверка названия поля Типа недвижимости
        System.out.println("Checking the compliance of the field 'Type of real estate' with the specified parameters ............. Success!");

        driver.findElement(By.cssSelector(".twitter-typeahead [type='text']:nth-of-type(2)")).click();
        WebElement name = driver.findElement(By.cssSelector("[title='Винница']"));
        String field1 = name.getText(); //Поиск текста на странице
        Assert.assertEquals("Винница", field1); //проверка названия поля Города
        System.out.println("Checking the compliance of the 'City' field with the specified parameters ............. Success!");

        WebElement Type3 = driver.findElement(By.xpath("//div[@id='pseudomodalRegion']//span[.='Районы города Винница']"));
        String field2 = Type3.getText(); //Поиск текста на странице
        Assert.assertEquals("Районы города Винница", field2); //проверка названия поля Районы
        System.out.println("Checking the compliance of the field 'City districts of Vinnytsia' with the given parameters ............. Success!");

        WebElement Type4 = driver.findElement(By.xpath("//span[@id='roomsCountBtns']/span/label[4]"));
        String butt = Type4.getText(); //Поиск текста
        Assert.assertEquals("4+", butt); //проверка названия кнопки 4+
        System.out.println("Checking the compliance of the 'number of rooms' with the given parameters ............. Success!");

        WebElement Type5 = driver.findElement(By.cssSelector("#additional_characteristics div:nth-of-type(3) .grid:nth-child(2) .small:nth-of-type(1) [type]"));
        String field3 = Type5.getAttribute("value"); //находим значение 100
        Assert.assertEquals("100", field3); //проверка поля площади дома
        System.out.println("Checking the compliance of the field 'Area of the house, Total from - 100' to the specified parameters ............. Success!!!");

        WebElement Type6 = driver.findElement(By.cssSelector("#additional_characteristics div:nth-of-type(4) .small:nth-of-type(2) [type]"));
        String field4 = Type6.getAttribute("value"); //находим значение 6
        Assert.assertEquals("6", field4); //проверка поля площади участка
        System.out.println("Checking the compliance of the field 'Estate area up to 6 acres' to the specified parameters ............. Sucess!");

        WebElement Type7 = driver.findElement(By.cssSelector("#additional_characteristics div:nth-of-type(9) .small:nth-of-type(2) [type]"));
        String field5 = Type7.getAttribute("value"); //находим значение 100000
        Assert.assertEquals("100000", field5); //проверка поля Цены
        System.out.println("Checking the compliance of the field 'Price to 100000' to the specified parameters ............. Success!");

    }
    @Test           //Проверяем или заданные параметры поиска сохраняются на пагинации при переходе с первой на следующую старницу
    public void TestOne() throws InterruptedException {
        Setup();
        setParameters();
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)"); //скроллим вниз страницы
        driver.findElement(By.xpath("/html//div[@id='pagination']//a[.='20 объявлений']")).click(); //выбираем по сколько объявлений показывать
        driver.findElement(By.xpath("//span[@id='countOnPage']/span[1]")).click();  //жмем 10шт
        System.out.println("Select - Show 10 ads per page");
        Thread.sleep(a);
        JavascriptExecutor ts = ((JavascriptExecutor) driver);
        ts.executeScript("window.scrollTo(0, document.body.scrollHeight)"); //скроллим вниз страницы
        driver.findElement(By.linkText("Вперед →")).click();    //Идем на следующую страничку
        System.out.println("Click the Forward button");
        Thread.sleep(a);
        Check();
        System.out.println("All specified search parameters are saved on pagination when going to the next page. I give the go-ahead to the opening of champagne!");
        Thread.sleep(b);
        driver.quit(); //покидаем страницу
    }

    @Test           //Проверяем или заданные параметры поиска сохраняются на пагинации при переходе с первой на ПОСЛЕДНЮЮ старницу
    public void TestTwo() throws InterruptedException {
        Setup();
        setParameters();

        JavascriptExecutor qq = ((JavascriptExecutor) driver);
        qq.executeScript("window.scrollTo(0, document.body.scrollHeight)"); //скроллим вниз страницы
        driver.findElement(By.xpath("/html//div[@id='pagination']//a[.='20 объявлений']")).click(); //выбираем по сколько объявлений показывать
        driver.findElement(By.xpath("//span[@id='countOnPage']/span[1]")).click();  //жмем 10шт
        System.out.println("Select - Show 10 ads per page");    //выводим на экран сообщение, что все сработало
        Thread.sleep(a);
        JavascriptExecutor aa = ((JavascriptExecutor) driver);
        aa.executeScript("window.scrollTo(0, document.body.scrollHeight)"); //скроллим вниз страницы
        driver.findElement(By.cssSelector("#pagination > div > div.pager > span:nth-child(17) > a")).click();    //Идем на следующую страничку
        System.out.println("Press the button to go to the last page");
        Thread.sleep(a);
        Check();
        System.out.println("All specified search parameters are saved on pagination when going to the LAST PAGE. I give the go-ahead to the opening of champagne!");
        Thread.sleep(b);
        driver.quit();  //покидаем страницу
    }

    @Test               //Проверяем или заданные значения  передаются при переходе в Расширенный поиск
    public void TestMenu() throws InterruptedException {
        Setup();
        setParameters();
        //  проверяем или заданные параметры поиска сохраняются при переходе в "Расширенный поиск"
        driver.findElement(By.cssSelector("#topFilterLinkToAdvancedSearch")).click();   //при помощи селектора переходим в Расширенное меню
        System.out.println("Enter Advanced search menu");
        Thread.sleep(c);
        WebElement str = driver.findElement(By.xpath("//div[@id='app']//div[@class='extended']/div[@class='extended-grid']/div[1]/div[@class='indent']//div[.='Продажа домов']"));
        String fieldOne = str.getText();        //поик текста для проверки на соответствие
        Assert.assertEquals("Продажа домов", fieldOne);     //собственно проверка на соответствие
        System.out.println("Checking the compliance of the field 'I'm searching' with the specified parameters ............. Success!");
        Thread.sleep(d);
        driver.findElement(By.xpath("/html//input[@id='autocomplete']")).click();
        WebElement str1 = driver.findElement(By.xpath("//*[@id=\"app\"]/div[3]/div/div[2]/div/div[3]/div[2]/div/div[1]/div/div/div[2]/label[1]/span/span[2]"));
        String fieldTwo = str1.getText();       //поик текста для проверки на соответствие
        Assert.assertEquals("Винница", fieldTwo);        // проверка на соответствие
        System.out.println("Checking the compliance of the 'Enter City' field with the specified parameters ............. Success!");
        Thread.sleep(d);
        WebElement str2 = driver.findElement(By.xpath("//*[@id=\"app\"]/div[3]/div/div[2]/div/div[4]/div[2]/div/div/div/div/div[1]/div[2]/div[5]/label"));
        String fieldThree = str2.getText();
        Assert.assertEquals("Вишенка", fieldThree); // проверка на соответствие района
        System.out.println("Checking the compliance of the checkbox 'Vishenka' with the given parameters ............. Success!");
        Thread.sleep(d);
        WebElement str3 = driver.findElement(By.xpath("//*[@id=\"app\"]/div[3]/div/div[2]/div/div[4]/div[2]/div/div/div/div/div[2]/div[2]/div[4]/label"));
        String fieldFour = str3.getText();
        Assert.assertEquals("Зарванцы", fieldFour);     // проверка на соответствие района
        System.out.println("Checking the compliance of the checkbox 'Zarvantsy' with the given parameters ............. Success!");
        Thread.sleep(d);
        WebElement str4 = driver.findElement(By.xpath("//*[@id=\"app\"]/div[3]/div/div[2]/div/div[4]/div[2]/div/div/div/div/div[3]/div[2]/div[2]/label"));
        String fieldFive = str4.getText();
        Assert.assertEquals("Березина", fieldFive); // проверка на соответствие района
        System.out.println("Checking the compliance of the checkbox 'Berezina' with the given parameters ............. Success!");
        Thread.sleep(d);
        WebElement str8 = driver.findElement(By.xpath("//*[@id=\"app\"]/div[3]/div/div[2]/div/div[6]/div[2]/div[2]/div[1]/div/div/span/label[4]"));
        String buttTwo = str8.getText(); //Поиск текста
        Assert.assertEquals("4+", buttTwo); //проверка названия кнопки 4+
        System.out.println("Checking the compliance of the 'rooms' with the given parameters ............. Success!");
        Thread.sleep(1000);
        WebElement str5 = driver.findElement(By.cssSelector("#\\32 15_from"));
        String fieldSix = str5.getAttribute("value"); //находим значение 100
        Assert.assertEquals("100", fieldSix); //проверка поля площади дома
        System.out.println("Checking the compliance of the field 'Area of the house, Total from - 100' to the specified parameters ............. Success!!!");
        Thread.sleep(b);
        WebElement str6 = driver.findElement(By.cssSelector(".empty-spin div:nth-of-type(4) .indent .col2:nth-child(3)"));
        String fieldSeven = str6.getAttribute("value"); //находим значение 6
        Assert.assertEquals("6", fieldSeven); //проверка поля площади участка
        System.out.println("Checking the compliance of the field 'Estate area up to 6 acres' to the specified parameters ............. Sucess!");
        Thread.sleep(b);
        WebElement str7 = driver.findElement(By.cssSelector(".empty-spin div:nth-of-type(9) .indent .col2:nth-child(3)"));
        String fieldEight = str7.getAttribute("value"); //находим значение 100000
        Assert.assertEquals("100000", fieldEight); //проверка поля Цены
        System.out.println("Checking the compliance of the field 'Price to 100000' to the specified parameters ............. Success!");
        System.out.println("All specified search parameters are saved when we are going to the 'EXTENDED MENU'. I give the go-ahead to the opening of champagne!");
        Thread.sleep(b);
        driver.quit();      //покидаем страницу
    }
    @Test
    public void checkSearchResult() throws InterruptedException {       //Проверяем поисковую выдачу на соответствие заданным параметрам
        Setup();
        setParameters();
        String TypeM = driver.findElement(By.cssSelector("div.wrap_desc > ul > li:nth-child(2)")).getText();     // Ищем соответствие в поисковой выдаче
        Integer val = Integer.parseInt(TypeM);
        if (val >= 100){
            System.out.println("Search output 'Area of house' is Success!");
        }
        else{
            System.out.println("oops, incorrect search output");
        }
        WebElement TypeN = driver.findElement(By.cssSelector("div.wrap_desc > ul > li:nth-child(5)"));   // Ищем соответствие в поисковой выдаче
        Integer va2 = Integer.parseInt(TypeN.getAttribute("value"));
        if (va2 <= 6){
            System.out.println("Search output 'Area of land' is Success!");
        }
        else{
            System.out.println("oops, incorrect search output");
        }
        WebElement TypeV = driver.findElement(By.cssSelector("div.wrap_desc > div.mb-10 > div.size18.tit"));   // Ищем соответствие в поисковой выдаче
        String va4 = TypeV.getText();
        if (va4.contains("Зарванцы")){
            System.out.println("Search output 'Region of city' is Success!");
        }
        if (va4.contains("Березина")){
            System.out.println("Search output 'Region of city' is Success!");
        }
        if (va4.contains("Вишенка")){
            System.out.println("Search output 'Region of city' is Success!");
        }
        else{
            System.out.println("oops, incorrect search output");
        }
        driver.quit();   //покидаем страницу
    }
}

