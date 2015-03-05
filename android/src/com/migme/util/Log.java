package com.migme.util;

		import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

	public class Log{

		//Initialize Log4j logs
		private static Logger Log = Logger.getLogger(Log.class.getName());//
		private static String output="";

	// This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite
	public static void startTestCase(String sTestCaseName){

	   Log.info("****************************************************************************************");
	   Log.info("****************************************************************************************");
	   Log.info("$$$$$$$$$$$$$$$$$$$$$                 "+sTestCaseName+ "       $$$$$$$$$$$$$$$$$$$$$$$$$");
	   Log.info("****************************************************************************************");
	   Log.info("****************************************************************************************");

	   }

	//This is to print log for the ending of the test case
	public static void endTestCase(String sTestCaseName){
	   Log.info("XXXXXXXXXXXXXXXXXXXXXXX             "+"-E---N---D-"+"             XXXXXXXXXXXXXXXXXXXXXX");
	   Log.info("X");
	   Log.info("X");
	   Log.info("X");
	   Log.info("X");

	   }

    // Need to create these methods, so that they can be called  
	public static void info(String message) {
		String prefix="                    > ";
		Log.info(prefix+message);
		System.out.println(prefix+message);
		   output=output+"\n"+prefix+message;
		   }
	
	public static void infoTitle(String message){
		Log.info("*****          "+message);
		System.out.println("*****          "+message);
		output=output+"\n"+"*****          "+message;
	}

	public static void warn(String message) {
	   Log.warn(message);
	   }

	public static void error(String message) {
	   Log.error(message);
	   }

	public static void fatal(String message) {
	   Log.fatal(message);
	   }

	public static void debug(String message) {
	   Log.debug(message);
	   }
	
	public static String getLogOutput(){
		return output;
	}

	public static void takeScreenShot(WebDriver driver, String TestCaseName, String TestStepName) throws IOException{
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot.png"));
		
		
	}
	}