

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 災害発生した際に、病院ごとに患者数を集計する
 * Created by jiao on 2016/11/29.
 * 病院との距離情報ファイルを"near_hospital.csv"に入ってます。ファイル名とルートは後で要チェック
 */
public class EstimatePatient_BasedOnDistance {

    // private static File m_i_master; //病院情報が入ってます
    //private static File siFile;// 震度分布ファイル
    //private static File simple_mesh_base_damage;
    //  private static File nearHospitalFile;//病院との距離情報を入ってます


    public static void main(String args[]) throws Exception {
//public static void Estimate_patient(File inFile1, File inFile2, File inFile3, File inFile4, File outFile){

        try {
            File m_i_master = new File("/Users/jiao/IdeaProjects/2/assistant/files/master/h-crisis_emis_medical_institute_master.csv");

            File siFile = new File("/Users/jiao/IdeaProjects/2/assistant/files/out/tests/tests_si.csv");
            File simple_mesh_base_damage = new File("/Users/jiao/IdeaProjects/2/assistant/files/out/tests/tests_mesh_base_03_damage_simple.csv");
            File nearHospitalFile = new File("/Users/jiao/IdeaProjects/2/assistant/files/master/near_hospital.csv");/////ファイル名はチェック
            File file = new File("/Users/jiao/IdeaProjects/2/assistant/files/out/tests/tests_hospital_01_eva_patient.csv");


            BufferedReader brIn3 = new BufferedReader(new InputStreamReader(new FileInputStream(m_i_master), "Shift_JIS"));
            BufferedReader brIn4 = new BufferedReader(new InputStreamReader(new FileInputStream(siFile), "Shift_JIS"));
            BufferedReader brIn1 = new BufferedReader(new InputStreamReader(new FileInputStream(simple_mesh_base_damage), "Shift_JIS"));
            BufferedReader brIn2 = new BufferedReader(new InputStreamReader(new FileInputStream(nearHospitalFile), "Shift_JIS"));
            //PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "Shift_JIS"));

            // 震度DBの作成
            HashMap<String, Double> siDB = new HashMap<>(); // mesh5thがキー、震度が値
            String line4 = brIn4.readLine(); // 1行目は見出し
            while ((line4 = brIn4.readLine()) != null) {
                String pair[] = line4.split(",");
                if (pair[1].equals("nan")) {
                    siDB.put(pair[0], 0.0);
                } else {
                    siDB.put(pair[0], Double.parseDouble(pair[1]));
                }
            }

            //Set set = siDB.keySet();
            //for (Iterator itr = set.iterator(); itr.hasNext(); ) {
            //  String value = (String) itr.next();
            //double key;
            //key = (double) siDB.get(value);
            //System.out.println(value + "=" + key);
            //}

            // 5次メッシュ患者数DBの作成
            String line1 = brIn1.readLine(); // 1行目は見出し
            HashMap<String, Double> meshDB1 = new HashMap<>(); // mesh5thがキー、患者数が値
            while ((line1 = brIn1.readLine()) != null) {
                String pair[] = line1.split(",");
                meshDB1.put(pair[0], Double.parseDouble(pair[7]));
            }

            // Set set = meshDB1.keySet();
            // for (Iterator itr = set.iterator(); itr.hasNext(); ) {
            //   String value = (String) itr.next();
            // double key;
            //  key = (double) meshDB1.get(value);
            //System.out.println(value + "=" + key);
            //}
            // 5次メッシュと病院DBの作成
            String line2 = brIn2.readLine(); // 1行目は見出し
            HashMap<String, HashMap<String, Double>> meshDB2 = new HashMap<>(); // mesh5thがキー、（病院コードがキー、距離の逆数が値）が値
            HashSet<String> col = new HashSet(); // 全ての病院をストックする
            while ((line2 = brIn2.readLine()) != null) {
                String pair[] = line2.split(",");
                if (siDB.containsKey(pair[0])) {
                    HashMap<String, Double> map;
                    if (meshDB2.containsKey(pair[0])) {
                        map = meshDB2.get(pair[0]);
                    } else {
                        map = new HashMap<>();
                    }
                    map.put(pair[1], 1 / Double.parseDouble(pair[2]));
                    meshDB2.put(pair[0], map);
                    col.add(pair[1]);
                }
            }



            // 病院DBの作成
            HashMap<String, Double> hospitalDB = new HashMap<>(); // 病院コードがキー、患者数が値
            Iterator iCol = col.iterator();
            while (iCol.hasNext()) {
                String s = (String) iCol.next();
                hospitalDB.put(s, 0.0);
            }


            // 患者推計
            for (String key : meshDB2.keySet()) {
                HashMap<String, Double> map = meshDB2.get(key);
                double d = 0.0; // 距離の総和
                for (String s : map.keySet()) {
                    d = d + map.get(s);
                }

                for (String s : map.keySet()) {
                    double patinet_num = hospitalDB.get(s);
                    if (d > 0 && meshDB1.containsKey(key)) {
                        patinet_num = patinet_num + meshDB1.get(key) * (map.get(s) / d);
                        hospitalDB.put(s, patinet_num);
                    }
                }
            }



            String line3 = brIn3.readLine(); //
            String pair1[] = line3.split(",");
            pw.write(pair1[1] + "," + pair1[2] + "," + pair1[6] + "," + pair1[8] + "," + pair1[10] + ",si,eva_patient");

            while((line3=brIn3.readLine())!=null) {
                String pair[] = line3.split(",");
                if(pair.length==20) {
                    if(siDB.containsKey(pair[19])) {

                        pw.write("\n" + pair[1] + "," + pair[2] + "," + pair[6] + "," + pair[8] + "," + pair[10] + "," + siDB.get(pair[19]) + "," + hospitalDB.get(pair[1]));
                    }
                    else{

                        pw.write("\n" + pair[1] + "," + pair[2] + "," + pair[6] + "," + pair[8] + "," + pair[10] + "," + "0.0" + "," + hospitalDB.get(pair[1]));
                    }
                }

            }// ファイル出力

            pw.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}

}

