package com.autome.web;


import java.io.File;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.autome.web.util.Constants;
import com.autome.web.util.GoogleDrive;
import com.autome.web.util.Log;




public class MyTestSuite {
	
	public static WebDriver driver;
	public static Properties OR;
	public static Properties webprop;
	public static Properties taf;
	public static int screenShotIndx =0;
	private static  String ssPath;
	public static String ssFolderId ;
	
	@BeforeClass
	public static void init(){
		
		try{
			
			//Launching Appium with command: '/Applications/Appium.app/Contents/Resources/node/bin/node' lib/server/main.js --address "127.0.0.1" --command-timeout "7200" --session-override --log "/tmp/appium.log" --log-timestamp --webhook "localhost:9876" --local-timezone --automation-name "Appium" --platform-name "Android" --platform-version "4.4" --full-reset --device-name "device"
			System.out.println("************init**************");
			Log.info("************init**************");

		System.setProperty("webdriver.chrome.driver", "src//resources//chromedriver");

		   driver = new ChromeDriver();
		   OR= new Properties();
		   webprop = new Properties();
		   taf = new Properties();
		   
		   FileInputStream orFIS = new FileInputStream("src//resources//OR.properties");
		   FileInputStream webFIS = new FileInputStream("src//resources//web.properties");

		   
           OR.load(orFIS);
           webprop.load(webFIS);
           DOMConfigurator.configure("log4j.xml");
           
           
		}catch(FileNotFoundException e){
			
			System.out.println(e.getMessage());
			Log.info(e.getMessage());

		}catch(IOException e){
			
			System.out.println(e.getMessage());
			Log.info(e.getMessage());

		}
	
	}
	
	@Test
	public static void runTest(){
		
		try {
		
		System.out.println("******************runTest********************");
		Log.info("******************runTest********************");

		
		driver.get(OR.getProperty("URL"));
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
        signIn();

		//user logged in

		postTextFriends();
		goToMentionsPage();
		goToFavouritesPage();
		postTextMyself();
		
		//Discover Page
		navigateDiscoverPage();
		
		//Play Page
		goToPlayPage();
		
		//Store Page
		navigateToStorePage();
		
		
		
		signOut();
        
			Thread.sleep(5000);
			System.out.println("******************End Test********************");
			Log.info("******************End Test********************");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			takeScreenShot();
		}
		catch(Exception e){
			
			takeScreenShot();
		}
        
        if(driver!=null)
        	driver.quit();
		
		
	}
	
public static void takeScreenShot(){
		

	   
	  if(screenShotIndx==0) {
		  
	  File ssDir= new File(Constants.screenShotDir+"//"+getFileName());
	    ssDir.mkdirs();
	   ssPath = ssDir.getAbsolutePath();
	  }
	  screenShotIndx++;
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		try {
			File newFile = new File(ssPath+"//"+getFileName()+".jpg");
			Log.info("Screenshot at : "+newFile.getAbsolutePath());
			FileUtils.copyFile(scrFile,newFile );
//			FileUtils.copyFile(scrFile, new File(Constants.screenShotDir+"//"+DriverScript.sTestCaseID+"_"+DriverScript.sTestStepName+".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		}
	
	
	public static void uploadScreenShotToGoogleDrive(){
		
		try {
		   GoogleDrive.getConnection();
		  if(screenShotIndx==0) {
			  ssFolderId =  GoogleDrive.createFolder(getFileName(), GoogleDrive.migautoFolderId);
		  }
		  screenShotIndx++;
			
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			// Now you can do whatever you need to do with it, for example copy somewhere
		
				File newFile = new File(ssPath+"//"+getFileName()+".jpg");
				Log.info("Screenshot at : "+newFile.getAbsolutePath());
				FileUtils.copyFile(scrFile,newFile );
//				FileUtils.copyFile(scrFile, new File(Constants.screenShotDir+"//"+DriverScript.sTestCaseID+"_"+DriverScript.sTestStepName+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	
	public static String getFileName(){
		
		String filename=  getCurrentTimeStamp().replaceAll("[ .:-]", "_");
//		System.out.println(filename);
//		Log.info(filename);
//		Log.info(filename);
		return filename;
	}
	

	
	public static void signIn(){
		
		
		System.out.println("             # signIn             ");
		Log.info("             # signIn             ");

		
		driver.findElement(By.cssSelector(OR.getProperty("lnk_SignIn"))).click();
		String username=new String(Base64.getDecoder().decode(webprop.getProperty("username")));
		String password=new String(Base64.getDecoder().decode(webprop.getProperty("password")));
		
		driver.findElement(By.cssSelector(OR.getProperty("txt_UserName"))).sendKeys(username);
		driver.findElement(By.cssSelector(OR.getProperty("txt_Password"))).sendKeys(password);		
		driver.findElement(By.cssSelector(OR.getProperty("btn_SignIn"))).click();
		takeScreenShot();
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
	//Log.info(new Timestamp(date.getTime()));

		 
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
		takeScreenShot();
	}
	
	public static void goToMentionsPage(){
		
		System.out.println("             # Mentions Page             ");
		Log.info("             # Mentions Page             ");

//		goToHomePage();
		
		waitForElementForSecs(By.cssSelector(OR.getProperty("mentionsPage")), 10);
		driver.findElement(By.cssSelector(OR.getProperty("mentionsPage"))).click();
		takeScreenShot();
	}
	
	public static void goToHomePage(){
		
		System.out.println("             HomePage #mig logo            ");
		Log.info("             HomePage #mig logo            ");
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		Assert.assertEquals(driver.getTitle(), "migme");

//		driver.get("http://www.mig.me/home");	
	}
	
	public static void goToFavouritesPage(){

		System.out.println("             # Favourites Page             ");
		Log.info("             # Favourites Page             ");
//		goToHomePage();
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("favouritesPage"))).click();
	    takeScreenShot();
	}

	
	public static void unSelectAllowReplies(){
		driver.findElement(By.cssSelector(OR.getProperty("ckbAllowReplies"))).click();
	}
	public static void postTextMyself(){
		System.out.println("             # postTextMyself             ");
		Log.info("             # postTextMyself             ");
//		goToHomePage();
		driver.findElement(By.cssSelector(OR.getProperty("homePage"))).click();
		postTextInit();
		postSelectMyself();
		unSelectAllowReplies();
		takeScreenShot();
		sendPost();
		
		
	}
	
	public static void postTextFriends(){
		System.out.println("             # postTextFriends             ");
		Log.info("             # postTextFriends             ");
		postTextInit();
		postSelectFriends();
		takeScreenShot();
		sendPost();
		
		
	}
	
	public static void navigateDiscoverPage(){
		System.out.println("             # Discover Page             ");
		Log.info("             # Discover Page             ");
		
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		discFeaturedPage();
		discStoriesPage();
		discPeoplePage();
		discUpdatesPage();

	}
	
	public static void discFeaturedPage(){
		System.out.println("                  > Feature Page                   ");
		Log.info("                  > Feature Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discFeatured"))).click();
		takeScreenShot();

	}
	public static void discStoriesPage(){
		System.out.println("                  > Stories Page                   ");
		Log.info("                  > Stories Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discStories"))).click();
		takeScreenShot();
	}
	
	public static void discContestsPage(){
		System.out.println("                  > Contests Page                   ");
		Log.info("                  > Contests Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discContests"))).click();
		takeScreenShot();
	}
	
	
	public static void discPeoplePage(){
		System.out.println("                  > People Page                   ");
		Log.info("                  > People Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discPeople"))).click();

		takeScreenShot();
	}
	public static void discUpdatesPage(){
		System.out.println("                  > Updates Page                   ");
		Log.info("                  > Updates Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discUpdates"))).click();
		takeScreenShot();
	}
	
	public static void goToPlayPage(){

		System.out.println("             # Play Page             ");
		Log.info("             # Play Page             ");
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("playPage"))).click();
		takeScreenShot();
