package com.migme.util;

public class Constants {
	//System Variables
	public static String URL;
	public static String Path_TestData;
	public static String Path_OR;
	public static String File_TestData;
	public static String KEYWORD_FAIL;
	public static String KEYWORD_PASS;
	
	//Data Sheet Column Numbers
	public static final int Col_TestCaseID = 0;	
	public static final int Col_TestScenarioID =1 ;
	public static final int Col_PageObject =4 ;
	public static final int Col_ActionKeyword =5 ;
	public static final int Col_RunMode =2 ;
	public static final int Col_Result =3 ;
	public static final int Col_DataSet =6 ;
	public static final int Col_TestStepResult =7 ;
		
	// Data Engine Excel sheets
	public static final String Sheet_TestSteps = "Test Steps";
	public static final String Sheet_TestCases = "TestSuite";
	
	// Test Data
	public static final String UserName = "testuser_3";
	public static final String Password = "Test@123";
	
	public static  String ieDriverPath = "";
	public static  String chromeDriverPath = "";
	public static  String ffDriverPath = "";
	public static String resultFilePath="";

    //Output
	public static String screenShotDir="ScreenShots";


}
