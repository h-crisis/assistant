package org.geotools.Shelter;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by jiao on 2016/12/19.
 */


public class refile {
    private static File inFile1;

    //Id小学校区内の施設(避難所を抽出する)
    public static void select(File inFile1, File out, String Id) {

        try {
        BufferedReader distance_info = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "SHIFT_JIS"));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, false), "SHIFT_JIS"));

        String line;
        line = distance_info.readLine();
            output.write(line);//表の第一行目コピー
        while ((line = distance_info.readLine()) != null) {
            String pair[] = line.split(",");
            if(Id.equals(pair[0])) {
                output.write("\n" + line);
            }

        }
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }




//ファイルの後ろに追加
    public static void add(File inFile1, File out) {
        try {
            BufferedReader distance_info = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "SHIFT_JIS"));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, true), "SHIFT_JIS"));

            String line;
            line = distance_info.readLine();

            while ((line = distance_info.readLine()) != null) {
                String pair[] = line.split(",");
                    output.write("\n" + line);

            }
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }


}
