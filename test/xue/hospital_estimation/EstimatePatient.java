
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 災害発生した際に、病院ごとに患者数を集計する
 * Created by jiao on 2016/11/29.
 * 病院との距離情報ファイルを"near_hospital.csv"に入ってます。ファイル名とルートは後で要チェック
 */
public class EstimatePatient {

    private static File m_i_master; //病院情報が入ってます
    private static File siFile;// 震度分布ファイル
    private static File simple_mesh_base_damage;
    private static File nearHospitalFile;//病院との距離情報を入ってます

    /**
     * @param masterDir マスターファイルのフォルダ
     * @param outDir outputファイルのフォルダ
     * @param code
     */

    EstimatePatient(File masterDir, File outDir, String code) throws Exception {
        m_i_master = new File(masterDir.getPath() + "/h-crisis_emis_medical_institute_master.csv");
        siFile= new File(outDir.getPath() + "/" + code + "_si.csv");
        simple_mesh_base_damage= new File(outDir.getPath() + "/" + code + "_mesh_base_03_damage_simple.csv");
        nearHospitalFile = new File(masterDir.getPath() + "/near_hospital.csv");/////ファイル名はチェック

        File outFile = new File(outDir.getPath() + "/" + code + "_hospital_01_eva_patient.csv"); // 医療機関ごと患者数の推計

        if(!outFile.exists()) {
            outFile.createNewFile();
        }

        Estimate_patient(this.simple_mesh_base_damage, this.nearHospitalFile, this.m_i_master, this.siFile, outFile);
    }
    /**
     * @param inFile1: mesh_base_damage_simple
     * @param inFile2: nearHospitalFile
     * @param inFile3: h-crisis_emis_medical_institute_masterファイル
     * @param inFile4: 震度分布ファイル
     * @param outFile:　出力ファイル　病院ごと患者人数の推計ファイル
     */

public static void Estimate_patient(File inFile1, File inFile2, File inFile3, File inFile4, File outFile){

    try(BufferedReader brIn1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
        BufferedReader brIn2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
        BufferedReader brIn3 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile3), "Shift_JIS"));
        BufferedReader brIn4 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile4), "Shift_JIS"));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"));

        // 震度DBの作成
        HashMap<String, Double> siDB = new HashMap<>(); // mesh5thがキー、震度が値
        String line4 = brIn4.readLine(); // 1行目は見出し
        while((line4 = brIn4.readLine())!=null) {
            String pair[] = line4.split(",");
            if(pair[1].equals("nan")) {
                siDB.put(pair[0], 0.0);
            }
            else {
                siDB.put(pair[0], Double.parseDouble(pair[1]));
            }
        }

        // 5次メッシュ患者数DBの作成
        String line1 = brIn1.readLine(); // 1行目は見出し
        HashMap<String, Double> meshDB1 = new HashMap<>(); // mesh5thがキー、患者数が値
        while((line1 = brIn1.readLine()) != null) {
            String pair[] = line1.split(",");
            meshDB1.put(pair[0], Double.parseDouble(pair[7]));
        }

        // 5次メッシュと病院DBの作成
        String line2 = brIn2.readLine(); // 1行目は見出し
        HashMap<String, HashMap<String, Double>> meshDB2 = new HashMap<>(); // mesh5thがキー、（病院コードがキー、距離の逆数が値）が値
        HashSet<String> col = new HashSet(); // 全ての病院をストックする
        while((line2=brIn2.readLine())!=null) {
            String pair[] = line2.split(",");
            if(siDB.containsKey(pair[0])) {
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
        while(iCol.hasNext()) {
            String s = (String) iCol.next();
            hospitalDB.put(s, 0.0);
        }

        // 患者推計
        for(String key : meshDB2.keySet()) {
            HashMap<String, Double> map = meshDB2.get(key);
            double d = 0.0; // 距離の総和
            for(String s : map.keySet()) {
                d = d + map.get(s);
            }

            for(String s : map.keySet()) {
                double patinet_num = hospitalDB.get(s);
                if(d > 0 && meshDB1.containsKey(key)) {
                    patinet_num = patinet_num + meshDB1.get(key) * (map.get(s) / d);
                    hospitalDB.put(s, patinet_num);
                }
            }
        }

        // ファイル出力
        String line3 = brIn3.readLine(); // 1行目は見出し
         boolean midashi = true;

     while(line3!=null) {
            String pair[] = line3.split(",");
            if(midashi) {
                pw.write(pair[1] + "," + pair[2] + "," + pair[6] + "," + pair[8] + "," + pair[10] + ",si,eva_patient");
                midashi = false;
            }
            else if(hospitalDB.containsKey(pair[2])) {

                if(siDB.get(pair[19])==null) {
                    pw.write("\n" + pair[1] + "," + pair[2] + "," + pair[6] + "," + pair[8] + "," + pair[10] + "," + "0.0" + "," + hospitalDB.get(pair[1]));
                }
                else {
                    pw.write("\n" + pair[1] + "," + pair[2] + "," + pair[6] + "," + pair[8] + "," + pair[10] + "," + siDB.get(pair[19]) + "," + hospitalDB.get(pair[1]));
                }
            }
            line3 = brIn3.readLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

}

}

