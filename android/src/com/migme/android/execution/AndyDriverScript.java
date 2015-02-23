package com.migme.android.execution;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.migme.android.execution.AndroidActionKeywords;
import com.migme.util.Constants;
import com.migme.util.ExcelUtils;
import com.migme.util.Log;
 
public class AndyDriverScript {
	
	public static Properties OR;
	public static AndroidActionKeywords actionKeywords;
	public static String sActionKeyword;
	public static String sPageObject;
	public static Method method[];
		
	public static int iTestStep;
	public static int iTestLastStep;
	public static String sTestCaseID;
	public static String sTestStepName;
	public static String sRunMode;
	public static String sData;
	public static boolean bResult;
	public static XSSFWorkbook testSuite;

	
	public AndyDriverScript() throws NoSuchMethodException, SecurityException, IOException{
		actionKeywords = new AndroidActionKeywords();
		method = actionKeywords.getClass().getMethods();

	}
	
	@Test(groups = { "sampleTest" })
    public static void main() throws Exception {
    	
    	
    	AndyDriverScript drv = new AndyDriverScript();
 
		drv.loadConfigProperties();
		
//		System.out.println("Path_excel : C:\\testdata "+Constants.Path_TestData);
		
	
		ExcelUtils.setExcelFile(Constants.Path_TestData);
    	
    	
    	
//    	System.out.println("ExcelUtils.ExcelWBook.getNumberOfSheets() : " + ExcelUtils.ExcelWBook.getNumberOfSheets());
    	DOMConfigurator.configure("log4j.xml");
    	String Path_OR = Constants.Path_OR;
    	
//         System.out.println("Current dir using System:" +System.getProperty("user.dir"));
    
		FileInputStream fs = new FileInputStream(Path_OR);
		OR= new Properties(System.getProperties());
		OR.load(fs);
		
		AndyDriverScript startEngine = new AndyDriverScript();
		startEngine.execute_TestCase();
		
    }
		
    
	public  void loadConfigProperties() throws IOException{
		
		try{
		Properties confProp;

		String confPropName = "src//config//androidauto.properties";
		
		FileInputStream fs = new FileInputStream(confPropName);
		confProp= new Properties(System.getProperties());
		confProp.load(fs);
		
		Constants.Path_TestData = confProp.getProperty("Path_TestData");
		System.out.println("********Constants.Path_TestData : "+Constants.Path_TestData);
		Constants.Path_OR = confProp.getProperty("Path_OR");
		Constants.File_TestData = confProp.getProperty("File_TestData");
		Constants.KEYWORD_FAIL = confProp.getProperty("KEYWORD_FAIL");
		Constants.KEYWORD_PASS = confProp.getProperty("KEYWORD_PASS");
		Constants.screenShotDir = confProp.getProperty("screenShotDir");

		}
		catch(Exception e){
			
			System.out.println(e.getMessage());
			
		}
		
	}

	@Test(groups = { "sampleTest" })
    private void execute_TestCase() throws Exception {

	    	int iTotalTestCases = ExcelUtils.getRowCount(Constants.Sheet_TestCases);
	    	System.out.println("iTotalTestCases : "+iTotalTestCases);
			for(int iTestcase=1;iTestcase<iTotalTestCases;iTestcase++){
				bResult = true;
				sTestCaseID = ExcelUtils.getCellData(iTestcase, Constants.Col_TestCaseID, Constants.Sheet_TestCases); 
				
				sRunMode = ExcelUtils.getCellData(iTestcase, Constants.Col_RunMode,Constants.Sheet_TestCases);
				if (sRunMode.equals("Yes")){
					Log.startTestCase(sTestCaseID);
					iTestStep = ExcelUtils.getRowContains(sTestCaseID, Constants.Col_TestCaseID, Constants.Sheet_TestSteps);
					iTestLastStep = ExcelUtils.getTestStepsCount(Constants.Sheet_TestSteps, sTestCaseID, iTestStep);
					bResult=true;
					for (;iTestStep<iTestLastStep;iTestStep++){
			    		sActionKeyword = ExcelUtils.getCellData(iTestStep, Constants.Col_ActionKeyword,Constants.Sheet_TestSteps);
			    		sPageObject = ExcelUtils.getCellData(iTestStep, Constants.Col_PageObject, Constants.Sheet_TestSteps);
			    		sData = ExcelUtils.getCellData(iTestStep, Constants.Col_DataSet, Constants.Sheet_TestSteps);
			    		sTestStepName = ExcelUtils.getCellData(iTestStep, Constants.Col_TestScenarioID, Constants.Sheet_TestSteps);
			    		System.out.println("sTestStepName : "+sTestStepName);
			    		execute_Actions();
						if(bResult==false){
							ExcelUtils.setCellData(Constants.KEYWORD_FAIL,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);
							Log.endTestCase(sTestCaseID);
							break;
							}						
						}
					if(bResult==true){
					ExcelUtils.setCellData(Constants.KEYWORD_PASS,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);
					Log.endTestCase(sTestCaseID);	
						}					
					}
				}
    		}	
     
    
     private  void execute_Actions() throws Exception {
	
		for(int i=0;i<method.length;i++){
			
			if(method[i].getName().equals(sActionKeyword)){
				method[i].invoke(actionKeywords,sPageObject, sData);
				if(bResult==true){
					ExcelUtils.setCellData(Constants.KEYWORD_PASS, iTestStep, Constants.Col_TestStepResult, Constants.Sheet_TestSteps);
					break;
				}else{
					ExcelUtils.setCellData(Constants.KEYWORD_FAIL, iTestStep, Constants.Col_TestStepResult, Constants.Sheet_TestSteps);
//					ActionKeywords.closeBrowser("","");
					break;
					}
				}
			}
     }
     
}