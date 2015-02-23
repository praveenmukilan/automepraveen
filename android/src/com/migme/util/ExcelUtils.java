package com.migme.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.google.common.io.Files;
import com.migme.util.Constants;
import com.migme.android.execution.AndyDriverScript;
    public class ExcelUtils {
                public static XSSFSheet ExcelWSheet;
                public static XSSFWorkbook ExcelWBook;
                public static org.apache.poi.ss.usermodel.Cell Cell;
                public static XSSFRow Row;
                //private static XSSFRow Row;
                
                public ExcelUtils() throws IOException{
                	
                	try{
                	
                 	
                	}
                	
                	catch(Exception e){
                		
                		System.out.println(e.getMessage());
                	}
                }
 
           
            public static void setExcelFile(String Path) throws Exception {
            	try {
//               	 FileInputStream ExcelFile = new FileInputStream(Constants.Path_TestData);
            	 
            		ExcelUtils.ExcelWBook = new XSSFWorkbook(ExcelUtils.copyFile(Constants.Path_TestData));   
            		
            		
                              
//                    System.out.println("ExcelWBook.getNumberOfSheets()"+ExcelUtils.ExcelWBook.getNumberOfSheets());
//                    System.out.println("bye");
            	} catch (Exception e){
            		Log.error("Class Utils | Method setExcelFile | Exception desc : "+e.getMessage());
            		AndyDriverScript.bResult = false;
                	}
            	}
            
            public static String getCellData(int RowNum, int ColNum, String SheetName ) throws Exception{
                try{
                	ExcelUtils.ExcelWSheet = ExcelUtils.ExcelWBook.getSheet(SheetName);
                   	Cell = ExcelUtils.ExcelWSheet.getRow(RowNum).getCell(ColNum);
                   	Cell.setCellType(1);
                    String CellData = Cell.getStringCellValue();
                    return CellData;
                 }catch (Exception e){
                     Log.error("Class Utils | Method getCellData | Exception desc : "+e.getMessage());
                     AndyDriverScript.bResult = false;
                     return"";
                     }
                 }
            
        	
        	public static int getRowCount(String SheetName){
        		int iNumber=0;
        		try {
        			ExcelUtils.ExcelWSheet = ExcelUtils.ExcelWBook.getSheet(SheetName);
//        			System.out.println("getRowCountPRaveen");
        			iNumber=ExcelUtils.ExcelWSheet.getLastRowNum()+1;
        			Log.error("rowNumber | "+iNumber);
        		} catch (Exception e){
        			Log.error("Class Utils | Method getRowCount | Exception desc : "+e.getMessage());
        			AndyDriverScript.bResult = false;
        			}
        		return iNumber;
        		}
        	
        	public static int getRowContains(String sTestCaseName, int colNum,String SheetName) throws Exception{
        		int iRowNum=0;	
        		try {
        		    //this.ExcelWSheet = this.ExcelWBook.getSheet(SheetName);
        			int rowCount = ExcelUtils.getRowCount(SheetName);
        			for (; iRowNum<rowCount; iRowNum++){
        				if  (ExcelUtils.getCellData(iRowNum,colNum,SheetName).equalsIgnoreCase(sTestCaseName)){
        					break;
        				}
        			}       			
        		} catch (Exception e){
        			Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
        			AndyDriverScript.bResult = false;
        			}
        		return iRowNum;
        		}
        	
        	public static int getTestStepsCount(String SheetName, String sTestCaseID, int iTestCaseStart) throws Exception{
        		try {
	        		for(int i=iTestCaseStart;i<=ExcelUtils.getRowCount(SheetName);i++){
	        			if(!sTestCaseID.equals(ExcelUtils.getCellData(i, Constants.Col_TestCaseID, SheetName))){
	        				int number = i;
	        				return number;      				
	        				}
	        			}
	        		ExcelUtils.ExcelWSheet = ExcelUtils.ExcelWBook.getSheet(SheetName);
	        		int number=ExcelUtils.ExcelWSheet.getLastRowNum()+1;
	        		return number;
        		} catch (Exception e){
        			Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
        			AndyDriverScript.bResult = false;
        			return 0;
                }
        	}
        	
        	@SuppressWarnings("static-access")
        	public static void setCellData(String Result,  int RowNum, int ColNum, String SheetName) throws Exception    {
                   try{
                	   
                	   ExcelUtils.ExcelWSheet = ExcelUtils.ExcelWBook.getSheet(SheetName);
                       Row  = ExcelUtils.ExcelWSheet.getRow(RowNum);
                       Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
                       if (Cell == null) {
                    	   Cell = Row.createCell(ColNum);
                    	   Cell.setCellValue(Result);
                        } else {
                            Cell.setCellValue(Result);
                        }
                         FileOutputStream fileOut = new FileOutputStream(Constants.resultFilePath);
                         ExcelUtils.ExcelWBook.write(fileOut);
                         //fileOut.flush();
                         fileOut.close();
                         ExcelUtils.ExcelWBook = new XSSFWorkbook(new FileInputStream(Constants.resultFilePath));
                     }catch(Exception e){
                    	 AndyDriverScript.bResult = false;
              
                     }
                }
        	
        	public static File copyFile(String srcFilePath){
        		String newFilePath = "src//com//migme//android//dataEngine//Result_"+System.currentTimeMillis()+".xlsx";
        		Constants.resultFilePath = newFilePath;
        		File newFile = new File(newFilePath);
        		try{
        		
        		File srcFile = new File(srcFilePath);
        		
        		
        		Files.copy(srcFile, newFile);
        		
        		}catch(Exception fne){
        			
        			
        			Log.info("Issues with the file :"+srcFilePath);
        		}
        		
        		return newFile;
        	}

    	}