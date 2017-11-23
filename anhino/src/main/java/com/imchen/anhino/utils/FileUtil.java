package com.imchen.anhino.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by imchen on 2017/11/22.
 */

public class FileUtil {

    public static String readFile(String filePath) {
        StringBuilder builder = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            } else {
                builder = new StringBuilder();
                String line = null;
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader ipsReader = new InputStreamReader(fis, "UTF-8");
                BufferedReader reader = new BufferedReader(ipsReader);
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
                reader.close();
                ipsReader.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static int writeFile(String filePath, String content) {

        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
            writer.write(content);
            writer.flush();
            writer.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
