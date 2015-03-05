package com.migme.android;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;






import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.migme.util.Constants;
import com.migme.util.Log;




public class AndroidDriverScript{

	public static AppiumDriver driver;
	public static Process appium;
	public static Properties OR;
	public static Properties andauto;
	public static WebDriverWait wait;
	public static String username;
	public static String password;
	public static int screenShotIndx=0;
	public static int retry=0;
	public static String build_tag;

	public static String build_url;

	
	public static long startTime;
	public static long endTime;
	public static long runTimeInMinutes;
    public static String job_build;
	
	static DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
	static DefaultExecutor executor = new DefaultExecutor();
	private static  String ssPath;
	
	@Test
	public static void main() throws ExecuteException, IOException, InterruptedException{
		
		
		try{
			
			startTime=System.currentTimeMillis();
			//Delete the existing logfile and create a new one 
			File logFile = new File("logfile.log");
			logFile.delete();
			logFile.createNewFile();
			
			DOMConfigurator.configure("log4j.xml");
			
			Log.info("test run @ : "+startTime);
			System.out.println("test run @ : "+getCurrentTimeStamp());
	        Log.info("****************main Starts****************");
			System.out.println("****************main Starts****************");
//			startAppium();
//			launchEmulator();
	//			killNodeAdbPlayer();
	//		launchEmulator();
   //       appium --address "127.0.0.1" --command-timeout "7200" --session-override --log "/tmp/appium.log" --log-timestamp --webhook "localhost:9876" --local-timezone --automation-name "Appium" --platform-name "Android" --platform-version "4.4" --full-reset --device-name "device"
	//		Thread.sleep(20000);
			setUp();
			test01();
			endTime = System.currentTimeMillis();
			
		     runTimeInMinutes = (endTime-startTime)/(1000*60);
			
			System.out.println("The tests run for "+ runTimeInMinutes + " minutes");
		    sendMail();
	// As the server start through code is having issues, commenting stopAppium()
	//		stopAppium();
		    
			Log.info("****************main Ends****************");
			System.out.println("****************main Ends****************");
			retry=0;
		}

		catch(SessionNotCreatedException e){
			//rerun the tests again. probably the server is not started.
			System.out.println("Session not yet created..!!");

			retry++;
			if(retry<=2){
			main();
			}
			
		}
		catch(Exception e){
			
			Log.info(e.toString());
            System.out.println(e);

		}
		
		
	}
	
	
	
public static void test01()  {
		
		try{

		Log.info("****************test01 Starts****************");
		System.out.println("****************test01 Starts****************");
//
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

//		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.projectgoth:id/main_button")));

		
		privateChat();
//		postText();
		postImage();
		postTextEmoticons();
		newGroupChat();

		signOut();


		Log.info("****************test01 Ends****************");
		System.out.println("****************test01 Ends****************");
		}
		catch(NoSuchElementException e){
			e.printStackTrace();
//			retry++;
//			if(retry<=3)
//				test01();
		}
		
		catch(Exception e){
			retry++;
			if(retry<=3)
				test01();
			
		}
		 }

	
	public static void setUp() throws Exception {
		
		try{
		
		Properties prop = new Properties();
		 andauto = new Properties();
		OR = new Properties();
		//From the build, the apkFile.properties will reside in $WORSPACE/android
		FileInputStream andautoFis = new FileInputStream("androidauto.properties");
//		FileInputStream fis = new FileInputStream("src//config//androidauto.properties");
		FileInputStream orFis = new FileInputStream("src//config//OR_Android.txt");
 
      
		andauto.load(andautoFis);
//		prop.load(fis);
		OR.load(orFis);
	       
		
		Log.info("****************$$$ setUP Starts****************");
		System.out.println("****************$$$ setUP Starts****************");
		
//		launchEmulator("");
      
		build_tag=andauto.getProperty("BUILD_TAG");

		build_url=andauto.getProperty("BUILD_URL");
		
		String androidApkPath = andauto.getProperty("APKPATH");
		System.out.println("build_tag : "+build_tag + "\nbuild_url :"+build_url);
		
		Log.info("androidApkPath : "+androidApkPath);
		System.out.println("androidApkPath : "+androidApkPath);
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("platformName", "Android");		
		capabilities.setCapability("app", androidApkPath);
		capabilities.setCapability("deviceName", "device");
		capabilities.setCapability("udid", andauto.getProperty("udid"));
		/*
		#udid emulator
		udid="192.168.56.101:5555"
		#udid real device
		#udid=a214daff
		*/
	
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
		
		Log.info("****");
		System.out.println("****");
		Log.info("waiting for app to be lauched");
		System.out.println("waiting for app to be lauched");
		if(!(driver!=null)){
		Thread.sleep(80000);
		}
       //wait for app to be lauched

		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		
		signIn();

		while(!isElementClickable(MobileBy.AccessibilityId(OR.getProperty("mainBtn")), 10)){
			System.out.print("***");
		Thread.sleep(5000);
		}
		retry=0;
	
		Log.info("****************setUp Ends****************");
		System.out.println("****************setUp Ends****************");
		
	}catch(UnreachableBrowserException unbe){
//		
//		retry++;
//		if(retry<=3)
//		setUp();
	}

}
	public static void signIn() throws Exception{
		
		System.out.println("****************signIn starts****************");
		
		   try{

				 username = new String(Base64.getDecoder().decode(OR.getProperty("username")));
				 password = new String(Base64.getDecoder().decode(OR.getProperty("password")));


				waitForElementPresent(MobileBy.AccessibilityId("txt_username"),5).sendKeys(username);
				waitForElementPresent(MobileBy.AccessibilityId("txt_password"),5).sendKeys(password);


				
				driver.findElement(By.xpath(OR.getProperty("signinBtn_xpath"))).click();
				takeScreenShot();
				
				Thread.sleep(Long.parseLong(OR.getProperty("mainBtnWaitSecs")));
			
		   }
		   catch(NoSuchElementException e){
		  
			   
		   }
					
				}

	
	
	

	

