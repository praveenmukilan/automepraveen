package com.migme.android;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.migme.util.Constants;

public class SetupAndroidAppium{

	public static AndroidDriver driver;
	public static Process appium;
	
	static DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
	static DefaultExecutor executor = new DefaultExecutor();

	
	public static void setUp() throws Exception {
		Properties prop = new Properties();
		
		//From the build, the apkFile.properties will reside in $WORSPACE/android
		
		FileInputStream fis = new FileInputStream("apkFile.properties");
		
		prop.load(fis);
		
		System.out.println("****************$$$ setUP Starts****************");
		
//		System.out.println("ANDROID_HOME : "+System.getenv("ANDROID_HOME"));
//		System.out.println("PATH : "+System.getenv("PATH"));
		
/*
		resultHandler = new DefaultExecuteResultHandler();
		executor = new DefaultExecutor();
		executor.setExitValue(1);
//		executor.execute(killNode,resultHandler);
//		executor.execute(killPlayerEmulator,resultHandler);
//		executor.execute(killADB,resultHandler);
		
	//	CommandLine command = new CommandLine("/bin/sh -c");
	//	command.addArgument("/Applications/Appium.app/Contents/Resources/node/bin/node",false);
		
		CommandLine command = new CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");

		
		command.addArgument("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js", false);
		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument("6723");
		command.addArgument("--bootstrap-port", false);
		command.addArgument("6724");
//		command.addArgument("--no-reset", false);

		executor.execute(command, resultHandler);
*/
//		startAppium();
	
		/*
	System.out.println("<<>>*********** Please wait for 15 seconds ***********");
		
		Thread.sleep(15000);
		*/
		
		String androidApkPath = prop.getProperty("APKPATH");
		
		System.out.println("androidApkPath : "+androidApkPath);
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("platformName", "Android");		
//		capabilities.setCapability("platformVersion", "4.4.2");
//		capabilities.setCapability("browserName", "android");
		
		/* appPackage & appActivity is not mandatory as the Appium will inspect the .apk file for the default package and the activity
		 * Issues if any, please set it explicitly.
		 * */
//		capabilities.setCapability("appPackage", "com.migmeplayground");
//		capabilities.setCapability("appActivity", "com.projectgoth.activity.MainActivity");
//		https://tools.projectgoth.com/jenkins/view/3.%20Mobile/job/QA-CI%20androidV5/ws/target/
//		capabilities.setCapability("app", "/Users/Praveen/APPIUM/mig33Droidv5.00.020.apk");
		capabilities.setCapability("app", androidApkPath);
	
//		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "device");		
		

//      The device name could be found by running the command adb in the terminal window.
		/*
		 * 
		 * android-sdk-macosx$ adb devices
							   List of devices attached 
							   192.168.56.101:5555	device
		 * 
		 * */
		capabilities.setCapability("deviceName", "device");
		
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);

		System.out.println("****");
	
	
		
		
	//	driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		
	
	
		driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
		

		System.out.println("****************setUp Ends****************");
}
	
	public static void main(String args[]){
		
		
		try{
			
			System.out.println("****************main Starts****************");
			killNodeAdbPlayer();
		launchEmulator();
//		Thread.sleep(20000);
		setUp();
		test01();
		tearDown();
		stopAppium();
		System.out.println("****************main Ends****************");
		}
		catch(Exception e){
			
			System.out.println(e);
		}
		
	}
	
	@Test
	public static void main(){
		
		
		try{
			
			System.out.println("****************main Starts****************");
			killNodeAdbPlayer();
		launchEmulator();
//		Thread.sleep(20000);
		setUp();
		test01();
// As the server start through code is having issues, commenting stopAppium()
//		stopAppium();
		System.out.println("****************main Ends****************");
		}
		catch(Exception e){
			
			System.out.println(e);
		}
		
	}

@AfterMethod
public static void tearDown() throws Exception {
	System.out.println("****************tearDown Starts****************");

	if(!driver.equals(null))
	       driver.quit();
	killNodeAdbPlayer();
	System.out.println("****************tearDown Ends****************");
}


