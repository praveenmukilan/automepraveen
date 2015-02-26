package com.autome.web;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;




public class MyTestSuite {
	
	public static WebDriver driver;
	public static Properties OR;
	public static Properties webprop;
	public static Properties taf;
	
	@BeforeClass
	public static void init(){
		
		try{
			System.out.println("************init**************");
		System.setProperty("webdriver.chrome.driver", "src//resources//chromedriver");

		   driver = new ChromeDriver();
		   OR= new Properties();
		   webprop = new Properties();
		   taf = new Properties();
		   
		   FileInputStream orFIS = new FileInputStream("src//resources//OR.properties");
		   FileInputStream webFIS = new FileInputStream("src//resources//web.properties");

		   
           OR.load(orFIS);
           webprop.load(webFIS);

           
           
		}catch(FileNotFoundException e){
			
			System.out.println(e.getMessage());
		}catch(IOException e){
			
			System.out.println(e.getMessage());
		}
	
	}
	
	@Test
	public static void runTest(){
		
		try {
		
		System.out.println("******************runTest********************");
		
		driver.get(OR.getProperty("URL"));
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
        signIn();

		//user logged in
		Assert.assertEquals(driver.getTitle(), "migme");
		postTextFriends();
		goToMentionsPage();
		goToFavouritesPage();
		postTextMyself();
		
		//Discover Page
		navigateDiscoverPage();
		
		
		signOut();
        
			Thread.sleep(5000);
			System.out.println("******************End Test********************");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(driver!=null)
        	driver.quit();
		
		
	}
	
	public static void signIn(){
		
		
		System.out.println("             # signIn             ");
		
		driver.findElement(By.cssSelector(OR.getProperty("lnk_SignIn"))).click();
		String username=new String(Base64.getDecoder().decode(webprop.getProperty("username")));
		String password=new String(Base64.getDecoder().decode(webprop.getProperty("password")));
		
		driver.findElement(By.cssSelector(OR.getProperty("txt_UserName"))).sendKeys(username);
		driver.findElement(By.cssSelector(OR.getProperty("txt_Password"))).sendKeys(password);		
		driver.findElement(By.cssSelector(OR.getProperty("btn_SignIn"))).click();
	}
	
	private static void postTextInit(){
		
		if(driver.findElement(By.cssSelector(OR.getProperty("txt_post"))).isDisplayed()){
		driver.findElement(By.cssSelector(OR.getProperty("txt_post"))).click();
		}
		driver.findElement(By.cssSelector(OR.getProperty("txt_newPost"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("txt_newPost"))).sendKeys("Post Text @ "+getCurrentTimeStamp()+" : "+RandomStringUtils.randomAlphabetic(100));
		driver.findElement(By.cssSelector(OR.getProperty("drpdwnPostPrivacy"))).click();
		}
	
	public static String getCurrentTimeStamp(){
		
		 java.util.Date date= new java.util.Date();
//		 Log.info(new Timestamp(date.getTime()));
	//System.out.println(new Timestamp(date.getTime()));
		 
		 Timestamp ts = new Timestamp(date.getTime());
		 return ts.toString();
		
	}
	
	private static void postSelectMyself(){
		
		driver.findElement(By.cssSelector(OR.getProperty("rdMyself"))).click();
	}
	
	private static void postSelectFriends(){
		
		driver.findElement(By.cssSelector(OR.getProperty("rdFriends"))).click();
	}
	
	private static void sendPost(){
		driver.findElement(By.cssSelector(OR.getProperty("btnpostboxShare"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("trendingTopicsLbl"))).click();

	}
	
	public static void goToMentionsPage(){
		
		System.out.println("             # Mentions Page             ");
//		goToHomePage();
		
		waitForElementForSecs(By.cssSelector(OR.getProperty("mentionsPage")), 10);
		driver.findElement(By.cssSelector(OR.getProperty("mentionsPage"))).click();
	}
	
	public static void goToHomePage(){
		
		System.out.println("             HomePage #mig logo            ");
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
//		driver.get("http://www.mig.me/home");	
	}
	
	public static void goToFavouritesPage(){

		System.out.println("             # Favourites Page             ");
//		goToHomePage();
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("favouritesPage"))).click();
	}

	
	public static void unSelectAllowReplies(){
		driver.findElement(By.cssSelector(OR.getProperty("ckbAllowReplies"))).click();
	}
	public static void postTextMyself(){
		System.out.println("             # postTextMyself             ");
//		goToHomePage();
		driver.findElement(By.cssSelector(OR.getProperty("homePage"))).click();
		postTextInit();
		postSelectMyself();
		unSelectAllowReplies();
		sendPost();
		
		
	}
	
	public static void postTextFriends(){
		System.out.println("             # postTextFriends             ");
		postTextInit();
		postSelectFriends();
		sendPost();
		
		
	}
	
	public static void navigateDiscoverPage(){
		System.out.println("             # Discover Page             ");
		
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		discFeaturedPage();
		discStoriesPage();
		discPeoplePage();
		discUpdatesPage();

	}
	
	public static void discFeaturedPage(){
		System.out.println("                  > Feature Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discFeatured"))).click();

	}
	public static void discStoriesPage(){
		System.out.println("                  > Stories Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discStories"))).click();

	}
	
	public static void discContestsPage(){
		System.out.println("                  > Contests Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discContests"))).click();

	}
	
	
	public static void discPeoplePage(){
		System.out.println("                  > People Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discPeople"))).click();

		
	}
	public static void discUpdatesPage(){
		System.out.println("                  > Updates Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discUpdates"))).click();

	}
	
	
	public static void waitForElementForSecs(By locator, int secs){
		
		WebDriverWait wait = new WebDriverWait(driver, secs);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	
	public static void signOut(){
		System.out.println("             # signOut             ");
		driver.findElement(By.cssSelector(OR.getProperty("dpd_Logout"))).click();
        driver.findElement(By.cssSelector(OR.getProperty("optLogout"))).click();
        
	}

//	@AfterSuite
	public void tearDown(){
		if(driver!=null) driver.quit();
		
	}
	
}