	public static WebElement waitForElementPresent(final By by, int timeOutInSeconds) {

        WebElement element; 
//        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
        try{
        	
            WebDriverWait wait = new WebDriverWait(driver, 80); 
            element = wait.until(ExpectedConditions.elementToBeClickable(by));

            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS); //reset implicitlyWait
            return element; //return the element
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null; 
    }
	
 public static Boolean isElementPresent(final By by, int timeOutInSeconds) {

        WebElement element; 
//        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
        try{
        	
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds); 
            element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            

            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS); //reset implicitlyWait
            return true; //return the element
        } catch (Exception e) {
            return false;
        } 
    
    }
 
 public static Boolean isElementClickable(final By by, int timeOutInSeconds) {

     WebElement element; 
//     driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
     try{
     	
         WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds); 
         element = wait.until(ExpectedConditions.elementToBeClickable(by));
         

         driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS); //reset implicitlyWait
         return true; //return the element
     } catch (Exception e) {
         return false;
     } 
 
 }

@AfterMethod
public static void tearDown() throws Exception {
	Log.info("****************tearDown Starts****************");
System.out.println("****************tearDown Starts****************");
/*
	if(!driver.equals(null))
	       driver.quit();
	killNodeAdbPlayer();
	*/
	Log.info("****************tearDown Ends****************");
System.out.println("****************tearDown Ends****************");
}

public static void postText(){
	Log.info("*****postText()****************");
System.out.println("*****postText()****************");
			
	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();	
	while(!isElementClickable(MobileBy.AccessibilityId(OR.getProperty("postBtn")), 5)){
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
//		Thread.sleep(2000);
	}
	
	driver.findElementByAccessibilityId(OR.getProperty("postBtn")).click();		
	driver.findElementById(OR.getProperty("postTextField")).sendKeys("posttext @ "+RandomStringUtils.randomAlphabetic(100));
	driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();		
	takeScreenShot();
}

