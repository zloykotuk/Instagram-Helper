package com.example.instagramhelper;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadWriteFile {

    public static void write(Context ctx, String filename, String text){
        FileOutputStream outputStream;
        try {
            outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read(Context ctx, String filename){
        StringBuffer fileContent = new StringBuffer("");

        try {
            FileInputStream fis;
            fis = ctx.openFileInput(filename);


            byte[] buffer = new byte[1024];
            int n;

            while ((n = fis.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }

            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

}
