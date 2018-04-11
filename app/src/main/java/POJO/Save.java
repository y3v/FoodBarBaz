package POJO;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Save {
    private Context context;
    private static final String  FOLDER_NAME = "/FoodBarBaz";
    private static final String  FILE_START = "FBB";

    public void SaveImage(Context context, Bitmap imageToSave){
        this.context = context;
        String file_path = Environment.getExternalStorageDirectory() + FOLDER_NAME;
        File dir = new File(file_path);
        if(!dir.exists()){
            dir.mkdirs();
            Log.d("FILE EXISTENCE ", "FUTILE");
        }else{
            Log.d("FILE EXISTENCE ", "COMPLETE!!!");
        }

        File file = new File(dir, FILE_START + getCurrentDateAndTime() + ".jpg");

        try{
            FileOutputStream fOut = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreated(file);
            AbleToSave();
        }catch (Exception e){
            Log.d("EXCEPTIONHABA", e.getMessage());
            UnableToSave();
        }
    }

    private void UnableToSave() {
        Toast.makeText(context, "Picture unable to save to Gallery", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(context, "Picture Saved to Gallery", Toast.LENGTH_SHORT).show();
    }

    private void MakeSureFileWasCreated(File file) {
        Log.d("EXCEPTION", "HABASHABA");
        MediaScannerConnection.scanFile(context,
                new String[] {file.toString()} , null,
                new MediaScannerConnection.OnScanCompletedListener(){
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        Log.e("EXTERNAL STORAGE", "SCANNED " + s + ":");
                        Log.e("EXTERNAL STORAGE", "-> URI = " + uri);
                    }
                }
        );

    }

    private String getCurrentDateAndTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-DD-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
