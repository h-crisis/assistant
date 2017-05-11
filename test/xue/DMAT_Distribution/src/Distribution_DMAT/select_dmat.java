package Distribution_DMAT;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jiao on 2017/03/07.
 */
public class select_dmat {
    private static File m_i_master; //病院情報が入ってるマスターファイル
    private static File siFile;// 震度分布ファイル
    private static File hospital_distance_info; // 病院と病院の距離ファイル
    public static double dmat_level=3;//とりあえず、３と想定する
    public static ArrayList kyoten=new ArrayList();//災害拠点病院のリスト
    public static ArrayList dmat=new ArrayList();//搬送可能な病院のリスト
    public static HashMap<String, Double> kyoten_level = new HashMap<String, Double>();//病院のeコードはキー、dmatレベルは値
    public static HashMap<String, Double> Dmat_level = new HashMap<String, Double>();//病院のeコードはキー、dmatレベルは値
    public static HashMap<HashMap<String, String>, Double> kyoten_Dmat_distance= new HashMap<HashMap<String, String>, Double>(); //hospital dmatのeコードはキー、距離は値





    select_dmat(File masterDir, File outDir) throws IOException {
        m_i_master = new File(masterDir.getPath() + "/h-crisis_emis_medical_institute_master.csv");
        siFile= new File(outDir.getPath() + "/20161118_01/20161118_01_si.csv");
        File outFile = new File(outDir.getPath() + "/hospital_info.csv");
        hospital_distance_info= new File(outDir.getPath() + "/hospital_distance_info.csv");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
       ///// pick_up_info(m_i_master,siFile,outFile);//ファイル作り
        pick_up_kyoten_dmat(outFile);
        pick_up_distance(hospital_distance_info);
    }
    /*
    public static void main(String arg[]) throws Exception {
        m_i_master = new File("/Users/jiao/IdeaProjects/private/files_full/master/h-crisis_emis_medical_institute_master.csv");
        siFile = new File("/Users/jiao/IdeaProjects/private/files_full/out/20161118_01/20161118_01_si.csv");
        File outFile = new File("/Users/jiao/IdeaProjects/private/files_full/out/hospital_info.csv"); // 結合されたファイル
        File hospital_distance_info= new File("/Users/jiao/IdeaProjects/private/files_full/out/hospital_distance_info.csv"); // 病院と病院の距離ファイル

        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        pick_up_info(m_i_master,siFile,outFile);
        pick_up_kyoten_dmat(outFile);
        pick_up_distance(hospital_distance_info);
    }
*/
    //emisの情報と震度分布情報　（DMAT_level情報は後で）を抽出し、hospital_infoのファイルに入る
    public static void pick_up_info(File m_i_master, File base_SI, File hospital_info) {
        try {

            BufferedReader input1 = new BufferedReader(new InputStreamReader(new FileInputStream(m_i_master), "Shift_JIS"));
            BufferedReader input2 = new BufferedReader(new InputStreamReader(new FileInputStream(base_SI), "Shift_JIS"));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hospital_info, false), "Shift_JIS"));//結合した結果を新しいファイル'out'に保存する

            HashMap<String, Double> SI_mesh = new HashMap<String, Double>();//map

            String line = "";
            int count = 0;
            while ((line=input2.readLine()) != null) {
                //
                count++;
                if (count > 2) { // 第２行から
                    String[] s = line.split(",");
                    SI_mesh.put(s[0], Double.parseDouble(s[1])); // 1項目目がメッシュコード、2項目目が震度SI
                }
            }
            line = input1.readLine(); //
            output.write("ecode_pref_code,lat,lon,saigai,dmat,dmat_level,5thmesh,SI");
            while((line=input1.readLine())!=null) {
                String s[] = line.split(",");
                if(s.length==20) {
                    if(SI_mesh.containsKey(s[19])) {
                        if(s[16].equals("1")) {
                            output.write("\n" + s[1] + s[4] + "," + s[11] + "," + s[12] + "," + s[13] + "," + s[16] + "," + dmat_level + "," + s[19] + "," + SI_mesh.get(s[19]));
                        }
                        else
                            output.write("\n" + s[1] + s[4] + "," + s[11] + "," + s[12] + "," + s[13] + "," + s[16] + "," + 0 + "," + s[19] + "," + SI_mesh.get(s[19]));
                    }
                    else{
                        if(s[16].equals("1")) {
                            output.write("\n" + s[1] + s[4] + "," + s[11] + "," + s[12] + "," + s[13] + "," + s[16] + "," + dmat_level + "," + s[19] + "," + 0.0);
                        }
                        else
                            output.write("\n" + s[1] + s[4] + "," + s[11] + "," + s[12] + "," + s[13] + "," + s[16] + "," + 0 + "," + s[19] + "," + 0.0);
                    }
                }
                // 第２０列目は空欄なら,震度わからないので,何もしない
            }

            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //災害拠点病院と搬送可能なdmatを選定する
    //震度6.0以上によって災害本部の候補を決める
    //震度5.0以下のdmatは搬送可能
    public static void pick_up_kyoten_dmat(File hospital_info) {
        try {
            BufferedReader input1 = new BufferedReader(new InputStreamReader(new FileInputStream(hospital_info), "Shift_JIS"));

            String line = "";
            line=input1.readLine();
            while ((line=input1.readLine()) != null) {
                    String[] s = line.split(",");
                if(Double.parseDouble(s[3])==1 && Double.parseDouble(s[7])>=6.0) {
                    kyoten.add(s[0]); //災害拠点病院をpick up
                    kyoten_level.put(s[0], Double.parseDouble(s[5]));
                }
                if(Double.parseDouble(s[4])==1 && Double.parseDouble(s[7])<=5.0) {
                    dmat.add(s[0]);
                    Dmat_level.put(s[0], Double.parseDouble(s[5])); //DMAT搬送可能な病院をpick up
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //拠点病院の確認
        /*int t=0;
        for (Double key : Dmat_level.values()) {
            t++;
            System.out.println(t);
        }*/

    }


    //抽出された災害拠点病院と搬送可能なdmatの距離を探す
    public static void pick_up_distance(File distance_info) {
        try {
            BufferedReader input1 = new BufferedReader(new InputStreamReader(new FileInputStream(distance_info), "Shift_JIS"));

            String line = "";
            line=input1.readLine();
            while ((line=input1.readLine()) != null) {
                String[] s = line.split(",");
                if(kyoten.contains(s[0]) && Dmat_level.containsKey(s[1])){
                    HashMap<String, String> hospital_dmat = new HashMap<String, String>();//
                    hospital_dmat.put(s[0],s[1]);
                    kyoten_Dmat_distance.put(hospital_dmat,Double.parseDouble(s[2]));
                }


            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //拠点病院の確認
       /* for (Double value : kyoten_Dmat_distance.values()) {

            System.out.println("value = " + value);

        }*/

    }


}
