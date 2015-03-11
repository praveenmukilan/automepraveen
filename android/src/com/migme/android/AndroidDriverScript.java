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

import org.testng.Assert;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
	public static WebDriver webdriver;
	public static Process appium;
	public static Properties OR;
	public static Properties andauto;
	public static WebDriverWait wait;
	public static String androidusername;
	public static String password;
	public static int screenShotIndx=0;
	public static int retry=0;
	public static String build_tag;

	public static String build_url;

	
	public static long startTime;
	public static long endTime;
	public static long runTimeInMinutes;
    public static String job_build;
    public static String apkURL;
    public static String githubChangesetURL;
	
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
			
			Log.infoTitle("test started @ : "+getCurrentTimeStamp());
	        Log.infoTitle("Test Suite Starts");
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
			
			Log.infoTitle("The tests run for "+ runTimeInMinutes + " minutes");
		    sendMail();
	// As the server start through code is having issues, commenting stopAppium()
	//		stopAppium();
		    
			Log.infoTitle("Test Suite Ends   --Pass");
			retry=0;
		}

		catch(SessionNotCreatedException e){
			//rerun the tests again. probably the server is not started.
			Log.info("Session not yet created..!!");

			retry++;
			if(retry<=2){
			main();
			}
			Log.infoTitle("Test Suite Ends   --Fail. Pls check the job : "+build_url);
			
		}
		catch(Exception e){
			
			Log.info(e.toString());
            System.out.println(e);

		}
		
		
	}
	
	
	
