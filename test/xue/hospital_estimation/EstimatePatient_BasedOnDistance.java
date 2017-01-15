/**
 * 災害発生した際に、各外科病院、整形外科形成外科にいる患者数を集計するプログラム
 * Created by jiao on 2017/01/13.
 * 各５次メッシュから一番近い５つの病院及び距離情報を記録したファイルを"nearest_hospital_file.csv"に入ってます。ファイル名とルートは後で要チェック
 */
public class EstimatePatient_BasedOnDistance {


    private static File m_i_master; //病院情報が入ってます 必ずfullです！外科かどうかの情報が入ってる
    private static File siFile;// 震度分布ファイル
    private static File simple_mesh_base_damage;
    private static File nearHospitalFile;//各５次メッシュから一番近い５つの病院及び距離情報を記録したファイル

    /**
     * @param masterDir マスターファイルのフォルダ
     * @param outDir outputファイルのフォルダ
     * @param code
     */

    EstimatePatient_BasedOnDistance(File masterDir, File outDir, String code) throws Exception {
        m_i_master = new File(masterDir.getPath() + "/h-crisis_emis_medical_institute_master_full_04.csv");
        siFile= new File(outDir.getPath() + "/" + code + "_si.csv");
        simple_mesh_base_damage= new File(outDir.getPath() + "/" + code + "_mesh_base_03_damage_simple.csv");
        nearHospitalFile = new File(masterDir.getPath() + "/nearest_hospital_file.csv");/////ファイル名は要チェック！！！！

        File outFile = new File(outDir.getPath() + "/" + code + "_hospital_01_eva_patient.csv"); // 医療機関ごと患者数の推計

        if(!outFile.exists()) {
            outFile.createNewFile();
        }

        Estimate_patient(this.simple_mesh_base_damage, this.nearHospitalFile, this.m_i_master, this.siFile, outFile);
    }
    /**
     * @param inFile1: mesh_base_damage_simple
     * @param inFile2: nearHospitalFile
     * @param inFile3: h-crisis_emis_medical_institute_master_fullファイル
     * @param inFile4: 震度分布ファイル
     * @param outFile:　出力ファイル　病院ごと患者人数の推計ファイル
     */

