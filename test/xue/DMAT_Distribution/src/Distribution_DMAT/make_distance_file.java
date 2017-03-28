package Distribution_DMAT;

import java.io.*;
import java.util.HashMap;

/**
 * Created by jiao on 2017/03/07.
 * 本来なら、病院と病院間の距離は地図上の距離になりますが、一応緯度経度で全ての病院と病院間の距離を計算し、記録用テストファイルを作ります
 */
public class make_distance_file {
    private static File m_i_master; //病院情報が入ってるマスターファイル
    public static HashMap<String, Double> ecode_lat = new HashMap<String, Double>();//拠点病院のeコードはキー、緯度は値
    public static HashMap<String, Double> ecode_lon = new HashMap<String, Double>();//拠点病院のeコードはキー、経度は値

    public static void main(String arg[]) throws Exception {
        m_i_master = new File("/Users/jiao/IdeaProjects/private/files_full/master/h-crisis_emis_medical_institute_master.csv");
        File outFile = new File("/Users/jiao/IdeaProjects/private/files_full/out/hospital_distance_info.csv"); // 出力ファイル
        PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"));//結合した結果を新しいファイル'out'に保存する
        try {
            BufferedReader input1 = new BufferedReader(new InputStreamReader(new FileInputStream(m_i_master), "Shift_JIS"));

            String line = "";
            line = input1.readLine();
            while ((line = input1.readLine()) != null) {
                String[] s = line.split(",");
                ecode_lat.put(s[1] + s[4], Double.parseDouble(s[11])); //緯度
                ecode_lon.put(s[1] + s[4], Double.parseDouble(s[12])); //経度

            }
            output.write("hospital_list,hospital,distance");
            for (String key : ecode_lat.keySet()) {
                for (String key1 : ecode_lon.keySet()) {
                    if(!key.equals(key1)) {
                        double xOrig = ecode_lat.get(key);
                        double yOrig = ecode_lon.get(key);
                        double xDest = ecode_lat.get(key1);
                        double yDest = ecode_lon.get(key1);
                        output.write("\n"+key+","+ key1 +","+CalculateEuclideanDistance(xOrig,
                        yOrig, xDest, yDest)) ;
                    }
                }
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private static double CalculateEuclideanDistance(double xOrig,
                                                     double yOrig, double xDest, double yDest) {
        // 直線距離を計算する
        double distance = Math.sqrt((xDest - xOrig) * (xDest - xOrig)
                + (yDest - yOrig) * (yDest - yOrig));
        return distance;

    }

}