public static void test01()  {
		
		try{

		Log.infoTitle("Test Starts");
		
//
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.projectgoth:id/main_button")));


		
		privateChat();


		postImage();
		postTextEmoticons();
		groupChat();

		signOut();


		Log.infoTitle("Test Ends   --Pass");

		}
		catch(NoSuchElementException e){
			e.printStackTrace();
			Log.infoTitle("Test Ends   --Fail. Pls check the job "+build_url);
//			retry++;
//			if(retry<=3)
//				test01();
		}
		
		catch(Exception e){
			Log.info(e.getMessage());
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
	       
		
		Log.infoTitle("Setup Starts");

		
//		launchEmulator("");
      
		build_tag=andauto.getProperty("BUILD_TAG");

		build_url=andauto.getProperty("BUILD_URL");
		apkURL=andauto.getProperty("APKURL");
		githubChangesetURL=andauto.getProperty("GITHUB_CHANGESET_URL");
		
		String androidApkPath = andauto.getProperty("APKPATH");
		Log.info("build_tag : "+build_tag);
		Log.info("build_url : "+build_url);
		Log.info("Android Build APK URL : "+apkURL);
//		Log.info("job_build : "+job_build);
		
		Log.info("androidApkPath : "+androidApkPath);

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
		
		Log.info("driver started");
		Log.info("waiting for app to be lauched");
	
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
	
		Log.infoTitle("Setup Ends   --Pass");
	
		
	}catch(UnreachableBrowserException unbe){
//		
//		retry++;
//		if(retry<=3)
//		setUp();
	}

}
	public static void signIn() throws Exception{
		
		Log.infoTitle("Signin Starts");
		
		   try{

				 androidusername = new String(Base64.getDecoder().decode(OR.getProperty("username")));
				 password = new String(Base64.getDecoder().decode(OR.getProperty("password")));


				waitForElementPresent(MobileBy.AccessibilityId("txt_username"),5).sendKeys(androidusername);
				waitForElementPresent(MobileBy.AccessibilityId("txt_password"),5).sendKeys(password);


				
				driver.findElement(By.xpath(OR.getProperty("signinBtn_xpath"))).click();
				takeScreenShot(driver);
				
				Thread.sleep(Long.parseLong(OR.getProperty("mainBtnWaitSecs")));
			Log.infoTitle("Signin Ends   --Pass");
			
		   }
		   catch(Exception e){
		  
			   Log.infoTitle("Signin Fails. Please check!");
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
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
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
	Log.infoTitle("tearDown Starts");

/*
	if(!driver.equals(null))
	       driver.quit();
	killNodeAdbPlayer();
	*/
	Log.infoTitle("tearDown ends");

}

public static void postText(){
	Log.infoTitle("PostText Starts");
	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();	
	while(!isElementClickable(MobileBy.AccessibilityId(OR.getProperty("postBtn")), 5)){
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
//		Thread.sleep(2000);
	}
	
	driver.findElementByAccessibilityId(OR.getProperty("postBtn")).click();		
	driver.findElementById(OR.getProperty("postTextField")).sendKeys("posttext from android @ "+getCurrentTimeStamp()+" with random text : \n"+RandomStringUtils.randomAlphabetic(100));
	driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();		
	takeScreenShot(driver);
	Log.infoTitle("PostText Ends   --Pass");
}

public static void postImage(){
	//camera button differs between emulator & real device
	try{
	Log.infoTitle("PostImage Starts");	
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
		takeScreenShot(driver);
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if(isElementPresent(MobileBy.id(OR.getProperty("doneBtn")), 10)){
		    driver.findElementById(OR.getProperty("doneBtn")).click();
		    takeScreenShot(driver);
			driver.findElementById(OR.getProperty("postTextField")).sendKeys("Post Image-text from android @ "+getCurrentTimeStamp()+" : \n"+RandomStringUtils.randomAlphabetic(100));
			driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();
			takeScreenShot(driver);
		    Log.info("Post Image   --Pass");
		}
		
		else{

			
			Log.info("Camera is not working. Please check");

		    driver.findElementById(OR.getProperty("cameraBtnCancel")).click();
		    Log.info("image could not be taken using camera. pls check!");
//		    Log.infoTitle("PostImage Ends   --Fail. Pls check!");	
		 	goToFeedPage();
			}
	}
	
	else{
		//ToFix : Camera Error Alert should be clicked OK.
		if(isElementPresent(By.id("android:id/alertTitle"),5)){
			System.out.println("Camera Error");
			driver.findElementById("android:id/button3").click();
		}
	//ToFix : Camera Error Alert should be clicked OK.
		
		 driver.findElementById(OR.getProperty("cameraBtnCancel")).click();
		    Log.info("image could not be taken using camera. pls check!");
		    Log.infoTitle("PostImage Ends   --Fail. Pls check!");
		 	goToFeedPage();
	    }
	

	retry=0;
	}
    catch(Exception e){
    	//==this code makes the driver come out of the migme app. need to fix. infinite loop
    	Log.infoTitle("PostImage Ends   --Fail. Pls check!");
    	Log.info(e.getMessage());
     	goToFeedPage();
//    	while(!isElementPresent(MobileBy.AccessibilityId(OR.getProperty("mainBtn")), 10)){
////        	driver.launchApp(); 
//        	goBack();
//    	}
//    	

    	//postimage retry commented - starts
    /*
    	//add camera cancel button, so that the next function postemoticons can work
    	retry++;
    	if(retry<=1){
    		Log.info("Retrying Post Image..");

    		postImage();
    	}
    	else
    	{
    		Log.info("Retried two times. Post Image has issues. Please check.!");
    	 	goToFeedPage();

    	}
    	*/
    	//postimage retry commented - starts
    }
	
	
}

public static void goBack(){
	Log.info("goBack()");
	while(!isElementPresent(MobileBy.AccessibilityId("ab_title_icon"), 5)){
		driver.navigate().back();
		
	}
	Log.info("title page reached. hence going out of goBack()");
	
}

public static void postTextEmoticons(){
	
	try{
	Log.infoTitle("PostTextEmoticons Starts");


	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();	
	while(!isElementClickable(MobileBy.AccessibilityId(OR.getProperty("postBtn")), 5)){

		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(2000);
	}
	
	driver.findElementByAccessibilityId(OR.getProperty("postBtn")).click();
	while(!isElementClickable(MobileBy.id(OR.getProperty("postTextField")), 5)){
		Log.info("waiting for post text field to appear..");

		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		driver.findElementByAccessibilityId(OR.getProperty("postBtn")).click();
		Thread.sleep(2000);
	}
	driver.findElementById(OR.getProperty("postTextField")).sendKeys("postTextEmoticons from android @ "+getCurrentTimeStamp()+" with some random text : \n"+RandomStringUtils.randomAlphabetic(150));
	driver.findElementByAccessibilityId(OR.getProperty("emoticonBtn")).click();	
	
	driver.findElementByXPath(OR.getProperty("emoticonItem")).click();
	
	driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();
//	driver.findElementById(OR.getProperty("postTextField")).sendKeys(OR.getProperty("postTextLT300"));
	
	while(!isElementClickable(MobileBy.AccessibilityId("main_button"), 5)){
		Log.info("waiting for the post to be sent");
	driver.findElementByAccessibilityId(OR.getProperty("postSendBtn")).click();
	Thread.sleep(2000);
	}
	takeScreenShot(driver);
	Log.infoTitle("PostTextEmoticons Ends   -Pass");
	
	}catch(Exception e){
		takeScreenShot(driver);
		Log.infoTitle("PostTextEmoticons Ends   -Fail. Please check!");
		goToFeedPage();	
	}
}

public static void goToFeedPage(){
	try {
	Log.info("go to feed page");
	while(!isElementPresent(MobileBy.xpath("//android.widget.TextView[@text='Feed']"),5)){
	
		
	    goBack();
//    	driver.findElementByAccessibilityId("main_button").click();
//    	waitForElementPresent(MobileBy.AccessibilityId("ad_feed_orange"), 5);
	   
	    	driver.findElementByAccessibilityId("main_button").click();
			Thread.sleep(2000);
         	driver.findElementByAccessibilityId("ad_feed_orange").click();
         	
	}
	    }
	catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.info("failed to navigate to feed page. Launching the app again");
	    launchApp();
		
	}
	    
}
	

public static void launchApp(){
	try{
	driver.launchApp();
	Thread.sleep(10000);
	}catch(Exception e){
		
		Log.info("failed to lauch the app. Please check the app!");
		Log.info(e.getMessage());
		
	}
}

public static void chatToFeedPage() throws InterruptedException{
	Log.info("chatToFeedPage");

	Thread.sleep(10000);
	//to navigate back from chat window
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatBackBtn")), 5);
	//if the current page is recent chats, then the chatBackBtn is not available.
	if(isElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatBackBtn")), 10)){
	      driver.findElementByAccessibilityId(OR.getProperty("chatBackBtn")).click();
	}
	
	Log.info("navigated from the chat window to recent chats");

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
	Log.info("start newchat");
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("mainBtn")), 20).click();
//	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();

	while(!(isElementClickable(MobileBy.AccessibilityId(OR.getProperty("chatBtn")), 5) && isElementClickable(MobileBy.AccessibilityId(OR.getProperty("searchBtn")), 5) )){
		Log.info("**waiting for navigation button");
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(3000);
		}
	    Log.info("outof -navigation button");


//	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatBtn")), 15).click();
	driver.findElementByAccessibilityId(OR.getProperty("chatBtn")).click();

	Log.info("clicked chat navigation btn");
	Thread.sleep(5000);
		
	//main button click to view the new private group chat icon
	driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
	while(!(isElementClickable(MobileBy.AccessibilityId(OR.getProperty("newChatBtn")), 10) && isElementClickable(MobileBy.AccessibilityId(OR.getProperty("userInviteBtn")), 10))){
		Log.info("**waiting for ad chatadd white ");
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		Thread.sleep(2000);
		if(isElementClickable(MobileBy.AccessibilityId(OR.getProperty("chatBtn")), 5)){
			driver.findElementByAccessibilityId(OR.getProperty("chatBtn")).click();
			}
		
		}
	
	Log.info("wait over for ad_chatadd_white button");

	
//	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("newChatBtn")), 15).click();


	Log.info("clicked ad_chatadd_white button");
	
	driver.findElementByAccessibilityId(OR.getProperty("newChatBtn")).click();
	while(!isElementClickable(MobileBy.id(OR.getProperty("chatUserNamesList")),5)){
		Log.info("****");

		Thread.sleep(5000);
		
		driver.findElementByAccessibilityId(OR.getProperty("mainBtn")).click();
		driver.findElementByAccessibilityId(OR.getProperty("newChatBtn")).click();
		Log.info("waiting for chat user names to appear");

	}
	 retry=0;
	 driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	 Log.info("startNewChat ends");

	//driver.findElementByAccessibilityId("chat_list_tab").click();
	}catch(Exception e){
		Log.info("StartNewChat not working. Retrying..!!");
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
	Log.infoTitle("PrivateChat starts");

	//    Thread.sleep(10000);
	 startNewChat();
	
	WebElement username =  driver.findElementById(OR.getProperty("chatUserNamesList"));
	String usern = username.getText();
	username.click();
	
	//	//chat list
	//	driver.findElementById("com.projectgoth:id/label").click();
	//	//driver.findElementById("com.projectgoth:id/container").click();
	//
	//	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	
    Log.info("users selected");
// sendGiftInPrivateChat();
    
//    postEmoticonInChat();
    
    driver.findElementById(OR.getProperty("chatTextField")).click();
    String chatText="private chat from android- hi @ : "+getCurrentTimeStamp();
	driver.findElementById(OR.getProperty("chatTextField")).sendKeys(chatText);
	
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatSend")),5).click();
	takeScreenShot(driver);
	verifyChatInWebClient(usern,chatText);

	chatToFeedPage();
	retry=0;
	Log.infoTitle("PrivateChat --Pass");

	
	}catch(Exception e){
		retry++;
		if(retry<=2){
		Log.info(e.getMessage());
		Log.info("Retrying Private Chat again!");
		privateChat();
		}
		else{
		//skip this if the private chat is passed.
		Log.infoTitle("PrivateChat --Fail. Please check!");
		}
	}
}

public static void verifyChatInWebClient(String username, String chatText){
	   Log.info("Verify Chat in Web");
	   System.setProperty("webdriver.chrome.driver","src//resources//chromedriver");
	   webdriver = new ChromeDriver();
	   webdriver.manage().window().maximize();
	   webdriver.get("http://www.mig.me");
//	   waitForSecs(10);
	   webSignIn(username);
	   waitForSecs(8);
	   openSideBar();
	   openChatWindow(androidusername);
	   verifyChatText(chatText);
//	   getLastMessageStatus(chatText);
       webdriver.quit();
}

private static void webSignIn(String username){
	
	
	Log.info("Web signIn");
	
	webdriver.findElement(By.cssSelector("a[href='https://login.mig.me/web']")).click();

	String password=new String(Base64.getDecoder().decode("NjBzZSFpbk1T"));
	
	webdriver.findElement(By.cssSelector("input[name='mig33-username']")).sendKeys(username);
	webdriver.findElement(By.cssSelector("input[name='mig33-password']")).sendKeys(password);		
	webdriver.findElement(By.cssSelector("input[type='submit'][value='sign in']")).click();
	takeScreenShot(driver);
}

private static void openSideBar(){
	Log.info("Open Side bar");
	webdriver.findElement(By.cssSelector("span.icon-wb_arrowLeft")).click();
}



private static void openChatWindow(String androidUserName){
	
	try{
	Log.info("Open Web Chat Window");
	List<WebElement> chats = webdriver.findElements(By.cssSelector("div.text-truncate.chat-list-title"));
	
	for(WebElement e :chats){
		
//		Log.info("e.getText() : "+e.getText());
		
//		Log.info("androidusername : "+androidusername);
		
		if(e.getText().matches("^"+androidUserName+"$")){
			Log.info("Opening Chat window of user : "+androidusername);
			e.click();
			waitForSecs(5);
			return;
		}
		
		
		}//for
	}catch(Exception e){
		Log.info("Exception in openChatWindow() :");
		Log.info(e.getMessage());
	}
	
}

public static void verifyChatText(String chatText){
	
	Log.info("Verify Chat Text");
	String actualText = webdriver.findElement(By.cssSelector("div.chat-window-messages ul>li:nth-last-child(2)>div.row.chat-message>div.chat-msg-content>div.chat-msg-msg>span.message.normal")).getText();
	Log.info("expected : '"+chatText+"'"+"\nactual : '"+actualText+"'");
	Assert.assertEquals(chatText, actualText);
	takeScreenShot(webdriver);

}


public static void getLastMessageStatus(String chatText){
	
	Log.info("Get Message Status");
	waitForSecs(10);
	//li[id] > div.row.chat-message > .chat-msg-content > div.chat-msg-msg > span.message.normal getText()
	//li[id] > div.row.chat-message > .chat-msg-content getclass
	List<WebElement> chatMsgStatus = webdriver.findElements(By.cssSelector("li[id] > div.row.chat-message > .chat-msg-content.self-msg"));
	Log.info("chatMsgStatus.size() : "+chatMsgStatus.size());
	String msgStat = chatMsgStatus.get(chatMsgStatus.size()-1).getAttribute("class");
	Log.info(msgStat);
	takeScreenShot(driver);
	
	
	if(msgStat.matches("^.* msg-received.*")){
		Log.info("chat message received successfully");
	}
	else if(msgStat.matches("^.* msg-sent-ok.*")){
    
	    Log.info("chat message is not received yet and is only in sent status. Pls check!");
	}
	else{
		Log.info("chat message is not in received or sent state. Pls check! "+msgStat);
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
	takeScreenShot(driver);
	
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

		e.printStackTrace();
		return;
	}
}

public static void postEmoticonInChat(){
	driver.findElementById(OR.getProperty("chatTextField")).click();
	driver.findElementByAccessibilityId(OR.getProperty("chatEmoticon")).click();
	driver.findElementByAccessibilityId(OR.getProperty("chatEmoteItem")).click();
}

public static void groupChat() throws InterruptedException{
	
	try{
	Log.infoTitle("GroupChat starts");

	startNewChat();
	   // 1. Click Main Button
	   // 2. Click the New Chat Icon
	
	
//	List<WebElement> chatUsers= driver.findElementsById(OR.getProperty("chatUsersList"));
	List<WebElement> chatUsers = driver.findElementsById(OR.getProperty("chatUserNamesList"));

	if(chatUsers.size()<2){
		Log.info("The user is not having two friends to do a group chat");
	}

//	driver.findElementById(OR.getProperty("catName")).click();


	
	//add only two users of the list for a group chat
	for(int i=0; i<2;i++){
		chatUsers.get(i).click();
	}
	
	

	
	
//	sendGiftInGroupChat();

    postEmoticonInChat();
    driver.findElementById(OR.getProperty("chatTextField")).click();
	driver.findElementById(OR.getProperty("chatTextField")).sendKeys("group chat from android - hi @ : \n"+getCurrentTimeStamp());
	
	waitForElementPresent(MobileBy.AccessibilityId(OR.getProperty("chatSend")),5).click();
	takeScreenShot(driver);
	
	chatToFeedPage();
	Log.infoTitle("GroupChat -- Pass");
	}catch(Exception e){
		
		Log.infoTitle("GroupChat   --Fail. Pls check!");
	}
}

public static void waitForSecs(int seconds){
	
	try {
		Thread.sleep(seconds*1000);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.infoTitle("Wait -- Fail. Please check!");
	}
}



public static void takeScreenShot(WebDriver driver){
  
   
  if(screenShotIndx==0) {
	  job_build = build_tag.replace("jenkins-", "").replaceAll("[ ]", "");
//	  Log.info("job_build : "+job_build);
	  
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
	try{
	
	Log.infoTitle("SignOut Starts");

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
    takeScreenShot(driver);
	driver.findElement(By.xpath("//android.widget.TextView[@text='Sign out']")).click();

	Log.infoTitle("SignOut  --Pass");
	}catch(Exception e){
		
		Log.infoTitle("SignOut   --Fail. Please check!");
	}
	
}

public static String getCurrentTimeStamp(){
	
	 java.util.Date date= new java.util.Date();
//	 Log.info(new Timestamp(date.getTime()));
//System.out.println(new Timestamp(date.getTime()));
	 
	 Timestamp ts = new Timestamp(date.getTime());
	 return ts.toString();
	
}



public static void executeADBCommand(String command) throws ExecuteException, IOException, InterruptedException{
	Log.info("execute adb command");
	System.out.println("execute adb command");
	Log.info(command);

//adb shell "am start -a android.media.action.STILL_IMAGE_CAMERA" && sleep 1 && adb shell "input keyevent 27"
	
	CommandLine enterpass = new CommandLine("$ANDROID_HOME/platform-tools/"+command  );

	//CommandLine enterpass = new CommandLine("adb -s 192.168.56.101:5555 shell input text "+password);
	executor.execute(enterpass, resultHandler);
	
	Thread.sleep(10000);
}

public static void sendKeysUsingADB(String textString) throws ExecuteException, IOException, InterruptedException{
	Log.info("send keys using adb");
	System.out.println("send keys using adb");

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
	sendKeysUsingADB(androidusername);
	driver.findElementById("com.projectgoth:id/txt_password").click();
	sendKeysUsingADB(password);
}

/*
 * Method to launch the emulator programmatically
 * */

public static void launchEmulator(String vmName) throws ExecuteException, IOException, InterruptedException{
	
	
	Log.infoTitle("launchEmulator Starts");

	
	CommandLine launchEmul = new CommandLine("/Applications/Genymotion.app/Contents/MacOS/player");

	


	launchEmul.addArgument("--vm-name", false);
	launchEmul.addArgument(vmName);
	
	  

	executor.setExitValue(1);
	
	executor.execute(launchEmul, resultHandler);
	Thread.sleep(20000);
	
	Log.infoTitle("launchEmulator --Pass");

}

private static void killNodeAdbPlayer() throws ExecuteException, IOException, Exception{
	Log.info("kill Node Adb Player Starts");

	
	CommandLine killNode = new CommandLine("kill -9 $(lsof -i | grep 6723 | awk '{print $2}')");
	CommandLine killPlayer = new CommandLine("kill -9 $(lsof -i | grep 6724 | awk '{print $2}')");

	executor.setExitValue(1);
	executor.execute(killNode,resultHandler);
	executor.execute(killPlayer,resultHandler);

	Log.info("kill Node Adb Player Ends");

}


public static void startAppium() {
    //start appium instance
    try {
    	Log.info("Start Appium");

        Thread.sleep((long)(Math.random() * 10000)); //wait from 0 to 10 sec for parallel process run
        ProcessBuilder builder = new ProcessBuilder(getCmd());
        
//        builder.redirectOutput("path to log file"); //here you can find logs of appium
//        builder.redirectErrorStream(true);
        appium = builder.start();
        Thread.sleep(20000); //wait 20 sec until server started
        Log.info("Server Started");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void sendMail(){
	Log.infoTitle("Send the results in mail");
	 // Recipient's email ID needs to be mentioned.
    String to = andauto.getProperty("emailTo");
    String cc = andauto.getProperty("emailCC");

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
       
       message.setRecipients(Message.RecipientType.CC,
    	          InternetAddress.parse(cc));

       // Set Subject: header field
       message.setSubject("Android Automation Job '"+job_build.replace("-", "-Build#")+"' status");

       // Create the message part
       BodyPart messageBodyPart = new MimeBodyPart();

       // Now set the actual message
       
       java.io.File logF = new java.io.File("logfile.log");
       
       StringBuffer sb = new StringBuffer();
       sb.append("\nHi,\n\nAndroid automation job is triggered by the below Android build job.\n");
//       https://tools.projectgoth.com/jenkins/job/QA-CI%20androidV5/907/artifact/target/mig33Droid-5.01.005-SNAPSHOT.apk
       
       String androidJobURL = apkURL.replaceAll("^/artifact/.*", "");
       String apk = apkURL.replaceAll("^.*/target/", "");
       sb.append("\tAndroid Build Job : "+androidJobURL);
       sb.append("\n\tGithub Changeset URL : "+githubChangesetURL);
       sb.append("\n");
       sb.append("\tAPK : "+apk);
       sb.append("\n\n");
       sb.append("Please check the below Automation job url for details : \n\tAutomation Job URL : "+build_url);
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
       String filename = "logfile.log";
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
//       throw new RuntimeException(e);

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
			Log.info("Stop Appium");
		System.out.println("Stop Appium");
		//stop appium instance
		appium.destroy();
	}


	
}