public static void test01() throws InterruptedException {
System.out.println("Jesus");
//AndroidDriver andy = (AndroidDriver)driver;
System.out.println("****************test01 Starts****************");

WebDriverWait wait = new WebDriverWait(driver, 30); 
wait.until(ExpectedConditions.elementToBeClickable(By.id("com.projectgoth:id/txt_username")));


//driver.findElement(By.xpath("//android.view.View[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.EditText[1]")).sendKeys("praveenmukilan");
driver.findElement(By.id("com.projectgoth:id/txt_username")).sendKeys("praveenmukilan");
//driver.findElementsByAndroidUIAutomator("com.projectgoth:id/txt_username");

//driver.findElement(By.xpath("//android.view.View[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.EditText[2]")).sendKeys("60se!inMS");
driver.findElement(By.id("com.projectgoth:id/txt_password")).sendKeys("60se!inMS");

//driver.findElement(By.xpath("//android.view.View[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.Button[2]")).click();
driver.findElement(By.id("com.projectgoth:id/btn_signin")).click();

//driver.findElement(By.id("com.projectgoth:id/txt_username"));
//driver.findElement(MobileBy.ByAndroidUIAutomator.)

//Steps to sign out

//Button Icon: com.projectgoth:id/button_icon

driver.findElement(By.id("com.projectgoth:id/button_icon")).click();

//Menu Settings: com.projectgoth:id/menu_settings

//driver.findElement(By.id("com.projectgoth:id/menu_settings"));
driver.findElementByAccessibilityId("setting").click();



//Click on Sign out Link
//driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.projectgoth:id/menu_settings' and @index='2']")).click();

//List <WebElement> elmnts = driver.findElement("com.projectgoth:id/label");
//driver.findElement(By.name("setting")).click();
// Thread.sleep(500000);

driver.findElement(By.xpath("//android.widget.TextView[@text='Sign out']")).click();


System.out.println("****************test01 Ends****************");

 }


/*
 * Method to launch the emulator programmatically
 * */

public static void launchEmulator() throws ExecuteException, IOException, InterruptedException{
	
	
	System.out.println("****************launchEmulator Starts****************");
	
	CommandLine launchEmul = new CommandLine("/Applications/Genymotion.app/Contents/MacOS/player");

	


	launchEmul.addArgument("--vm-name", false);
	launchEmul.addArgument("SamsungGalaxyS4");
	
	  

	executor.setExitValue(1);
	
	executor.execute(launchEmul, resultHandler);
	Thread.sleep(20000);
	
	System.out.println("****************launchEmulator Ends****************");
}

private static void killNodeAdbPlayer() throws ExecuteException, IOException, Exception{
	System.out.println("****************kill Node Adb Player Starts****************");
	
	CommandLine killNode = new CommandLine("kill -9 $(lsof -i | grep 6723 | awk '{print $2}')");
	CommandLine killPlayer = new CommandLine("kill -9 $(lsof -i | grep 6724 | awk '{print $2}')");

	executor.setExitValue(1);
	executor.execute(killNode,resultHandler);
	executor.execute(killPlayer,resultHandler);

	System.out.println("****************kill Node Adb Player Ends****************");
}


public static void startAppium() {
    //start appium instance
    try {
    	System.out.println("*************Start Appium*****************");
        Thread.sleep((long)(Math.random() * 10000)); //wait from 0 to 10 sec for parallel process run
        ProcessBuilder builder = new ProcessBuilder(getCmd());
        
//        builder.redirectOutput("path to log file"); //here you can find logs of appium
//        builder.redirectErrorStream(true);
        appium = builder.start();
        Thread.sleep(3000); //wait 3 sec until server started
        System.out.println("Server Started");
    } catch (Exception e) {
        e.printStackTrace();
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
cmd.add("6723");
cmd.add("--bootstrap-port");
cmd.add("6724 ");


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

System.out.println("Command : "+sb.toString());

return cmd;
}

/*
public static void getStringA throws ExecuteException, IOException{
	
	
	resultHandler = new DefaultExecuteResultHandler();
	executor = new DefaultExecutor();
	executor.setExitValue(1);
//	executor.execute(killNode,resultHandler);
//	executor.execute(killPlayerEmulator,resultHandler);
//	executor.execute(killADB,resultHandler);
	
//	CommandLine command = new CommandLine("/bin/sh -c");
//	command.addArgument("/Applications/Appium.app/Contents/Resources/node/bin/node",false);
	
	CommandLine command = new CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");

	
	command.addArgument("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js", false);
	command.addArgument("--address", false);
	command.addArgument("127.0.0.1");
	command.addArgument("--port", false);
	command.addArgument("6723");
	command.addArgument("--bootstrap-port", false);
	command.addArgument("6724");
//	command.addArgument("--no-reset", false);

	executor.execute(command, resultHandler);


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

System.out.println("Command : "+sb.toString());
	
}

*/
public static void stopAppium(){
	System.out.println("****************Stop Appium****************");
//stop appium instance
appium.destroy();
}

	
}