public static void postImage(){
	//camera button differs between emulator & real device
	try{
	Log.info("*****postImage()****************");
System.out.println("*****postImage()****************");
	
	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();	
	while(!isElementClickable(MobileBy.AccessibilityId(OR.getProperty("postBtn")), 5)){
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(2000);
	}
	driver.findElementByAccessibilityId(OR.getProperty("postBtn")).click();		
	driver.findElementByAccessibilityId(OR.getProperty("photoBtn")).click();
	driver.findElementByXPath(OR.getProperty("cameraOption")).click();
	
	if(isElementClickable(By.id(OR.getProperty("shutterBtn")), 5)){
		driver.findElementById(OR.getProperty("shutterBtn")).click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if(isElementPresent(MobileBy.id(OR.getProperty("doneBtn")), 10))
		    driver.findElementById(OR.getProperty("doneBtn")).click();
		else{
			//ToFix : Camera Error Alert should be clicked OK.
				if(isElementPresent(By.id("android:id/alertTitle"),5)){
					System.out.println("Camera Error");
					driver.findElementById("android:id/button3").click();
				}
			//ToFix : Camera Error Alert should be clicked OK.
			
			Log.info("Camera is not working. Please check");
System.out.println("Camera is not working. Please check");
		    driver.findElementById(OR.getProperty("cameraBtnCancel")).click();
		    Log.info("image could not be taken using camera. pls check!");
System.out.println("image could not be taken using camera. pls check!");
			}
	}
	
	driver.findElementById(OR.getProperty("postTextField")).sendKeys("Post Image @ "+getCurrentTimeStamp()+" "+RandomStringUtils.randomAlphabetic(100));
	driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();
	takeScreenShot();
	retry=0;
	}
    catch(Exception e){
    	//==this code makes the driver come out of the migme app. need to fix. infinite loop
    	
    	while(!isElementPresent(MobileBy.AccessibilityId(OR.getProperty("mainBtn")), 10)){
//        	driver.launchApp(); 
        	goBack();
    	}
    	

    	
    
    	//add camera cancel button, so that the next function postemoticons can work
    	retry++;
    	if(retry<=1){
    		Log.info("Retrying Post Image..");
    		System.out.println("Retrying Post Image..");
    		postImage();
    	}
    	else
    	{
    		Log.info("Retried two times. Post Image has issues. Please check.!");
    		System.out.println("Retried two times. Post Image has issues. Please check.!");
    		
    		return;
    	}
    	
 
    }
	
	
}

public static void goBack(){
	Log.info("****************go back()*************");
	System.out.println("****************go back()*************");
	driver.navigate().back();
}

public static void postTextEmoticons(){
	
	try{
	Log.info("*****postTextEmoticons()****************");
	System.out.println("*****postTextEmoticons()****************");

	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();	
	while(!isElementClickable(MobileBy.AccessibilityId(OR.getProperty("postBtn")), 5)){

		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(2000);
	}
	
	driver.findElementByAccessibilityId(OR.getProperty("postBtn")).click();
	while(!isElementClickable(MobileBy.id(OR.getProperty("postTextField")), 5)){
		Log.info("waiting for post text field to appear..");
    System.out.println("waiting for post text field to appear..");
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		driver.findElementByAccessibilityId(OR.getProperty("postBtn")).click();
		Thread.sleep(2000);
	}
	driver.findElementById(OR.getProperty("postTextField")).sendKeys("postTextEmoticons @ "+getCurrentTimeStamp()+" with some random text"+RandomStringUtils.randomAlphabetic(150));
	driver.findElementByAccessibilityId(OR.getProperty("emoticonBtn")).click();	
	
	driver.findElementByXPath(OR.getProperty("emoticonItem")).click();
	
	driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();
//	driver.findElementById(OR.getProperty("postTextField")).sendKeys(OR.getProperty("postTextLT300"));
	
	while(!isElementClickable(MobileBy.AccessibilityId("main_button"), 5)){
		System.out.println("waiting for the post to be sent");
	driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();
	Thread.sleep(2000);
	}
	takeScreenShot();
	
	}catch(Exception e){
		
		sendMail();
	}
}




public static void chatToFeedPage() throws InterruptedException{
	Log.info("*****************chatToFeedPage*********************");
System.out.println("*****************chatToFeedPage*********************");
	Thread.sleep(10000);
	//to navigate back from chat window
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatBackBtn")), 5);
	//if the current page is recent chats, then the chatBackBtn is not available.
	if(isElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatBackBtn")), 10)){
	      driver.findElementByAccessibilityId(OR.getProperty("chatBackBtn")).click();
	}
	
	System.out.println("navigated from the chat window to recent chats");

	//to navigate back to feed screen, click main button & click on feed button
	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
	while(!isElementPresent(MobileBy.AccessibilityId(OR.getProperty("feedBtn")), 10)){
		System.out.print("**");
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(2000);
		}

	driver.findElementByAccessibilityId(OR.getProperty("feedBtn")).click();
	Thread.sleep(5000);

}
public static void startNewChat() throws InterruptedException{
	
	try{
	Log.info("*****************startNewChat*********************");
System.out.println("*****************startNewChat*********************");
	
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("mainBtn")), 20).click();
//	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();

	while(!(isElementClickable(MobileBy.AccessibilityId(OR.getProperty("chatBtn")), 5) && isElementClickable(MobileBy.AccessibilityId(OR.getProperty("searchBtn")), 5) )){
		System.out.print("**inwhile -navigation button");
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(3000);
		}
	Log.info("out of while -- -navigation button");
