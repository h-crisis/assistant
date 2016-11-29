package csvmerge;

import java.io.*;
import java.util.HashMap;



    /**
     * Created by jiao on 2016/11/24.
     * csvファイルの結合プログラム
     * 同じメッシュコードなら、震度をファイルの後ろに追加
     */
        public class csv_merge {


        public static HashMap<String, Double> SI_mesh = new HashMap<String, Double>();//map

       // public static void main(String args[]) throws Exception {
        public static void merge(File m_i_master, File base_SI, File out) {
            //m_i_master.csv　は h-crisis_emis_medical_institute_masterファイル　第20列はメッシュコード
            //base_SI.csv　は outputのmesh_base_SIファイル　
            try {
            //    File m_i_master = new File("/Users/jiao/IdeaProjects/2/assistant/test/xue/csvmerge/h-crisis_emis_medical_institute_master_04.csv");
            //    File base_SI = new File("/Users/jiao/IdeaProjects/2/assistant/files/out/20161118_01/20161118_01_mesh_base_01_SI.csv");


                BufferedReader input1 = new BufferedReader(new InputStreamReader(new FileInputStream(m_i_master), "Shift_JIS"));
                BufferedReader input2 = new BufferedReader(new InputStreamReader(new FileInputStream(base_SI), "Shift_JIS"));
             //   File file = new File("/Users/jiao/IdeaProjects/2/assistant/test/xue/csvmerge/out.csv");
                //PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "Shift_JIS"));
                //PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(m_i_master, false), "Shift_JIS")); //結合した結果をm_i_master.csvに保存する
                 PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, false), "Shift_JIS"));//結合した結果を新しいファイル'out'に保存する

                String line = "";
                int count = 0;
                while ((line=input2.readLine()) != null) {
                    //
                    count++;
                    if (count > 2) { // 第２行から
                        String[] s = line.split(",");
                        SI_mesh.put(s[0], Double.parseDouble(s[13])); // 1項目目がメッシュコード、2項目目が震度SI
                    }
                }
                line = input1.readLine(); //
                output.write(line + ",SI");
                while((line=input1.readLine())!=null) {
                    String s[] = line.split(",");
                    if(SI_mesh.containsKey(s[19])) {
                        if(!s[19].equals("")) {
                            output.write("\n" + line + "," + SI_mesh.get(s[19]));
                        }
                    }
                }
                //  System.out.println(SI_mesh.get("5034717214"));
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }