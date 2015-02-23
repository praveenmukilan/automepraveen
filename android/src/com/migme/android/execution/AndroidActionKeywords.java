package com.migme.android.execution;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.migme.android.execution.AndyDriverScript.OR;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;

import com.migme.android.execution.AndyDriverScript;
import com.migme.util.Constants;
import com.migme.util.Log;

public class AndroidActionKeywords {
	
		public static WebDriver driver;
		
		
		public static void input(String object, String data){
			try{
				Log.info("Entering the text in " + object);
				takeScreenShot();
				driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			 }catch(Exception e){
				 Log.error("Not able to Enter UserName --- " + e.getMessage());
				 AndyDriverScript.bResult = false;
			 	}
			}
		
		public static void takeScreenShot(){
			
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			// Now you can do whatever you need to do with it, for example copy somewhere
			try {
				FileUtils.copyFile(scrFile, new File(Constants.screenShotDir+"//"+AndyDriverScript.sTestCaseID+"_"+AndyDriverScript.sTestStepName+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			public static void click(String object, String data){
				try{
					Log.info("Clicking on Webelement "+ object);
					driver.findElement(By.xpath(OR.getProperty(object))).click();
					takeScreenShot();
				 }catch(Exception e){
		 			Log.error("Not able to click --- " + e.getMessage());
		 			AndyDriverScript.bResult = false;
		         	}
				}
		
		/*
			
	
	public static void navigate(String object, String data){
		try{
			Log.info("Navigating to URL");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(Constants.URL);
			takeScreenShot();
		}catch(Exception e){
			Log.info("Not able to navigate --- " + e.getMessage());
			DriverScript.bResult = false;
			}
		}
	

	

	

	public static void waitFor(String object, String timeInMilliSecs) throws Exception{
		try{
			Log.info("Wait for - "+ timeInMilliSecs+" seconds");
			Thread.sleep(Integer.parseInt(timeInMilliSecs));
			takeScreenShot();
		 }catch(Exception e){
			 Log.error("Not able to Wait --- " + e.getMessage());
			 DriverScript.bResult = false;
         	}
		}

	public static void closeBrowser(String object, String data){
		try{
			Log.info("Closing the browser");
			driver.quit();
		 }catch(Exception e){
			 Log.error("Not able to Close the Browser --- " + e.getMessage());
			 DriverScript.bResult = false;
         	}
		}
	

		
		
	}
	
	public static void verifyTitle(String object, String data){
		Log.info("Verify the Page Title");
		if(!driver.getTitle().equals(data)){
			DriverScript.bResult = false;
		}
	}
	
	*/

	}