//	WebElement webe=	driver.findElement(By.cssSelector(OR.getProperty("whatsHot")));
//
//	String value = webe.getText();
//	
//		System.out.println(value+" : value");
//		Log.info(value+" : value");
//		Assert.assertEquals(value, "What's hot");
//		System.out.println("single player text : "+driver.findElement(By.cssSelector(OR.getProperty("singlePlayer"))).getText());
//		Log.info("single player text : "+driver.findElement(By.cssSelector(OR.getProperty("singlePlayer"))).getText());
//		
//		Assert.assertEquals(driver.findElement(By.cssSelector(OR.getProperty("singlePlayer"))).getText(), "Single-player games");
	}
	
	public static void navigateToStorePage(){
		System.out.println("             # Store Page             ");
		Log.info("             # Store Page             ");
		
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
//		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		
		giftsPage();
		stickersPage();
		emoticonsPage();
		avatarPage();
		rechargePage();
		
		
	}
	
	public static void giftsPage(){
		System.out.println("                  > Gifts Page                   ");
		Log.info("                  > Gifts Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strGifts"))).click();
		takeScreenShot();
	}
	public static void stickersPage(){
		System.out.println("                  > Stickers Page                   ");
		Log.info("                  > Stickers Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strStickers"))).click();
		takeScreenShot();
	}
	public static void emoticonsPage(){
		System.out.println("                  > Emoticons Page                   ");
		Log.info("                  > Emoticons Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strEmoticons"))).click();
		takeScreenShot();
	}
	public static void avatarPage(){
		System.out.println("                  > Avatar Page                   ");
		Log.info("                  > Avatar Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strAvatar"))).click();
		takeScreenShot();
	}
	public static void rechargePage(){
		System.out.println("                  > Recharge Page                   ");
		Log.info("                  > Recharge Page                   ");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strRecharge"))).click();
		takeScreenShot();
	}
	
	
	public static void waitForElementForSecs(By locator, int secs){
		
		WebDriverWait wait = new WebDriverWait(driver, secs);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	
	public static void signOut(){
		System.out.println("             # signOut             ");
		Log.info("             # signOut             ");
		driver.findElement(By.cssSelector(OR.getProperty("dpd_Logout"))).click();
        driver.findElement(By.cssSelector(OR.getProperty("optLogout"))).click();
		takeScreenShot();
	}

//	@AfterSuite
	public void tearDown(){
		if(driver!=null) driver.quit();
		
	}
	
}
