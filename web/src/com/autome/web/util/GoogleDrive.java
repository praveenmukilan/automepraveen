package com.autome.web.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


public class GoogleDrive {

  private static String CLIENT_ID = "1025454419977-1c36fqd19ljg3hcike1hp8cosm2dfj7b.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "ovowSgM_W1YtSDE5eM8yepU1";

  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  public static  Drive service;
  public static final String migautoFolderId = "0B5Oonp8LMi-0fmRfX2dQdm1ESzBJTXE2eGgxVW53RzM4cHFXY1Z5b2paXzBTRlU0Q3ZwYW8";
  public static WebDriver driverGD;
  
  public static void main(String args[]) throws InterruptedException{
//	  folderTitle : Screenshots | folderID : 0B0JDYkvkPzcRfllwSVZYd0Y5VEYtSmVLSU9hLVJOeVVLRjdzYlhKUGhpLVJ1UEJSTmQzdWs
//	  folderTitle : web | folderID : 0B0JDYkvkPzcRflVWRUM2U1FwcDJvVUNNZ21KNFJBYnBNLW9fdzREemhWUmY2N080dF9tMDQ
	  try {
			System.setProperty("webdriver.chrome.driver", "src//resources//chromedriver");
		  driverGD= new ChromeDriver();
		  
	getConnectionCode();
//	getConnection();
//	findScreenShotFolder();
	uploadImageFile("testfile.jpeg","0B0JDYkvkPzcRfllwSVZYd0Y5VEYtSmVLSU9hLVJOeVVLRjdzYlhKUGhpLVJ1UEJSTmQzdWs" );
	driverGD.quit();
//	
//	createFolder("newPraveenSoundService",migautoFolderId );
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public static void uploadScreenshot(String fileName){
	  
	  
  }
  
  public static void findScreenShotFolder(){
	  
	  Files.List request;
	try {
		request = service.files().list().setQ(
			       "mimeType='application/vnd.google-apps.folder' and trashed=false");
		FileList files = request.execute();
		List<File> fileList =  files.getItems();
		System.out.println("Find Screen Shot Folder");
		
		for(File folder : fileList){
			
			System.out.println("folderTitle : "+folder.getTitle()+" | folderID : "+folder.getId());

		}
		

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }
  
  public static String createFolder(String folderName, String parentFolderID){
	
	  try {
	  File body = new File();
	  body.setTitle(folderName);
	  body.setMimeType("application/vnd.google-apps.folder");
	  body.setParents(Arrays.asList(new ParentReference().setId(parentFolderID)));
	  


	  File file = service.files().insert(body).execute();
		return file.getId();

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
		
	}
		
  }
  
  public static void getNewConnection() throws IOException {
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    
////
////   
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
        .setAccessType("online")
        .setApprovalPrompt("auto").build();
    

    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    System.out.println("Please open the following URL in your browser then type the authorization code:");
    System.out.println("  " + url);


    
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String code = br.readLine();

    
    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
  
    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
    String accessToken = credential.getAccessToken();
    System.out.println("accessToken : "+accessToken);
    String refreshToken = credential.getRefreshToken();
    System.out.println("refreshToken : "+refreshToken);
//   
    
    /* 

    Please open the following URL in your browser then type the authorization code:
  https://accounts.google.com/o/oauth2/auth?access_type=online&approval_prompt=auto&client_id=1025454419977-1c36fqd19ljg3hcike1hp8cosm2dfj7b.apps.googleusercontent.com&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&scope=https://www.googleapis.com/auth/drive
4/Mc6c0laNLUNDoOWedqcKBdQeZTvFOpuk3eux7KbcnBw.krHGPt3ZDMkZcp7tdiljKKZxfDmzlwI
accessToken : ya29.KgGwauBnxojIspz3dJoyruXrivC4kyDGj6oOObv8IIqTXW4me-zPl6D2O-wN1l63fdFN_YciJR5vtg
refreshToken : null
Mar 02, 2015 4:31:57 PM com.google.api.client.googleapis.services.AbstractGoogleClient <init>
WARNING: Application name is not set. Call Builder#setApplicationName.
File ID: 0B5Oonp8LMi-0eFVPYWRhV2xGdzA

    */


//
//     GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(jsonFactory)
//    		 .setTransport(httpTransport).setClientSecrets(CLIENT_ID, CLIENT_SECRET).build();
//     credential.setAccessToken("ya29.KgEkzUtVVZn4jYqnceKRCRWEGyzLTgFUh1u6s7EmaPJLxoRnO0MDFm-Y93_Yt1eNiQ_7C0-YzglFUQ");
//     credential.setRefreshToken("");



//Drive service = new Drive.Builder(httpTransport, jsonFactory, credential1).build(); 

    //4/Mc6c0laNLUNDoOWedqcKBdQeZTvFOpuk3eux7KbcnBw.krHGPt3ZDMkZcp7tdiljKKZxfDmzlwI
    
    //Create a new authorized API client
     service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
    

     
     driverGD.quit();
 
//     http://stackoverflow.com/questions/18150073/how-to-create-folders-in-google-drive-without-duplicate
/*
    //Insert a file  
    File body = new File();
    body.setTitle("My document");
    body.setDescription("A test document");
    body.setMimeType("text/plain");
    
    java.io.File fileContent = new java.io.File("weblog.log");
    FileContent mediaContent = new FileContent("text/plain", fileContent);
//4/OgdJ9bAOcTzouCdxLkCr9cPelAjtV4Coi7cmSaogewQ.Ihsc984K8G0Rcp7tdiljKKbxwuyylwI
//    4/U_SoaXECL5DXhqgXdQm_9yFjfc-KRV83pxJMWIzAWlM.Ijth0FelxG4Scp7tdiljKKbnkO-ylwI
    File file = service.files().insert(body, mediaContent).execute();
    System.out.println("File ID: " + file.getId());
    
    */
  }
  
  public static void uploadImageFile(String filePath, String parentFolderId){
		try {
	  File body = new File();
	  body.setMimeType("image/jpeg");
	  body.setParents(Arrays.asList(new ParentReference().setId(parentFolderId)));

	  
	  java.io.File fileContent = new java.io.File(filePath);
	    FileContent mediaContent = new FileContent("image/jpeg", fileContent);
	//4/OgdJ9bAOcTzouCdxLkCr9cPelAjtV4Coi7cmSaogewQ.Ihsc984K8G0Rcp7tdiljKKbxwuyylwI
//	    4/U_SoaXECL5DXhqgXdQm_9yFjfc-KRV83pxJMWIzAWlM.Ijth0FelxG4Scp7tdiljKKbnkO-ylwI
	    File file = service.files().insert(body, mediaContent).execute();
	    System.out.println("File ID: " + file.getId());
	   
	    System.out.println("getWebContentLink() :"+ file.getWebContentLink() +"| getAlternateLink : "+     file.getAlternateLink() + " | iconlink :"+file.getIconLink()+" | selfLink :"+file.getSelfLink());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	  
  }
  
  public static void uploadScreenshotsFolder(String localFolderPath, String parentFolderId){
	  
	  java.io.File dir = new java.io.File(localFolderPath);
	  
	  if(dir.isDirectory()){
		  String[] files = dir.list();
		  
		  for(String filePath : files){
			  uploadImageFile(filePath, parentFolderId);
		  }
	  }
  }
  
  public static String getCode(String url) {
	  
	  try{
	  
	  System.out.println("       getCode      ");
	  
	  driverGD.get(url);
	  driverGD.findElement(By.cssSelector("input#Email")).sendKeys("automatemig");
	  driverGD.findElement(By.cssSelector("input#Passwd")).sendKeys("Letmein01");
	  driverGD.findElement(By.cssSelector("input#signIn")).click();
	  
	  Thread.sleep(3000);

	  
	  driverGD.findElement(By.cssSelector("button#submit_approve_access")).click();
	  
	 String code = driverGD.findElement(By.cssSelector("input#code")).getAttribute("value");
	 
	 System.out.println("code : "+code);
	  return code;
	  }
	  catch(Exception e){
		  
		 e.printStackTrace();
		 return null;
	  }
  }
  
  public static void getConnectionCode() throws IOException, InterruptedException {
	    HttpTransport httpTransport = new NetHttpTransport();
	    JsonFactory jsonFactory = new JacksonFactory();
	    
	////
	////   
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
	        .setAccessType("online")
	        .setApprovalPrompt("auto").build();
	    

	    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
	    System.out.println("Please open the following URL in your browser then type the authorization code:");
	    System.out.println("  " + url);
	    
	    
	    
//	    driverGD.get(url);
//	    driverGD.findElement(By.cssSelector("button#submit_approve_access")).click();
//	    String code = driverGD.findElement(By.cssSelector("div#inner-editor")).getText();
//	    
//	    
//	    URL urlO = new URL(url);
//	    
//	    BufferedReader br = new BufferedReader(new InputStreamReader(urlO.openStream()));
//	    String tempstr="";
//	    while(null!=(tempstr=br.readLine())){
//	    	System.out.println("tempstr : "+tempstr);
//	    	
//	    }
	  
//	    
//	   String code="";
	    String code = getCode(url);
	    

//	    
	    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
	  
	    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
	    String accessToken = credential.getAccessToken();
	    System.out.println("accessToken : "+accessToken);
	    String refreshToken = credential.getRefreshToken();
	    System.out.println("refreshToken : "+refreshToken);
//	//   
//	    
//	 
	     service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
	    
	     Drive.Files.List lst = service.files().list();
//	     
  
  
  
}
  
}