package tomerbu.edu.gcmdemo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tomerbuzaglo on 23/03/2016.
 */
public class IOUtils {
    public static String toString(InputStream inputStream) throws Exception {

        String line = null;
        StringBuilder sb = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
