package com.example.userpreferences;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

public class AssetFileReader {
    final Context context;

    public AssetFileReader(Context context) {
        this.context = context;
    }

    public String readFile(String filename){
        byte[] buffer;
        InputStream inputStream;

        try {
            inputStream = context.getAssets().open(filename);
            int size = inputStream.available();
            buffer = new byte[size];

            if(inputStream.read(buffer) != -1)
                return new String(buffer);
            inputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