System.out.println("out of while -- -navigation button");

//	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatBtn")), 15).click();
	driver.findElementByAccessibilityId(OR.getProperty("chatBtn")).click();
	System.out.println("clicked navigation button");
	Log.info("clicked navigation button");
	Thread.sleep(5000);
		
	//main button click to view the new private group chat icon
	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
	while(!(isElementClickable(MobileBy.AccessibilityId(OR.getProperty("newChatBtn")), 10) && isElementClickable(MobileBy.AccessibilityId(OR.getProperty("userInviteBtn")), 10))){
		System.out.print("**inwhile- ad chatadd white ");
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(2000);
		if(isElementClickable(MobileBy.AccessibilityId(OR.getProperty("chatBtn")), 5)){
			driver.findElementByAccessibilityId(OR.getProperty("chatBtn")).click();
			}
		
		}
	
	Log.info("out of while -- -ad_chatadd_white button");
System.out.println("out of while -- -ad_chatadd_white button");
	
//	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("newChatBtn")), 15).click();

	System.out.println("clicked ad_chatadd_white button");
	Log.info("clicked ad_chatadd_white button");
	
	driver.findElementByAccessibilityId(OR.getProperty("newChatBtn")).click();
	while(!isElementClickable(MobileBy.id(OR.getProperty("chatUserNamesList")),5)){
		Log.info("****");
		System.out.println("****");
		Thread.sleep(5000);
		
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		driver.findElementByAccessibilityId(OR.getProperty("newChatBtn")).click();
		Log.info("waiting for chat user names to appear");
		System.out.println("waiting for chat user names to appear");
	}
	 retry=0;
	 Log.info("*****************startNewChat ends*********************");
System.out.println("*****************startNewChat ends*********************");
	//driver.findElementByAccessibilityId("chat_list_tab").click();
	}catch(Exception e){
		System.out.println("StartNewChat not working. Retrying..!!");
		retry++;
		if(retry<=3){
			chatToFeedPage();
			startNewChat();
		}
		
	}
	
}

public static void sleepDriver(int secs) throws InterruptedException{
	Thread.sleep(secs*1000);
}


public static void privateChat() {
	
	try{
	Log.info("*****privateChat starts*********************");
System.out.println("*****privateChat starts*********************");
//    Thread.sleep(10000);
 startNewChat();

 driver.findElementById(OR.getProperty("chatUserNamesList")).click();
//	//chat list
//	driver.findElementById("com.projectgoth:id/label").click();
//	//driver.findElementById("com.projectgoth:id/container").click();
//
//	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);


// sendGiftInPrivateChat();
    
    postEmoticonInChat();
    driver.findElementById(OR.getProperty("chatTextField")).click();
	driver.findElementById(OR.getProperty("chatTextField")).sendKeys("private chat - hi @ :"+getCurrentTimeStamp());
	
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatSend")),5).click();
	
	

	chatToFeedPage();
	retry=0;
	Log.info("*****privateChat ends*********************");
System.out.println("*****privateChat ends*********************");
	
	}catch(Exception e){
		retry++;
		if(retry<=2){
		privateChat();
		}
	}
}

public static void chooseGiftInChat(){
	
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatGiftIcon")),3).click();
	
	while(isElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatGiftPrice3Cents")),5)){
		driver.findElementByAccessibilityId(OR.getProperty("chatGiftIcon")).click();
	}
//	driver.findElementByAccessibilityId(OR.getProperty("chatGiftIcon")).click();
	waitForSecs(5);
	chooseGiftInOrder();
//	chooseGiftInRandom();
	
}

