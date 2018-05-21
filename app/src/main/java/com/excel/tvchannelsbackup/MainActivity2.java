package com.excel.tvchannelsbackup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilSharedPreferences;
import com.excel.excelclasslibrary.UtilURL;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    String uploadedFileName = "";
    RootTools rt;
    ProgressDialog p;
    Button backup, zip, restore, upload, exit;
    TextView last_backup;
    EditText et_command, et_ip;
    Context context = this;
    SharedPreferences spfs;

    static String BACKUP_APP_NAME = "hdtv";
    static String SOURCE_BACKUP_DIR   = "/data/" + BACKUP_APP_NAME;

    static final String TAG = "TVChannelsBackup";

    String[] permissions = {
            //Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.WRITE_SETTINGS
    };

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        spfs = (SharedPreferences) UtilSharedPreferences.createSharedPreference( this, Constants.PERMISSION_SPFS );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if ( checkPermissions() ) {
                // permissions  granted.
                init();
                Log.d( TAG, "All views initialized()" );
            }
            else{
                Log.d( TAG, "views not initialized()" );
            }
        }
        else{
            init();
        }


    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults ) {
        switch ( requestCode ) {
            case 10:
            {
                if( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED ){
                    // permissions granted.
                    Log.d( TAG, grantResults.length + " Permissions granted : " );
                    UtilSharedPreferences.editSharedPreference( spfs, Constants.IS_PERMISSION_GRANTED, Constants.PERMISSION_GRANTED_YES );
                } else {
                    String permission = "";
                    for ( String per : permissions ) {
                        permission += "\n" + per;
                    }
                    // permissions list of don't granted permission
                    Log.d( TAG, "Permissions not granted : "+permission );
                    UtilSharedPreferences.editSharedPreference( spfs, Constants.IS_PERMISSION_GRANTED, Constants.PERMISSION_GRANTED_NO );
                }
                return;
            }
        }
    }

    public  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for ( String p:permissions ) {
            result = ContextCompat.checkSelfPermission( this, p );
            if ( result != PackageManager.PERMISSION_GRANTED ) {
                listPermissionsNeeded.add( p );
            }
        }
        if ( !listPermissionsNeeded.isEmpty() ) {
            ActivityCompat.requestPermissions( this, listPermissionsNeeded.toArray( new String[ listPermissionsNeeded.size() ] ), 10 );
            return false;
        }
        return true;
    }

    private void init(){

        if( Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT ) {
            BACKUP_APP_NAME = "data/com.amlogic.tvservice";
            SOURCE_BACKUP_DIR   = "/data/" + BACKUP_APP_NAME;
        }


        rt = new RootTools();
        backup  = (Button) findViewById(R.id.backup_button);
        zip     = (Button) findViewById(R.id.zip_button);
        restore = (Button) findViewById(R.id.restore_button);
        upload  = (Button) findViewById(R.id.upload_button);
        exit    = (Button) findViewById(R.id.exit_button);
        last_backup  = (TextView) findViewById(R.id.last_backup);
        et_command = (EditText) findViewById( R.id.et_command );
        et_ip = (EditText) findViewById( R.id.et_ip );

        // disable the create zip button if backup directory doesnt exist
        File f = new File( Functions.BACKUP_DIR );
        //Toast.makeText( MainActivity.this, Functions.BACKUP_DIR, Toast.LENGTH_LONG).show();
        if( !f.exists() ){
            disable( zip );
            disable( restore );
            disable( upload );
        }
        else if( f.exists() ){
            Date d = new Date( f.lastModified() );
            last_backup.setText( d.toString() );
        }
        File f1 = new File( Functions.BACKUP_ZIP_PATH );
        if( !f1.exists() ){
            disable( restore );
            disable( upload );
        }
    }

    public void enable( Button button ){
        button.setEnabled( true );
    }

    public void disable( Button button  ){
        button.setEnabled( false );
    }

    public void uploadIt(View v){
        Async upload_async = new Async(){
            @Override
            public void onPreExecute() {
                super.onPreExecute();
                p = ProgressDialog.show(context, "Uploading", "Please wait...", true/* Indefinite --> true */, false /* Cancellable --> true */);
            }

            @Override
            public Object doInBackground(Object... params) {
                try {
                    String ip = UtilURL.getWebserviceURL( et_ip.getText().toString() );//"http://" + et_ip.getText().toString() + "/appstv/webservice.php?what_do_you_want=upload_tv_channels";
                    ip += "?what_do_you_want=upload_tv_channels";
                    //String ip = "http://" + et_ip.getText().toString() + "/appstv/webservice.php?what_do_you_want=upload_tv_channels";

                    Log.i( null, "URL : "+ip );

                    uploadedFileName = Functions.uploadFile( "/mnt/sdcard/tv_channels.zip", ip );

                    Log.i( null, "File : "+uploadedFileName );

                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                return uploadedFileName;
            }

            @Override
            public void onPostExecute(Object result) {
                super.onPostExecute(result);

                p.dismiss();
                Log.i( null, "File : "+result );
                if( (result != null) && (!result.equals("")) ){
                    Toast.makeText(context, "Backup has been successfully uploaded", Toast.LENGTH_LONG).show();
                    // enable( restore );
                    restore.setEnabled( true );
                }
                else{
                    Toast.makeText(context, "Invalid Server IP address or you are probably not connected to the Network", Toast.LENGTH_LONG).show();
                }

            }
        };
        upload_async.execute();
    }

    public void backup( View v ){
        Async create_backup = new Async(){

            ProgressDialog p;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                p = ProgressDialog.show(context, "Taking Backup", "Please wait...", true/* Indefinite --> true */, false /* Cancellable --> true */);
            }

            @Override
            protected Object doInBackground(Object... params) {
                try {
                    Process process = null;

                    File f = new File( Functions.BACKUP_DIR );
                    //File f = new File( Environment.getDataDirectory() + "/backup1" );
                    if( !f.exists() ){
                        f.mkdirs();
                    }

                    File temp = Functions.getFile( "OTS", "temp.sh" );
                    Functions.executeShellCommandWithOp( "mkdir /mnt/sdcard/backup",
                            "chmod -R 777 /data",
                            "chmod -R 777 "+SOURCE_BACKUP_DIR,
                            "cp -r "+SOURCE_BACKUP_DIR + " /mnt/sdcard/backup" );

                    // RootTools.copyFile( Functions.SOURCE_BACKUP_DIR + "", "/mnt/sdcard/backup", false, false );

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText( context, e.toString(), Toast.LENGTH_LONG).show();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                File f = new File( Functions.BACKUP_DIR );
                p.dismiss();
                try{


                    if( !f.exists() || !f.isDirectory() ){
                        Log.i(null, "Backup Not Done : "+f.getAbsolutePath());
                        Toast.makeText(context, "Backup Not Done", Toast.LENGTH_LONG).show();
                        disable( restore );
                        disable( zip );
                        disable( upload );
                    }
                    else{
                        try {
                            //Compress.zipFolder(Functions.BACKUP_DIR, Functions.BACKUP_ZIP_PATH);
                            Log.i(null, "Backup Done");
                            Toast.makeText(context, "Backup Done", Toast.LENGTH_LONG).show();
                            enable( zip );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch( Exception e ){
                    Toast.makeText(context, "exception hua", Toast.LENGTH_LONG).show();
                }

            }
        };
        create_backup.execute();
    }

    public void zipit( View v ){

        Async create_zip = new Async(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                p = ProgressDialog.show(context, "Creating Zip", "Please wait...", true/* Indefinite --> true */, false /* Cancellable --> true */);
            }

            @Override
            protected Object doInBackground(Object... params) {
                try {
                    Compress.zipFolder( Functions.BACKUP_DIR, Functions.BACKUP_ZIP_PATH );
                    enable( restore );
                    enable( upload );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                p.dismiss();
                if( new File(Functions.BACKUP_ZIP_PATH).exists() ){
                    Toast.makeText(context, "tv_channels.zip created successfully", Toast.LENGTH_LONG).show();
                    //enable( upload );
                    upload.setEnabled( true );
                }
                else
                    Toast.makeText(context, "tv_channels.zip not created", Toast.LENGTH_LONG).show();
            }

        };
        create_zip.execute();
    }

    public void exitit(View v){
        finish();
    }

    public void viewPaths( View v ){
        //Toast.makeText( MainActivity.this, "getExternalStoragePublicDirectory() : " +Functions.BACKUP_DIR, Toast.LENGTH_LONG).show();
        //Toast.makeText( MainActivity.this, "getExternalStorageDirectory() : "+Environment.getExternalStorageDirectory().getAbsolutePath(), Toast.LENGTH_LONG).show();
        //Toast.makeText( MainActivity.this, "getDataDirectory() : "+Environment.getDataDirectory().getAbsolutePath(), Toast.LENGTH_LONG).show();

    }

    public void execCommand( View v ){
        String cmd = et_command.getText().toString();
        //Toast.makeText( MainActivity.this, "Command : "+cmd, Toast.LENGTH_LONG).show();
        String[] cmds = cmd.split(" ");
        rt.copyFile( cmds[4], cmds[5], false, false );

        //for(int i=0;i<cmds.length;i++)
        //	Toast.makeText( MainActivity.this, i + cmds[i], Toast.LENGTH_SHORT).show();
        Process process = null;

        try {

            process = Runtime.getRuntime().exec("su");
            //Runtime.getRuntime().exec(new String[] {  "su", "-f", "mkdir "+Functions.BACKUP_DIR });
            // chmod the permission of com.whatsapp recursively to 777
            //Runtime.getRuntime().exec(new String[] {  "su", "-c", "-f", " chmod -R 777 "+Functions.SOURCE_BACKUP_DIR });
            //Runtime.getRuntime().exec(new String[] {  "su -f chmod -R 777 "+Functions.SOURCE_BACKUP_DIR });
            // copy the contents from com.whatsapp folder to backup directory, recursively
            //Runtime.getRuntime().exec(new String[] {  "su", "-f", "cp -r "+ Functions.SOURCE_BACKUP_DIR +"* "+ Functions.BACKUP_DIR });
            process = Runtime.getRuntime().exec( cmds );
            // Toast.makeText( MainActivity.this, process.toString() , Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText( MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

/*    public void startTheService( View v ){
    	Intent in = new Intent( this, RestoreService.class );
    	startService( in );
    }*/

}