   // public static void main(String args[]) throws Exception {
public static void Estimate_patient(File inFile1, File inFile2, File inFile3, File inFile4, File outFile){

        try {
           // File m_i_master = new File("/Users/jiao/IdeaProjects/2/assistant/files/master/h-crisis_emis_medical_institute_master_full_04.csv");

           // File siFile = new File("/Users/jiao/IdeaProjects/2/assistant/files/out/tests/tests_si.csv");
          //  File simple_mesh_base_damage = new File("/Users/jiao/IdeaProjects/2/assistant/files/out/tests/tests_mesh_base_03_damage_simple.csv");
           // File nearest_hospital_file = new File("/Users/jiao/IdeaProjects/2/assistant/files/master/test_nearest_hospital_file.csv");/////ファイル名はチェック
            //File file = new File("/Users/jiao/IdeaProjects/2/assistant/files/out/tests/tests_hospital_01_eva_patient.csv");


            BufferedReader brIn3 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile3), "Shift_JIS"));
            BufferedReader brIn4 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile4), "Shift_JIS"));
            BufferedReader brIn1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
            BufferedReader brIn2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"));

            //PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "Shift_JIS"));

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

            System.out.print("震度DBの作成");

            // 5次メッシュ負傷者数DBの作成
            // 5次メッシュ重症者数DBの作成
            //負傷者または重傷者がいる5次メッシュ damage_5mesh
            ArrayList<String> damage_5mesh = new ArrayList<String>();
            String line1 = brIn1.readLine(); // 1行目は見出し
            HashMap<String, Double> mesh_injuredDB = new HashMap<>(); // mesh5thがキー、負傷者数が値
            HashMap<String, Double> mesh_severeDB = new HashMap<>(); // mesh5thがキー、重傷者数が値
            while ((line1 = brIn1.readLine()) != null) {
                String pair[] = line1.split(",");
                mesh_injuredDB.put(pair[0], Double.parseDouble(pair[5]));
                mesh_severeDB.put(pair[0], Double.parseDouble(pair[5]));
                if((Double.parseDouble(pair[5]))>0|| (Double.parseDouble(pair[6]))>0){
                    damage_5mesh.add(pair[0]);
                }
            }

            System.out.print("負傷者数DBの作成");

            //外科病院リストSurgery_list
            //整形外科形成外科病院リストOrthopedics_list
            brIn3.mark((int)m_i_master.length());//現在位置(初期位置)をマークする

            line1 = brIn3.readLine(); //
            String pair1[] = line1.split(",");
            ArrayList<String> Surgery_list= new ArrayList<String>();
            ArrayList<String> Orthopedics_list= new ArrayList<String>();
            while((line1=brIn3.readLine())!=null) {
                String pair[] =line1.split(",");
                if((Double.parseDouble(pair[59]))==1){
                    Surgery_list.add(pair[1]);
                }
                if((Double.parseDouble(pair[60]))==1|| (Double.parseDouble(pair[61]))==1){
                    Orthopedics_list.add(pair[1]);
                }
            }

            brIn3.reset();//最後にマークした位置に戻る

            System.out.print("外科病院リストSurgery_list");

            // 5次メッシュと外科病院/整形外科形成外科病院DBの作成

            String line2 = brIn2.readLine(); // 1行目は見出し
            HashMap<String, HashMap<String, Double>> mesh_surgery_distanceDB = new HashMap<>(32); // mesh5thがキー、（外科病院コードがキー、距離の逆数が値）が値
            HashMap<String, HashMap<String, Double>> mesh_orthopedics_distanceDB = new HashMap<>(32); // mesh5thがキー、（整形外科形成外科病院コードがキー、距離の逆数が値）が値
            HashSet<String> col_surgery = new HashSet(); // 全ての外科病院をストックする
            HashSet<String> col_orthopedics = new HashSet(); // 全ての整形外科形成をストックする
            while ((line2 = brIn2.readLine()) != null) {
                String pair[] = line2.split(",");
                if ((siDB.containsKey(pair[0]))&& (!damage_5mesh.contains(pair[0]))) { //damage_5meshにある病院を外す
                    HashMap<String, Double> map;

                    if (mesh_surgery_distanceDB.containsKey(pair[0])) {
                        map = mesh_surgery_distanceDB.get(pair[0]);
                    }
                    else if (mesh_orthopedics_distanceDB.containsKey(pair[0])) {
                        map = mesh_orthopedics_distanceDB.get(pair[0]);
                    }
                    else{
                        map = new HashMap<>();
                    }

                    int i;
                    for(i=1;i<pair.length;i=i+2) {

                        if(Surgery_list.contains(pair[i])){
                            map.put(pair[i], 1 / Double.parseDouble(pair[i + 1]));
                            mesh_surgery_distanceDB.put(pair[0], map);
                            col_surgery.add(pair[i]);
                        }
                        if(Orthopedics_list.contains(pair[i])){
                            map.put(pair[i], 1 / Double.parseDouble(pair[i + 1]));
                            mesh_orthopedics_distanceDB.put(pair[0], map);
                            col_orthopedics.add(pair[i]);
                        }



                    }
                }
            }



            // 外科病院DBと整形形成外科DBの作成
            HashMap<String, Double> surgeryDB = new HashMap<>(); // 病院コードがキー、患者数が値
            HashMap<String, Double> orthopedicsDB = new HashMap<>(); // 病院コードがキー、患者数が値
            Iterator icol_surgery = col_surgery.iterator();
            Iterator icol_orthopedics = col_orthopedics.iterator();
            while (icol_surgery.hasNext()) {
                String s = (String) icol_surgery.next();
                surgeryDB.put(s, 0.0);
            }
            while (icol_orthopedics.hasNext()) {
                String s = (String) icol_orthopedics.next();
                orthopedicsDB.put(s, 0.0);
            }


            // 重傷者患者推計
            for (String key : mesh_surgery_distanceDB.keySet()) {
                HashMap<String, Double> map = mesh_surgery_distanceDB.get(key);
                double d = 0.0; // 距離の総和
                for (String s : map.keySet()) {
                    d = d + map.get(s);

                }

                for (String s : map.keySet()) {
                    double severe_patient_num = surgeryDB.get(s);
                    if (d > 0 && mesh_severeDB.containsKey(key)) {
                        severe_patient_num = severe_patient_num + mesh_severeDB.get(key) * (map.get(s) / d);
                        surgeryDB.put(s, severe_patient_num);
                        //System.out.print(s+ "\n");
                        //System.out.print(severe_patient_num+ "\n");
                    }
                }
            }

            // 負傷者患者推計
            for (String key : mesh_orthopedics_distanceDB.keySet()) {
                HashMap<String, Double> map = mesh_orthopedics_distanceDB.get(key);
                double d = 0.0; // 距離の総和
                for (String s : map.keySet()) {
                    d = d + map.get(s);

                }

                for (String s : map.keySet()) {
                    double injured_patient_num = orthopedicsDB.get(s);
                    if (d > 0 && mesh_injuredDB.containsKey(key)) {
                        injured_patient_num = injured_patient_num + mesh_injuredDB.get(key) * (map.get(s) / d);
                        surgeryDB.put(s, injured_patient_num);
                        // System.out.print(s+ "\n");
                        //System.out.print(injured_patient_num+ "\n");
                    }
                }
            }


//書き出す
            String line3 = brIn3.readLine(); //
            String pair4[] = line3.split(",");
            pw.write(pair4[1] + "," + pair4[2] + "," + pair4[6] + "," + pair4[8] + "," + pair4[10] + ",eva_injured,eva_severe");

            while((line3=brIn3.readLine())!=null) {
                String pair[] = line3.split(",");
                if(pair.length>74) {//病院は外科あるかどうかの情報を確実に入っているところ

                    if ((Surgery_list.contains(pair[1]))||(Orthopedics_list.contains(pair[1]))) {  //外科と整形外科ない病院を外す
                      //   if (((Surgery_list.contains(pair[1]))||(Orthopedics_list.contains(pair[1])))&& (!damage_5mesh.contains(pair[0]))) { //damage_5meshにある病院を外す //外科と整形外科ない病院を外す
                        pw.write("\n" + pair[1] + "," + pair[2] + "," + pair[6] + "," + pair[8] + "," + pair[10] + "," + orthopedicsDB.get(pair[1]) + "," + surgeryDB.get(pair[1]));
                    }

                }

            }// ファイル出力



            pw.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}