public static void chooseGiftInOrder(){
	
	driver.findElementById(OR.getProperty("chatGiftPrice3Cents")).click();
	
}

public static void chooseGiftInRandom(){
	
	List<WebElement> gifts = driver.findElementsById(OR.getProperty("chatGiftPrice3Cents"));
	
	//randomly select a gift
	gifts.get(new Random().nextInt(gifts.size())).click();
}


public static void selectUserForGift(){
	

	if(driver.findElementByXPath(OR.getProperty("chatGiftUserSelect"))!=null)
		driver.findElementByXPath(OR.getProperty("chatGiftUserSelect")).click();
	

}

public static void clickNextButtonChatGift(){
	
	driver.findElementById(OR.getProperty("chatGiftNextBtn")).click();
}

public static void sendGiftInChat(){
	

	driver.findElementById(OR.getProperty("chatGiftSend")).click();
	waitForSecs(3);
	takeScreenShot();
	
	if(isElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatGiftSentCloseBtn")),5))
	     waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatGiftSentCloseBtn")), 5).click();
//	driver.findElementByAccessibilityId(OR.getProperty("chatGiftSentCloseBtn")).click();

}

public static void sendGiftInPrivateChat(){
	try{
	chooseGiftInChat();
	sendGiftInChat();
	}
	catch(NoSuchElementException e){
		
		Log.info("element could not be found :");
System.out.println("element could not be found :");
		e.printStackTrace();
		return;
	}
}

public static void sendGiftInGroupChat(){
	
	try{
		chooseGiftInChat();
	
	selectUserForGift();
	clickNextButtonChatGift();
	sendGiftInChat();
	}
	
	catch(NoSuchElementException e){
		
		Log.info("element could not be found :");
System.out.println("element could not be found :");
		e.printStackTrace();
		return;
	}
}

public static void postEmoticonInChat(){
	driver.findElementById(OR.getProperty("chatTextField")).click();
	driver.findElementByAccessibilityId(OR.getProperty("chatEmoticon")).click();
	driver.findElementByAccessibilityId(OR.getProperty("chatEmoteItem")).click();
}

public static void newGroupChat() throws InterruptedException{
	Log.info("*****newGroupChat starts*********************");
	System.out.println("*****newGroupChat starts*********************");
	startNewChat();
	   // 1. Click Main Button
	   // 2. Click the New Chat Icon
	
	
//	List<WebElement> chatUsers= driver.findElementsById(OR.getProperty("chatUsersList"));
	List<WebElement> chatUsers = driver.findElementsById(OR.getProperty("chatUserNamesList"));

	if(chatUsers.size()<2){
		Log.info("The user is not having two friends to do a group chat");
	System.out.println("The user is not having two friends to do a group chat");
	}

//	driver.findElementById(OR.getProperty("catName")).click();


	
	//add only two users of the list for a group chat
	for(int i=0; i<2;i++){
		chatUsers.get(i).click();
	}
	
	

	
	
//	sendGiftInGroupChat();

    postEmoticonInChat();
    driver.findElementById(OR.getProperty("chatTextField")).click();
	driver.findElementById(OR.getProperty("chatTextField")).sendKeys("group chat - hi @ :"+getCurrentTimeStamp());
	
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatSend")),5).click();
	
	chatToFeedPage();
	Log.info("*****newGroupChat ends*********************");
	System.out.println("*****newGroupChat ends*********************");
}

public static void waitForSecs(int seconds){
	
	try {
		Thread.sleep(seconds*1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



public static void takeScreenShot(){
  
   
  if(screenShotIndx==0) {
	  job_build = build_tag.replace("jenkins-", "").replaceAll("[ ]", "");
	  System.out.println("job_build : "+job_build);
	  
  File ssDir= new File(Constants.screenShotDir+"//"+job_build);
    ssDir.mkdirs();
   ssPath = ssDir.getAbsolutePath();
  }
  screenShotIndx++;
	
	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	// Now you can do whatever you need to do with it, for example copy somewhere
	try {
		File newFile = new File(ssPath+"//"+getFileName()+".jpg");
		Log.info("Screenshot at : "+newFile.getAbsolutePath());
		System.out.println("Screenshot at : "+newFile.getAbsolutePath());
		FileUtils.copyFile(scrFile,newFile );
//		FileUtils.copyFile(scrFile, new File(Constants.screenShotDir+"//"+DriverScript.sTestCaseID+"_"+DriverScript.sTestStepName+".jpg"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}

public static String getFileName(){
	
	String filename=  getCurrentTimeStamp().replaceAll("[ .:-]", "_");
//	System.out.println(filename);
	return filename;
}

public static void signOut(){
	
	
	Log.info("*****SignOut*********************");
	System.out.println("*****SignOut*********************");
	// to navigate to menu settings window
	driver.findElement(By.id("com.projectgoth:id/button_icon")).click();


	//Steps to sign out

	//Button Icon: com.projectgoth:id/button_icon
	driver.findElementByAccessibilityId("menu_settings").click();
	//driver.findElement(By.id("com.projectgoth:id/button_icon")).click();

	//Menu Settings: com.projectgoth:id/menu_settings

	//driver.findElement(By.id("com.projectgoth:id/menu_settings"));
	//driver.findElementByAccessibilityId("setting").click();



	//Click on Sign out Link
	//driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.projectgoth:id/menu_settings' and @index='2']")).click();

	//List <WebElement> elmnts = driver.findElement("com.projectgoth:id/label");
	//driver.findElement(By.name("setting")).click();
	// Thread.sleep(500000);

	driver.findElement(By.xpath("//android.widget.TextView[@text='Sign out']")).click();


	
}

public static String getCurrentTimeStamp(){
	
	 java.util.Date date= new java.util.Date();
//	 Log.info(new Timestamp(date.getTime()));
//System.out.println(new Timestamp(date.getTime()));
	 
	 Timestamp ts = new Timestamp(date.getTime());
	 return ts.toString();
	
}



public static void executeADBCommand(String command) throws ExecuteException, IOException, InterruptedException{
	Log.info("************execute adb command************");
	System.out.println("************execute adb command************");
	Log.info(command);
	System.out.println(command);
//adb shell "am start -a android.media.action.STILL_IMAGE_CAMERA" && sleep 1 && adb shell "input keyevent 27"
	
	CommandLine enterpass = new CommandLine("$ANDROID_HOME/platform-tools/"+command  );

	//CommandLine enterpass = new CommandLine("adb -s 192.168.56.101:5555 shell input text "+password);
	executor.execute(enterpass, resultHandler);
	
	Thread.sleep(10000);
}

public static void sendKeysUsingADB(String textString) throws ExecuteException, IOException, InterruptedException{
	Log.info("************send keys using adb************");
	System.out.println("************send keys using adb************");

	char a[] = textString.toCharArray();
	for(char b : a){
	CommandLine enterpass = new CommandLine("/Users/Praveen/APPIUM/android-sdk-macosx/platform-tools/adb -s 192.168.56.101:5555 shell input text "+b);

	//CommandLine enterpass = new CommandLine("adb -s 192.168.56.101:5555 shell input text "+password);
	executor.execute(enterpass, resultHandler);
	}
	Thread.sleep(10000);
}
public static void populateUserCredentialsUsingADB() throws ExecuteException, IOException, InterruptedException{
	
	driver.findElementById("com.projectgoth:id/txt_username").click();
	sendKeysUsingADB(username);
	driver.findElementById("com.projectgoth:id/txt_password").click();
	sendKeysUsingADB(password);
}

/*
 * Method to launch the emulator programmatically
 * */

public static void launchEmulator(String vmName) throws ExecuteException, IOException, InterruptedException{
	
	
	Log.info("****************launchEmulator Starts****************");
	System.out.println("****************launchEmulator Starts****************");
	
	CommandLine launchEmul = new CommandLine("/Applications/Genymotion.app/Contents/MacOS/player");

	


	launchEmul.addArgument("--vm-name", false);
	launchEmul.addArgument(vmName);
	
	  

	executor.setExitValue(1);
	
	executor.execute(launchEmul, resultHandler);
	Thread.sleep(20000);
	
	Log.info("****************launchEmulator Ends****************");
	System.out.println("****************launchEmulator Ends****************");
}

private static void killNodeAdbPlayer() throws ExecuteException, IOException, Exception{
	Log.info("****************kill Node Adb Player Starts****************");
	System.out.println("****************kill Node Adb Player Starts****************");
	
	CommandLine killNode = new CommandLine("kill -9 $(lsof -i | grep 6723 | awk '{print $2}')");
	CommandLine killPlayer = new CommandLine("kill -9 $(lsof -i | grep 6724 | awk '{print $2}')");

	executor.setExitValue(1);
	executor.execute(killNode,resultHandler);
	executor.execute(killPlayer,resultHandler);

	Log.info("****************kill Node Adb Player Ends****************");
	System.out.println("****************kill Node Adb Player Ends****************");
}


public static void startAppium() {
    //start appium instance
    try {
    	Log.info("*************Start Appium*****************");
	System.out.println("*************Start Appium*****************");
        Thread.sleep((long)(Math.random() * 10000)); //wait from 0 to 10 sec for parallel process run
        ProcessBuilder builder = new ProcessBuilder(getCmd());
        
//        builder.redirectOutput("path to log file"); //here you can find logs of appium
//        builder.redirectErrorStream(true);
        appium = builder.start();
        Thread.sleep(20000); //wait 20 sec until server started
        Log.info("Server Started");
	System.out.println("Server Started");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void sendMail(){
	
	 // Recipient's email ID needs to be mentioned.
    String to = andauto.getProperty("emailTo");

    // Sender's email ID needs to be mentioned
    String from = "praveen.m@mig.me";

    final String username = "praveen.m@mig.me";//change accordingly
    final String password =  new String(Base64.getDecoder().decode(andauto.getProperty("emailPwd")));
    

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

       // Set Subject: header field
       message.setSubject(build_tag+" status");

       // Create the message part
       BodyPart messageBodyPart = new MimeBodyPart();

       // Now set the actual message
       
       java.io.File logF = new java.io.File("logfile.log");
      

       messageBodyPart.setText("Android automation execution status.\n Please check the below URL : \n"+build_url+ "\n\n\nThe test '"+job_build+"' run for "+runTimeInMinutes +" minutes.\n\n\n Result : \n\n"+Log.getLogOutput());
       
       

       // Create a multipar message
       Multipart multipart = new MimeMultipart();

       // Set text message part
       multipart.addBodyPart(messageBodyPart);

       // Part two is attachment
       messageBodyPart = new MimeBodyPart();
       String filename = "logfile.log";
       DataSource source = new FileDataSource(filename);
       messageBodyPart.setDataHandler(new DataHandler(source));
       messageBodyPart.setFileName(filename);
       multipart.addBodyPart(messageBodyPart);

       // Send the complete message parts
       message.setContent(multipart);

       // Send message
       Transport.send(message);

       System.out.println("Sent message successfully....");

    } catch (MessagingException e) {
       throw new RuntimeException(e);
    }
  }


	private static List<String> getCmd(){
		//create cmd by adding each param
		List<String>  cmd = new ArrayList<String>();
		
		cmd.add("/Applications/Appium.app/Contents/Resources/node/bin/node");
		
		cmd.add("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js");
		cmd.add("--address");
		cmd.add("127.0.0.1");
		cmd.add("--port");
		cmd.add("4723");
		cmd.add("--bootstrap-port");
		cmd.add("4724 ");
		
		
		cmd.add(" --log");
		cmd.add(" /Users/Praveen/APPIUM/AppiumServer.log");
		//cmd.append("--webhook");
		//cmd.append("localhost:9876");
		cmd.add(" --log-timestamp");
		cmd.add(" --local-timezone");
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("/Applications/Appium.app/Contents/Resources/node/bin/node ");
		
		sb.append("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js ");
		sb.append("--address ");
		sb.append("127.0.0.1 ");
		sb.append("--port ");
		sb.append("6723 ");
		sb.append("--bootstrap-port ");
		sb.append("6724 ");
		
		
		sb.append(" --log ");
		sb.append(" /Users/Praveen/APPIUM/AppiumServer.log ");
		//cmd.append("--webhook");
		//cmd.append("localhost:9876");
		sb.append(" --log-timestamp ");
		sb.append(" --local-timezone ");
		
		Log.info("Command : "+sb.toString());
		System.out.println("Command : "+sb.toString());
		
		return cmd;
	}
	
	public static void stopAppium(){
			Log.info("****************Stop Appium****************");
		System.out.println("****************Stop Appium****************");
		//stop appium instance
		appium.destroy();
	}


	
}