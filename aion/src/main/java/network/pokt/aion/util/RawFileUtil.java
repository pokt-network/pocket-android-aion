package network.pokt.aion.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RawFileUtil {

    public static String readRawTextFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while (( line = buffReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}
