package com.autome.web;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.autome.web.utilities.Constants;
import com.autome.web.utilities.GoogleDrive;
import com.autome.web.utilities.Log;






public class MyTestSuite {
	
	public static WebDriver driver;
	public static Properties OR;
	public static Properties webprop;
	public static Properties taf;
	public static int screenShotIndx =0;
	private static  String ssPath;
	public static String ssFolderId ;
	private static String job_build;
	private static String build_tag;
	private static String build_url;
	private static double runTimeInMinutes;
	

	
	@BeforeClass
	public static void init(){
		
		try{
			
			//Launching Appium with command: '/Applications/Appium.app/Contents/Resources/node/bin/node' lib/server/main.js --address"127.0.0.1"--command-timeout"7200"--session-override --log"/tmp/appium.log"--log-timestamp --webhook"localhost:9876"--local-timezone --automation-name"Appium"--platform-name"Android"--platform-version"4.4"--full-reset --device-name"device"
			
		   Log.infoTitle("init");
		   OR= new Properties();
		   webprop = new Properties();
		   taf = new Properties();
		   
		   FileInputStream orFIS = new FileInputStream("src//resources//OR_Web.properties");
		   FileInputStream webFIS = new FileInputStream("src//resources//web.properties");

		   
           OR.load(orFIS);
           webprop.load(webFIS);
           DOMConfigurator.configure("log4j.xml");
           
           build_tag =webprop.getProperty("BUILD_TAG");
           build_url=webprop.getProperty("BUILD_URL");
           job_build = build_tag.replace("jenkins-", "").replaceAll("[ ]", "");
           
           String browser = webprop.getProperty("browser");
           
           // If the browser is Firefox, then do this
           
           if(browser.equalsIgnoreCase("firefox")) {
        		
       		Log.infoTitle("Tests run in Firefox");
       		FirefoxBinary binary = new FirefoxBinary(new File("/Applications/Firefox.app/Contents/MacOS/firefox-bin"));
       		FirefoxProfile profile = new FirefoxProfile();
       		driver = new FirefoxDriver(binary, profile);
//       		System.setProperty("webdriver.firefox.bin", "/Applications/Firefox.app/Contents/MacOS/firefox-bin");
//       		driver= new FirefoxDriver();

          
           // If browser is IE, then do this	  
          
           }
           else if (browser.equalsIgnoreCase("chrome")) { 
        	   
       		   Log.infoTitle("Tests run in Chrome");
        	 
        	   System.setProperty("webdriver.chrome.driver","src//resources//chromedriver");
        	 
        	   driver = new ChromeDriver();
        	 
        	  } 
          
           // Doesn't the browser type, lauch the Website
          
         
           
           
		}catch(FileNotFoundException e){
			
			
			Log.info(e.getMessage());

		}catch(IOException e){
			
			
			Log.info(e.getMessage());

		}
	
	}
	

	
	public static void runTestInChrome(){
		
		   Log.infoTitle("Tests run in Chrome");
		   System.setProperty("webdriver.chrome.driver","src//resources//chromedriver");
		   driver = new ChromeDriver();
		   runTest();
	}
	
	public static void runTestInIE(){
		  System.setProperty("webdriver.ie.driver","src//resources//chromedriver");
		   driver = new ChromeDriver();
		   runTest();
	}
	
	public static void runTestInFirefox(){
		
		Log.infoTitle("Tests run in Firefox");
		FirefoxBinary binary = new FirefoxBinary(new File("/Applications/Firefox.app/Contents/MacOS/firefox-bin"));
		FirefoxProfile profile = new FirefoxProfile();
		driver = new FirefoxDriver(binary, profile);
//		System.setProperty("webdriver.firefox.bin", "/Applications/Firefox.app/Contents/MacOS/firefox-bin");
//		driver= new FirefoxDriver();
		runTest();
	}
	
