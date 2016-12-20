package distance_calculation;

import java.io.*;

/**
 * Created by jiao on 2016/12/19.
 */


public class refile {
    private static File inFile1;
    private static String mesh;

    //refile(File inFile1, File outFile, String mesh) throws Exception {
//    }
    //２次メッシュコードからメッシュ内の施設を抽出
    public static void select(File inFile1, File out, String mesh) {
        //    public static void main(String[] args) {
        //File shelter = new File("/Users/jiao/Desktop/shortestpath_test/shelter_lat_lon_mesh.csv");
          //      File shelter_temp = new File("/Users/jiao/Desktop/shortestpath_test/shelter_lat_lon_mesh_temp.csv");
            //    mesh="523644";
        try {
        BufferedReader distance_info = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "SHIFT_JIS"));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, false), "SHIFT_JIS"));

        String line;
        line = distance_info.readLine();
            output.write(line);//表の第一行目コピー
        while ((line = distance_info.readLine()) != null) {
            String pair[] = line.split(",");
            if(mesh.equals(pair[3])) {
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
