package jp.hcrisis.assistant.disaster;

import java.io.*;
import java.util.HashMap;

/**
  *Created by jiao on 2016/11/24.
  *csvファイルの結合プログラム
  *同じメッシュコードなら、震度をファイルの後ろに追加
 */

    public class csv_merge {
        private static File m_i_master;
        private static File base_SI;

        /**
         * @param masterDir マスターファイルのフォルダ
         * @param outDir 震度分布ファイルのフォルダ
         * @param code
         */

        csv_merge(File masterDir, File outDir, String code) throws Exception {
            m_i_master = new File(masterDir.getPath() + "/h-crisis_emis_medical_institute_master.csv");
            base_SI= new File(outDir.getPath() + "/" + code + "_mesh_base_01_SI.csv");
            File outFile = new File(outDir.getPath() + "/" + code + "_merge_SI.csv"); // 結合されたファイル
            if(!outFile.exists()) {
                outFile.createNewFile();
            }


            merge(this.m_i_master, this.base_SI, outFile);
        }


    /**
     * @param m_i_master h-crisis_emis_medical_institute_masterファイル; 第20列目はメッシュコード
     * @param base_SI 震度分布ファイル,1列目はメッチュコード；14列目は震度
     * @param merge_file 出力ファイル
     */

        public static void merge(File m_i_master, File base_SI, File merge_file) {
            try {

                BufferedReader input1 = new BufferedReader(new InputStreamReader(new FileInputStream(m_i_master), "Shift_JIS"));
                BufferedReader input2 = new BufferedReader(new InputStreamReader(new FileInputStream(base_SI), "Shift_JIS"));
                PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(merge_file, false), "Shift_JIS"));//結合した結果を新しいファイル'out'に保存する

                HashMap<String, Double> SI_mesh = new HashMap<String, Double>();//map

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
                    if(s.length==20) {
                        if(SI_mesh.containsKey(s[19])) {
                            output.write("\n" + line + "," + SI_mesh.get(s[19]));
                        }
                        else{
                            output.write("\n" + line + ", ");
                        }
                    }
                    else{// 第２０列目は空欄なら
                        output.write("\n" + line + ", ");
                    }

                }

                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