	@Test
	public static void runTest(){
		
		try {
		
		
		Log.infoTitle("runTest");
        Log.info("Test started at : "+getCurrentTimeStamp());
        Long startTime = System.currentTimeMillis();
		
		driver.get(OR.getProperty("URL"));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		driver.manage().window().maximize();

        signIn();
        Thread.sleep(5000);
//        skipTour();
////		//user logged in
//		postTextFriends();
//		goToMentionsPage();
//		goToFavouritesPage();
//		postTextMyself();	
		//Discover Page
		navigateDiscoverPage();		
		//Play Page
		goToPlayPage();
		//Store Page
		navigateToStorePage();
		//Merchant Page
		navigateMerchantPage();
		//notifications 
		viewNotifications();
		//share post using inner editor
		sharePostInnerEditor();
//		user profile settings
		userProfile();
//        
//        //post zero text. User should get a short-lived notification. Oh Snap! 
        postTextWithZeroChars();
        
        //text chat


		chatUserAvatar();
		privateChat();
		groupChat();
		
		
		signOut();
		Long endTime = System.currentTimeMillis();
		
		
			
        
			Thread.sleep(5000);
			
			Log.infoTitle("End Test");
			 runTimeInMinutes = (endTime-startTime)/(1000*60.0);
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			takeScreenShot();
		}
		catch(Exception e){
			
			takeScreenShot();
			Log.info(e.getMessage());
		}
        
//        if(driver!=null)
//        	driver.quit();
//		
		
	}
	
public static void takeScreenShot(){
		

	   
	  if(screenShotIndx==0) {
	  job_build = build_tag.replace("jenkins-", "").replaceAll("[ ]", "");
	  File ssDir= new File(Constants.screenShotDir+"//"+job_build);
	    ssDir.mkdirs();
	   ssPath = ssDir.getAbsolutePath();
	  }
	  screenShotIndx++;
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		try {
			File newFile = new File(ssPath+"//"+getFileName()+".jpg");
			Log.info("Screenshot at :"+newFile.getAbsolutePath());
			FileUtils.copyFile(scrFile,newFile );
//			FileUtils.copyFile(scrFile, new File(Constants.screenShotDir+"//"+DriverScript.sTestCaseID+"_"+DriverScript.sTestStepName+".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		}
	
	
	public static void uploadScreenShotToGoogleDrive(){
		
		try {

		  if(screenShotIndx==0) {
			  ssFolderId =  GoogleDrive.createFolder(job_build, GoogleDrive.migwebScreenShotsFolderID);
		  }
		  screenShotIndx++;
			
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			// Now you can do whatever you need to do with it, for example copy somewhere
		
				File newFile = new File(ssPath+"//"+getFileName()+".jpg");
				Log.info("Screenshot at :"+newFile.getAbsolutePath());
				FileUtils.copyFile(scrFile,newFile );
//				FileUtils.copyFile(scrFile, new File(Constants.screenShotDir+"//"+DriverScript.sTestCaseID+"_"+DriverScript.sTestStepName+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	
	public static String getFileName(){
		
		String filename=  getCurrentTimeStamp().replaceAll("[ .:-]","_");
//		
//		Log.info(filename);
//		Log.info(filename);
		return filename;
	}
	

	
	public static void signIn(){
		
		try{
		
		Log.infoTitle("signIn");

		
		driver.findElement(By.cssSelector(OR.getProperty("lnk_SignIn"))).click();
		String username=webprop.getProperty("username");
		String password=new String(Base64.getDecoder().decode(webprop.getProperty("password")));
		
		driver.findElement(By.cssSelector(OR.getProperty("txt_UserName"))).sendKeys(username);
		driver.findElement(By.cssSelector(OR.getProperty("txt_Password"))).sendKeys(password);		
		driver.findElement(By.cssSelector(OR.getProperty("btn_SignIn"))).click();
		takeScreenShot();
		}catch(Exception e){
			Log.info(e.getMessage());
		}
	}
	
	private static void postTextWithZeroChars(){
		try{
		
		Log.infoTitle("postTextWithZeroChars");
		if(driver.findElement(By.cssSelector(OR.getProperty("txt_post"))).isDisplayed()){
			driver.findElement(By.cssSelector(OR.getProperty("txt_post"))).click();
			}
			driver.findElement(By.cssSelector(OR.getProperty("txt_newPost"))).click();
			driver.findElement(By.cssSelector(OR.getProperty("btnpostboxShare"))).click();
			
			Assert.assertEquals(driver.findElement(By.cssSelector(OR.getProperty("zeroTextNotif"))).getText(),"Oh Snap!");
			
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	private static void postTextInit(String text){
		
		if(driver.findElement(By.cssSelector(OR.getProperty("txt_post"))).isDisplayed()){
		driver.findElement(By.cssSelector(OR.getProperty("txt_post"))).click();
		}
		driver.findElement(By.cssSelector(OR.getProperty("txt_newPost"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("txt_newPost"))).sendKeys(text+"Post Text @"+getCurrentTimeStamp()+":"+RandomStringUtils.randomAlphabetic(100));

		}
	
	public static String getCurrentTimeStamp(){
		
		 java.util.Date date= new java.util.Date();
//		 Log.info(new Timestamp(date.getTime()));
	//
	//Log.info(new Timestamp(date.getTime()));

		 
		 Timestamp ts = new Timestamp(date.getTime());
		 return ts.toString();
		
	}
	
	private static void postSelectMyself(){
		driver.findElement(By.cssSelector(OR.getProperty("drpdwnPostPrivacy"))).click();		
		driver.findElement(By.cssSelector(OR.getProperty("rdMyself"))).click();
	}
	
	private static void postSelectFriends(int indx){
		driver.findElements(By.cssSelector(OR.getProperty("drpdwnPostPrivacy"))).get(indx).click();		
		driver.findElements(By.cssSelector(OR.getProperty("rdFriends"))).get(indx).click();
	}
	
	private static void clickEmoticonsIcon(){
		
		driver.findElement(By.cssSelector(OR.getProperty("txt_Emoticons"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("insertEmoticons"))).click();
	}
	
	private static void sendPost(int indx){
		driver.findElements(By.cssSelector(OR.getProperty("btnpostboxShare"))).get(indx).click();
		driver.findElement(By.cssSelector(OR.getProperty("trendingTopicsLbl"))).click();
		takeScreenShot();
	}
	
	public static void goToMentionsPage(){
		try{
		
		Log.infoTitle("Mentions Page");

//		goToHomePage();
		
		waitForElementForSecs(By.cssSelector(OR.getProperty("mentionsPage")), 10);
		driver.findElement(By.cssSelector(OR.getProperty("mentionsPage"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void goToHomePage(){
		
		try{
		Log.info("HomePage #mig logo");
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		Assert.assertEquals(driver.getTitle(),"migme");

//		driver.get("http://www.mig.me/home");	
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void goToFavouritesPage(){
		try{
		
		Log.infoTitle("Favourites Page");
//		goToHomePage();
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("favouritesPage"))).click();
	    takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}

	
	public static void unSelectAllowReplies(){
		driver.findElement(By.cssSelector(OR.getProperty("ckbAllowReplies"))).click();
	}
	public static void postTextMyself(){
		try{
		Log.infoTitle("postTextMyself");
//		goToHomePage();
		driver.findElement(By.cssSelector(OR.getProperty("homePage"))).click();
		postTextInit("Myself");
		clickEmoticonsIcon();
		postSelectMyself();
		unSelectAllowReplies();
		takeScreenShot();
		sendPost(0);
	}catch(Exception e){
		Log.info(e.getMessage());
	}
		
		
	}
	
	public static void postTextFriends(){
		try{
		Log.infoTitle("postTextFriends");
		postTextInit("Friends");
		clickEmoticonsIcon();
		postSelectFriends(0);
		takeScreenShot();
		sendPost(0);
	}catch(Exception e){
		Log.info(e.getMessage());
	}
		
	}
	
	public static void navigateDiscoverPage(){
		try{
		Log.infoTitle("Discover Page");
		
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		discFeaturedPage();
		discStoriesPage();
		discPeoplePage();
		discUpdatesPage();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void discFeaturedPage(){
		try{
		Log.info("Feature Page");
		
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discFeatured"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	public static void discStoriesPage(){
		try{
		Log.info("Stories Page");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discStories"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void discContestsPage(){
		try{
		Log.info("Contests Page");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discContests"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	
	public static void discPeoplePage(){
		try{
		Log.info("People Page");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discPeople"))).click();

		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	public static void discUpdatesPage(){
		try{
		Log.info("Updates Page");
		driver.findElement(By.cssSelector(OR.getProperty("discDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("discUpdates"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void goToPlayPage(){

		try{
		Log.infoTitle("Play Page");
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("playPage"))).click();
		takeScreenShot();
//	WebElement webe=	driver.findElement(By.cssSelector(OR.getProperty("whatsHot")));
//
//	String value = webe.getText();
//	
//		
//		Log.info(value+": value");
//		Assert.assertEquals(value,"What's hot");
//		
//		Log.info("single player text :"+driver.findElement(By.cssSelector(OR.getProperty("singlePlayer"))).getText());
//		
//		Assert.assertEquals(driver.findElement(By.cssSelector(OR.getProperty("singlePlayer"))).getText(),"Single-player games");
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void navigateToStorePage(){
		try{
		Log.infoTitle("Store Page");
		
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
//		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		
		giftsPage();
		stickersPage();
		emoticonsPage();
		avatarPage();
		rechargePage();
		
	}catch(Exception e){
		Log.info(e.getMessage());
	}
		
		
	}
	
	public static void giftsPage(){
		try{
		Log.info("Gifts Page");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strGifts"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	public static void stickersPage(){
		try{
		Log.info("Stickers Page");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strStickers"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	public static void emoticonsPage(){
		try{
		Log.info("Emoticons Page");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strEmoticons"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	public static void avatarPage(){
		try{
		Log.info("Avatar Page");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strAvatar"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	public static void rechargePage(){
		try{
		Log.info("Recharge Page");
		driver.findElement(By.cssSelector(OR.getProperty("storeDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("strRecharge"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void navigateMerchantPage(){
		try{
		Log.infoTitle("Merchant Page");
		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("merchantDrpDwn"))).click();
		Assert.assertEquals(driver.getTitle(), OR.getProperty("merchantPageTitle"));
		takeScreenShot();
		driver.findElement(By.cssSelector(OR.getProperty("merchantToMigHome"))).click();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void viewNotifications(){
		try{
		
		Log.infoTitle("View Notifications");
//		driver.findElement(By.cssSelector(OR.getProperty("migLogo"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("notifications"))).click();
		takeScreenShot();
		driver.findElement(By.cssSelector(OR.getProperty("notifications"))).click();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
		
		
	}
	
	public static void sharePostInnerEditor(){
		try{
		Log.infoTitle("SharePost - Inner Editor");
		
		driver.findElement(By.cssSelector(OR.getProperty("sharePost"))).click();
		typeTextInnerEditor();

		postSelectFriends(1);
		takeScreenShot();
		//send post. There are two Share buttons similar. Top Post Share button is the second element.
		driver.findElements(By.cssSelector(OR.getProperty("btnpostboxShare"))).get(1).click();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void typeTextInnerEditor(){
		
		Log.info("Type text in Inner Editor");
		
		//choose the second textarea as the first is the normal post
		driver.findElements(By.cssSelector(OR.getProperty("innerEditor"))).get(1).sendKeys("Inner Editor - Post Text @"+getCurrentTimeStamp()+RandomStringUtils.randomAlphabetic(200));
		takeScreenShot();
	}
	
	public static void userProfile(){
		
		try{
		Log.infoTitle("User Profile");
		
		goToHomePage();
		driver.findElement(By.cssSelector(OR.getProperty("profileDrpDwn"))).click();
		driver.findElement(By.cssSelector(OR.getProperty("profileSettings"))).click();
		
		Assert.assertEquals(driver.getTitle(),"Settings - migme");
		goToHomePage();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
		
		
	}
	public static void privateChat(){
		try{
		Log.infoTitle("Start Private Chat from Side Bar");
		openSideBar();
		startNewPrivateChat();
		closeSideBar();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void groupChat(){
		try{
		Log.infoTitle("Start Group Chat from Side Bar");
		openSideBar();
		startNewGroupChat();
		closeSideBar();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void openSideBar(){
		
//		goToHomePage();
		Log.info("Open Side bar");
		driver.findElement(By.cssSelector("span.icon-wb_arrowLeft")).click();
	}
	
	public static void closeSideBar(){
		
		
		Log.info("Close Side bar");
		driver.findElement(By.cssSelector("span.icon-wb_arrowRight")).click();

	}
	
	public static void skipTour(){
		try{
		Log.info("Skip Tour");
		
		if(driver.findElement(By.cssSelector("button.btn.btn-xs.btn-inverse-default.pull-left.tour-close")).isDisplayed()){
			driver.findElement(By.cssSelector("button.btn.btn-xs.btn-inverse-default.pull-left.tour-close")).click();
			
			//click on get started button
			driver.findElement(By.cssSelector("button.btn.btn-xs.btn-default.tour-next")).click();
		}
		}catch(Exception e){			
			Log.info(e.getMessage());
		}
	}
	
	public static void startNewPrivateChat(){
		try {
		Log.info("Click Start New Private Chat");
	
		waitForSecs(5);
		
		driver.findElement(By.cssSelector("span.icon-wb_addchat")).click();
		//Enter username to chat with
		driver.findElement(By.cssSelector("li.search-field > input")).click();
	    chooseUserName();
		driver.findElement(By.cssSelector("input.start-chat.btn.btn-xs.btn-default[value='START CHAT']")).click();
		waitForElementForSecs(By.cssSelector("textarea.messagebox"), 5);
		String chatText = "Private chat @ "+getCurrentTimeStamp()+" "+RandomStringUtils.randomAlphabetic(100);
		driver.findElement(By.cssSelector("textarea.messagebox")).click();
		driver.findElement(By.cssSelector("textarea.messagebox")).sendKeys(chatText+Keys.RETURN);
	
        getLastMessageStatus(chatText);
		Log.info("closed the chat window.!");
		driver.findElement(By.cssSelector("span.icon-wb_chatclose")).click();

		} //try
		catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
	}//method
	
	public static void startNewGroupChat(){
		try {
		Log.info("Click Start New Group Chat");
		waitForSecs(5);
	
		driver.findElement(By.cssSelector("span.icon-wb_addchat")).click();
		//Enter username to chat with
		driver.findElement(By.cssSelector("li.search-field > input")).click();

		chooseUserNames();
		driver.findElement(By.cssSelector("input.start-chat.btn.btn-xs.btn-default[value='START CHAT']")).click();

		

		openRecentChat();
		
		String chatText = "Group chat @ "+getCurrentTimeStamp()+" "+RandomStringUtils.randomAlphabetic(100);
		driver.findElement(By.cssSelector("textarea.messagebox")).click();
		driver.findElement(By.cssSelector("textarea.messagebox")).sendKeys(chatText+Keys.RETURN);

		
        getLastMessageStatus(chatText);
		
		
		Log.info("closed the chat window.!");
		driver.findElement(By.cssSelector("span.icon-wb_chatclose")).click();
//		}//else
		} //try
		catch (Exception e) {
			Log.info("Group chat could not be started. Pls check!");

			Log.info(e.getMessage());
		}
	}//method
	
	public static void openRecentChat(){
		
		Log.info("Open Recent Chat");
		waitForSecs(5);
		
		driver.findElement(By.cssSelector("div.mCSB_container > div > div:nth-of-type(1).chat-item-row.row")).click();
	}
	
	
	public static void chooseUserName(){
		Log.info("Selecting a user");
		Log.info("1. "+driver.findElements(By.cssSelector("div.chosen-drop > ul.chosen-results > li")).get(0).getText());
		driver.findElements(By.cssSelector("div.chosen-drop > ul.chosen-results > li")).get(0).click();
	}
	
	public static void chooseUserNames(){
		
		try{
		Log.info("Selecting two users");
		
		List<WebElement> userNamesList = driver.findElements(By.cssSelector("div.chosen-drop > ul.chosen-results > li.active-result"));
		String chatUser1 = driver.findElement(By.cssSelector("div.chosen-drop > ul.chosen-results > li:nth-of-type(1)")).getText();
		String chatUser2 = driver.findElement(By.cssSelector("div.chosen-drop > ul.chosen-results > li:nth-of-type(2)")).getText();
//		Log.info("User1 :"+chatUser1); 		Log.info("User2 :"+chatUser2);
		if(userNamesList.size()<2){
			Log.info("The user is not having more than 1 user to chat with");
			
		}
		else{
			Log.info("1. "+chatUser1);
			driver.findElement(By.cssSelector("li.search-field > input")).click();
			driver.findElement(By.cssSelector("li.search-field > input")).sendKeys(chatUser1+Keys.RETURN);
		
			//type the second user name in the list
			Log.info("2. "+chatUser2);
			driver.findElement(By.cssSelector("li.search-field > input")).click();
			driver.findElement(By.cssSelector("li.search-field > input")).sendKeys(chatUser2+Keys.RETURN);
			
	
			}
		}catch(Exception e){
			Log.info("Check whether the user '"+webprop.getProperty("username")+"' is having atleast two chat users");
			Log.info(e.getMessage());
		}
	}
	
	public static void getLastMessageStatus(String chatText){
		try{
		Log.info("Get Message Status");
		waitForSecs(10);
		//li[id] > div.row.chat-message > .chat-msg-content > div.chat-msg-msg > span.message.normal getText()
		//li[id] > div.row.chat-message > .chat-msg-content getclass
		List<WebElement> chatMsgStatus = driver.findElements(By.cssSelector("li[id] > div.row.chat-message > .chat-msg-content.self-msg"));
		Log.info("chatMsgStatus.size() : "+chatMsgStatus.size());
		String msgStat = chatMsgStatus.get(chatMsgStatus.size()-1).getAttribute("class");
		Log.info(msgStat);
		takeScreenShot();
		
		
		if(msgStat.matches("^.* msg-received.*")){
			Log.info("chat message received successfully");
		}
		else if(msgStat.matches("^.* msg-sent-ok.*")){
        
		    Log.info("chat message is not received yet and is only in sent status. Pls check!");
		}
		else{
			Log.info("chat message is not in received or sent state. Pls check! "+msgStat);
		}
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	public static void chatUserAvatar(){
		
		try{
		openSideBar();
		waitForSecs(5);
		Log.infoTitle("Click Chat User Avatar");
		driver.findElement(By.cssSelector("div.chat-user-username>a")).click();
//		driver.findElement(By.cssSelector("img.img-circle")).click();
		String usrn=driver.findElement(By.cssSelector("div.user-username>a.profile-hovercard")).getText();
		Log.info("username : "+usrn);
		Assert.assertEquals(usrn, webprop.getProperty("username"));
		closeSideBar();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	
	public static void waitForElementForSecs(By locator, int secs){
		
//		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		
		WebDriverWait wait = new WebDriverWait(driver, secs);
	
		wait.until(ExpectedConditions.elementToBeClickable(locator));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public static void waitForSecs(int secs){
		try {
			Log.info("Wait for "+secs+" secs");
		for(int i=0;i<=secs;i++)
		
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.info(e.getMessage());
			}
	}
	public static void signOut(){
		try{
		Log.infoTitle("signOut");
		driver.findElement(By.cssSelector(OR.getProperty("dpd_Logout"))).click();
        driver.findElement(By.cssSelector(OR.getProperty("optLogout"))).click();
		takeScreenShot();
	}catch(Exception e){
		Log.info(e.getMessage());
	}
	}
	
	@AfterMethod
	public static void afterMethod(){
		
		if(driver!=null) {
			driver.quit(); 
			}
		
		
	}

	@AfterSuite
	public void tearDown(){
		if(driver!=null) {
			driver.quit(); 
			}
		
	}
	
	public static void sendMail(){
		Log.infoTitle("Send the results in mail");
		 // Recipient's email ID needs to be mentioned.
	    String to = webprop.getProperty("emailTo");
	    String cc = webprop.getProperty("emailCC");

	    // Sender's email ID needs to be mentioned
	    String from = "praveen.m@mig.me";

	    final String username = "praveen.m@mig.me";//change accordingly
	    final String password =  new String(Base64.getDecoder().decode(webprop.getProperty("emailPwd")));
	    

	    // Assuming you are sending email through relay.jangosmtp.net
	    String host = "smtp.gmail.com";
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", "25");

	    // Get the Session object.
	    Session session = Session.getInstance(props,
	       new javax.mail.Authenticator() {
	          protected PasswordAuthentication getPasswordAuthentication() {
	return new PasswordAuthentication(username, password);
	          }
	       });

	    try {
	       // Create a default MimeMessage object.
	       Message message = new MimeMessage(session);

	       // Set From: header field of the header.
	       message.setFrom(new InternetAddress(from));

	       // Set To: header field of the header.
	       message.setRecipients(Message.RecipientType.TO,
	          InternetAddress.parse(to));
	       
	       message.setRecipients(Message.RecipientType.CC,
	    	          InternetAddress.parse(cc));

	       // Set Subject: header field
	       message.setSubject("Web Automation Job '"+job_build.replace("-", "-Build#")+"' status");

	       // Create the message part
	       BodyPart messageBodyPart = new MimeBodyPart();

	       // Now set the actual message
	       
	       java.io.File logF = new java.io.File("logfile.log");
	       
	       StringBuffer sb = new StringBuffer();
	       sb.append("\nHi,\n\nWeb automation job is triggered by the Web client build job.\n");
//	       https://tools.projectgoth.com/jenkins/job/QA-CI%20androidV5/907/artifact/target/mig33Droid-5.01.005-SNAPSHOT.apk
	       



	       sb.append("Please check the below Automation job url for details : \n\t Web Automation Job URL : "+build_url);
	       sb.append("\n\t");
	       sb.append("Run time(mins) : "+runTimeInMinutes);
	       sb.append("\n\n\n Result : \n\n"+Log.getLogOutput());
	       
	    	  

	       
	       messageBodyPart.setText(sb.toString());

	       // Create a multipart message
	       Multipart multipart = new MimeMultipart();

	       // Set text message part
	       multipart.addBodyPart(messageBodyPart);

	       // Part two is attachment
	       messageBodyPart = new MimeBodyPart();
	       String filename = "weblog.txt";
	       DataSource source = new FileDataSource(filename);
	       messageBodyPart.setDataHandler(new DataHandler(source));
	       messageBodyPart.setFileName(filename);
	       multipart.addBodyPart(messageBodyPart);

	       // Send the complete message parts
	       message.setContent(multipart);

	       // Send message
	       Transport.send(message);

	       Log.info("Mail sent successfully....");

	    } catch (MessagingException e) {
	        Log.infoTitle("Send mail   --Fail. Pls check!");
	        Log.info(e.getMessage());
//	       throw new RuntimeException(e);

	    }
	  }
	
}
