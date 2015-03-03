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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GoogleDrive {

  private static String CLIENT_ID = "1025454419977-1c36fqd19ljg3hcike1hp8cosm2dfj7b.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "ovowSgM_W1YtSDE5eM8yepU1";

  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  public static  Drive service;
  public static final String migautoFolderId = "0B5Oonp8LMi-0fmRfX2dQdm1ESzBJTXE2eGgxVW53RzM4cHFXY1Z5b2paXzBTRlU0Q3ZwYW8";
  
  public static void main(String args[]){
	  //folderTitle : migauto | folderID : 0B5Oonp8LMi-0fmRfX2dQdm1ESzBJTXE2eGgxVW53RzM4cHFXY1Z5b2paXzBTRlU0Q3ZwYW8
//	  folderTitle : Screenshots | folderID : 0B5Oonp8LMi-0fm9abFF4VjMzb0U0T0JDZDBrQlBQOHlaYlU3VEZNeDNxei1oRWJuZXVHekE
	  try {
	getConnection();
//	findScreenShotFolder();
	
	createFolder("newPraveenSoundService",migautoFolderId );
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
  
  public static void getConnection() throws IOException {
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    
//
//   
//    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
//        .setAccessType("online")
//        .setApprovalPrompt("auto").build();
//    
//
//    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
//    System.out.println("Please open the following URL in your browser then type the authorization code:");
//    System.out.println("  " + url);
//    
//    
//    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    String code = br.readLine();
//
//    
//    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
//  
//    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
//    String accessToken = credential.getAccessToken();
//    System.out.println("accessToken : "+accessToken);
//    String refreshToken = credential.getRefreshToken();
//    System.out.println("refreshToken : "+refreshToken);
   
    
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



     GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(jsonFactory)
    		 .setTransport(httpTransport).setClientSecrets(CLIENT_ID, CLIENT_SECRET).build();
     credential.setAccessToken("ya29.KgEkzUtVVZn4jYqnceKRCRWEGyzLTgFUh1u6s7EmaPJLxoRnO0MDFm-Y93_Yt1eNiQ_7C0-YzglFUQ");
     credential.setRefreshToken("");



//Drive service = new Drive.Builder(httpTransport, jsonFactory, credential1).build(); 

    //4/Mc6c0laNLUNDoOWedqcKBdQeZTvFOpuk3eux7KbcnBw.krHGPt3ZDMkZcp7tdiljKKZxfDmzlwI
    
    //Create a new authorized API client
     service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
    
     Drive.Files.List lst = service.files().list();
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
  
  
  
}