package com.excel.tvchannelsbackup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Functions {
	final static String website           = "http://silentcoders.net/tv/";	
	//final static String website             = "http://192.168.2.3/excel_services/";
	final static String POST_REQUEST_URL    = website + "post_request.php";
	final static String CHECK_DATA_URL      = website + "check_data.php";
	final static String ASK_DATA_URL        = website + "ask_data.php";
	final static String FILE_UPLOAD_URL     = website + "file_upload.php";
	final static String FILE_DOWNLOAD_URL   = website + "file_download.php";
	final static String ZIP_FILE_NAME       = "tv_channels.zip";
	final static String RESTORE_DIR_NAME    = "restore";
	final static String RESTORE_DIR_PATH    = Environment.getExternalStorageDirectory() + File.separator + Functions.RESTORE_DIR_NAME;
	final static String RESTORED_ZIP_PATH   = RESTORE_DIR_PATH + File.separator + ZIP_FILE_NAME;
	
	final static String BACKUP_ZIP_PATH     = Environment.getExternalStorageDirectory() + File.separator + ZIP_FILE_NAME;
	//final static String BACKUP_DIR        = Environment.getDataDirectory() + "/backup1";
	final static String BACKUP_DIR_NAME     = "backup";
	final static String BACKUP_DIR          = Environment.getExternalStorageDirectory() + File.separator + BACKUP_DIR_NAME;

	final static String ZIP_MD5				= "zip_md5";
	

	
	// ----- Making GET/POST Request Starts Here
	public static String makeRequestForData( String url, String request_method, String urlParameters ){
		StringBuffer response = null;
		try{
			URL obj = null;
			HttpURLConnection con = null;
			
			if(request_method.equals("GET")){
				String encodedURL = url + "?" + urlParameters;
				//encodedURL = encodedURL.replaceAll("+", "%20");
				
				obj = new URL(encodedURL);
				con = (HttpURLConnection) obj.openConnection();
				//con.setRequestProperty("User-Agent", USER_AGENT);			
				con.setRequestMethod("GET");
				con.setDoOutput(true);					
			}
			else{
				String encodedURL = url;//URLEncoder.encode(url, "UTF-8");
				//urlParameters     = URLEncoder.encode(urlParameters, "UTF-8");
				//urlParameters     = urlParameters.replaceAll("+", "%20");
				
				obj = new URL(encodedURL);
				con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
			}
			
			int responseCode = con.getResponseCode();
			
			if(responseCode == 200){
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
		 
				//print result
				System.out.println(response.toString());
				
			}
			else{
				throw new Exception("No Response from server.");
			}
			}
			catch(Exception e){
				Log.i(null,"Exception : " + e.toString());
				return null;
			}
		Log.i("funcs", response.toString());
		return response.toString();
		
	}
	// ----- Making GET/POST Request Ends Here
	
	public static boolean checkIfBackupDone(){
		File f = new File( BACKUP_ZIP_PATH );
		Log.i(null, f.getAbsolutePath());
		if( f.exists() ){
			return true;
		}
		else
			return false;
	}
	
	// ----- Creating SharedPreference Starts Here
	public static SharedPreferences createSharedPreference( Context ct, String name ){
		SharedPreferences spfs = ct.getSharedPreferences( name, Context.MODE_PRIVATE );
		return spfs;
	}
	// ----- Creating SharedPreference Ends Here
	
	// ----- Editing SharedPreference Starts Here
	public static void editSharedPreference( SharedPreferences spfs, String name, String key, String value ){
		SharedPreferences.Editor spe = spfs.edit();
		spe.putString( key, value );
		spe.commit();
	}
	// ----- Editing SharedPreference Ends Here
	
	// Common Toast and Log recorder function
	public static void logAndToast( Context context, String TAG, String message ){
		Log.i( TAG, message );
		//Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
	}
	
	public  static String uploadFile(){
		String Tag = "UPLOADER";
	    String urlString = "http://excel.com.sg/appstv/webservice.php?what_do_you_want=upload_tv_channels";
	    HttpURLConnection conn = null;
	    
	    String exsistingFileName = "/sdcard/tv_channels.zip";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            // ------------------ CLIENT REQUEST

            Log.e(Tag, "Inside second Method");

            FileInputStream fileInputStream = new FileInputStream(new File(
                    exsistingFileName));

            // open a URL connection to the Servlet

            URL url = new URL(urlString);

            // Open a HTTP connection to the URL

            conn = (HttpURLConnection) url.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
           // conn.setRequestProperty("uploaded_file", fileName ); 
            
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos
                    .writeBytes("Content-Disposition: post-data; name=\"uploadedfile\";filename="
                            + exsistingFileName + "" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag, "Headers are written");

            // create a buffer of maximum size

            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1000;
            // int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bytesAvailable];

            // read file and write it into form...

            int bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bytesAvailable);
                bytesAvailable = fileInputStream.available();
                bytesAvailable = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
            }

            // send multipart form data necesssary after file data...

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e(Tag, "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            Log.e(Tag, "error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe) {
            Log.e(Tag, "error: " + ioe.getMessage(), ioe);
        }
        String up = "";
        
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                //Log.e("Dialoge Box", "Message: " + line);
            	up += line;
            }
            rd.close();
        } catch (IOException ioex) {
            Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
        }
        return up;
	}
	
	public static String uploadFile( String file_path, String URL ){
		String serverResponseMessage = "";
		
		try{
			HttpURLConnection connection = null;
			DataOutputStream outputStream = null;
			DataInputStream inputStream = null;

			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";

			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			try {
				Log.w("----in try---", " ");
				
				FileInputStream fileInputStream = new FileInputStream(new File(
						file_path));

				URL url = new URL(URL);
				connection = (HttpURLConnection) url.openConnection();

				// Allow Inputs & Outputs
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);

				// Enable POST method
				connection.setRequestMethod("POST");

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				outputStream = new DataOutputStream(connection.getOutputStream());
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream
						.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
								+ file_path + "\"" + lineEnd);
				outputStream.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// Read file
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) 
				{
					Log.w(null, " ");
					
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens
						+ lineEnd);

				// Responses from the server (code and message)
				int serverResponseCode = connection.getResponseCode();
				serverResponseMessage = connection.getResponseMessage();

				Log.w("serverResponseMessage", serverResponseMessage);
				
				fileInputStream.close();
				outputStream.flush();
				outputStream.close();
			}
			catch (Exception ex) 
			{
				Log.w(null, " ");
				ex.printStackTrace();
				// Exception handling
			}
			}
			catch (Exception ex) 
			{
				Log.w(null, " ");
				ex.printStackTrace();
				// Exception handling
			}
		return serverResponseMessage;
	}
	
	// create file
	public static File getFile( String dir_name, String file_name ){
		File dir = Environment.getExternalStoragePublicDirectory( dir_name );
		if( ! dir.exists() )
			dir.mkdirs();

		File file = new File( dir.getAbsolutePath() + File.separator + file_name );
		try{
			if( ! file.exists() )
				file.createNewFile();
		}
		catch( Exception e ){
			e.printStackTrace();
		}

		return file;
	}
	
	public static String executeShellCommandWithOp(String...strings) {
        String res = "exception occurred ";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try{
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }
    
    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }
    
    public static String getCMSIpFromTextFile(){
    	String ip = "";
		try{
			FileInputStream fis = new FileInputStream( Functions.getFile( "OTS", "ip.txt" ) );
			int ch;
			ip = "";
			while( ( ch = fis.read() ) != -1 ){
				ip += (char)ch;
			}
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return ip;
    }
}
