package distance_calculation;


import java.io.*;

/**
 * Created by jiao on 2016/12/19.
 * 避難所の近くの病院３つを選びます
 */

public class MAIN {
    public static void main(String args[]) throws Exception {
        File Mesh2nd = new File("/Users/jiao/Desktop/shortestpath_test/Mesh2nd_test.csv");//2次メッシュコードファイル
        File srcfilepath = new File("/Users/jiao/Desktop/split_map/全道路リンク.shp");//全道路リンク
        File shelter = new File("/Users/jiao/Desktop/shortestpath_test/shelter_lat_lon_mesh.csv");//避難所（5thメッシュ、または病院などでもいい )の緯度経度とlocationの2次メッシュ　　　
        File hospital = new File("/Users/jiao/Desktop/shortestpath_test/hospital_lat_lon_mesh.csv");//病院（5thメッシュ、または病院などでもいい )の緯度経度とlocationの2次メッシュ
        File out = new File("/Users/jiao/Desktop/shortestpath_test/output.csv");//結果を記録します
        //if(!out.exists()) {
          //  out.createNewFile();
        //}
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, false), "SHIFT_JIS"));
        pw.write("scode"+","+"ecode"+","+"distance");
        pw.close();

        BufferedReader distance_info = new BufferedReader(new InputStreamReader(new FileInputStream(Mesh2nd), "SHIFT_JIS"));
        String value;
        String line;
        line = distance_info.readLine();//第一行を飛ばす
       /* while ((line = distance_info.readLine()) != null) {
            String pair[] = line.split(",");
            value=pair[0];
            File destfilepath= new File("/Users/jiao/Desktop/split_map/test.shp");//全道路の2次メッシュコードによる分割
            File shelter_temp = new File("/Users/jiao/Desktop/shortestpath_test/shelter_lat_lon_mesh_temp.csv");
            File hospital_temp = new File("/Users/jiao/Desktop/shortestpath_test/hospital_lat_lon_mesh_temp.csv");
            File out_temp = new File("/Users/jiao/Desktop/shortestpath_test/output_temp.csv");
            File out_temp2 = new File("/Users/jiao/Desktop/shortestpath_test/output_temp2.csv");

            new transShape(srcfilepath, destfilepath,value);//分割
            refile.select(shelter, shelter_temp, value);//2次メッシュコード内の避難所を抽出する
            refile.select(hospital, hospital_temp, value);//2次メッシュコード内の病院を抽出する
            new find_nearest_top3(destfilepath, shelter_temp,hospital_temp,out_temp);//避難所の近くの３つの病院を探す
            find_nearest_top3.find_top3(out_temp,out_temp2);
            refile.add(out_temp2,out);//outファイルを書き出す

        }
        */
       // ="523644";

        ////find_nearest_top3.build(destfilepath); // build graph
        ////find_nearest_top3.find_nearest(shelter,hospital,out);
        //File distance = new File("/Users/jiao/Desktop/shortestpath_test/output.csv");
       // find_nearest_top3.find_top3(distance,out);

    }
}
