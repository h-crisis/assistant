import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manab on 2016/08/08.
 */
public class Estimate {
    public static void main(String args[]) {
        File mesh5File = new File("files/master/mesh_base.csv");
        File rateFile1 = new File("files/CsvFiles/rate_zenkai.csv");
        File rateFile2 = new File("files/CsvFiles/rate_zenhankai.csv");
        File siFile = new File("files/JSHIS/F015321/S-V1-F015321-MAP-SHAPE-CASE1.csv"); // F015321は伊勢原断層
        File outFile1 = new File("files/JSHIS/F015321/mesh_base_f015321.csv"); // 被災地のみの5次メッシュ
        File outFile2 = new File("files/JSHIS/F015321/municipalities_base_f015321.csv"); // 市区町村ごとの震度分布
        File outFile3 = new File("files/JSHIS/F015321/mesh_base_f015321_statical.csv"); // 被災地域に人口と世帯数を付加
        File outFile4 = new File("files/JSHIS/F015321/mesh_base_f015321_damage.csv"); // 被害値の計算
        File outFile5 = new File("files/JSHIS/F015321/municipalities_base_f015321_damage.csv"); // // 市区町村ごと被害値計算

        extractDisasterArea(mesh5File, siFile, outFile1, 5);
        extractDisasterMunicipalities(outFile1, outFile2);
        combineData(outFile1, new File("files/CsvFiles/mesh4th_data.csv"), new File("files/CsvFiles/building_year_rate.csv"), outFile3);
        estimateDamage1(outFile3, rateFile1, rateFile2, outFile4);
        estimateDamage2(outFile4, outFile2, outFile5);
    }


    public static void extractDisasterArea(File mesh5File, File siFile, File outFile, int meshLevel) {
        try(BufferedReader brMesh5File = new BufferedReader(new InputStreamReader(new FileInputStream(mesh5File), "Shift_JIS"));
            BufferedReader brSiFile = new BufferedReader(new InputStreamReader(new FileInputStream(siFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 被災メッシュと震度をデータ化する
            HashMap<String, Double> disasterAreaMap = new HashMap<>();
            String line = brSiFile.readLine(); //J-SHISの1行目は項目名なので読み込んでおく
            while((line = brSiFile.readLine()) != null) {
                String pair[] = line.split(",");
                if(!pair[5].equals("nan")) { // 被災地のなかで震度が入っていないところを除く
                    disasterAreaMap.put(pair[0], Double.parseDouble(pair[5])); // J-SHISは1項目目がメッシュコード、5項目目が計測震度
                }
            }

            // 被災メッシュだけ書き出す
            line = brMesh5File.readLine(); // mesh_base.csvの1行目は項目名なので書き出す
            pw.write(line + ",SI");
            while((line=brMesh5File.readLine())!=null) {
                String pair[] = line.split(",");
                if(disasterAreaMap.containsKey(pair[5-meshLevel])) {
                    pw.write("\n" + line + "," + disasterAreaMap.get(pair[5-meshLevel]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void extractDisasterMunicipalities(File inFile, File outFile) {
        // 被災市区町村の最大震度、平均震度、最小震度を求める
        HashMap<String, LinkedList<Double>> municipalitiesMap = new HashMap<>();
        LinkedList<String> municipalitiesList = new LinkedList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = br.readLine(); // 一行目は項目名
            pw.write("JCODE,KEN,SICHO,GUN,SEIREI,SIKUCHOSON,P_NUM,H_NUM,MAX_SI,MIN_SI,AVE_SI");
            while((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                LinkedList<Double> list;
                if(municipalitiesMap.containsKey(pair[5])) {
                    list = municipalitiesMap.get(pair[5]);
                }
                else {
                    municipalitiesList.addLast(pair[5] + "," + pair[6] + "," + pair[7] + "," + pair[8] + "," + pair[9]
                            + "," + pair[10] + "," + pair[11] + "," + pair[12]);
                    list = new LinkedList<>();
                }
                list.addLast(Double.parseDouble(pair[13]));
                municipalitiesMap.put(pair[5], list);
            }

            for(int i=0; i<municipalitiesList.size(); i++) {
                String municipality = municipalitiesList.get(i);
                String pair[] = municipality.split(",");
                LinkedList<Double> list = municipalitiesMap.get(pair[0]);
                double min = Double.MAX_VALUE;
                double max = 0.0;
                double ave = 0.0;
                for(double d : list) {
                    if(d >= max) {
                        max = d;
                    }
                    if(d <= min) {
                        min = d;
                    }
                    ave = ave + d;
                }
                ave = ave / list.size();

                pw.write("\n" + municipality + "," + max + "," + min + "," + ave);
             }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void combineData(File meshFile, File staticalDataFile, File buildingYearFile, File outFile) {
        try(BufferedReader brMesh = new BufferedReader(new InputStreamReader(new FileInputStream(meshFile), "Shift_JIS"));
            BufferedReader brStaticalData = new BufferedReader(new InputStreamReader(new FileInputStream(staticalDataFile), "Shift_JIS"));
            BufferedReader brBuildingYear = new BufferedReader(new InputStreamReader(new FileInputStream(buildingYearFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = brStaticalData.readLine();
            HashMap<String, String> staticalDataMap = new HashMap<>();
            while((line = brStaticalData.readLine()) != null) {
                String pair[] = line.split(",");
                staticalDataMap.put(pair[0], line);
            }

            line = brBuildingYear.readLine();
            HashMap<String, String> buildingYearMap = new HashMap<>();
            while((line = brBuildingYear.readLine()) != null) {
                String pair[] = line.split(",");
                buildingYearMap.put(pair[0], line);
            }

            line = brMesh.readLine(); // 1行目は項目名
            pw.write(line + ",POP,POP_M,POP_F,HOUSE,w1,w2,w3,w4,w5,w6,nw1,nw2,nw3");
            while((line = brMesh.readLine()) != null) {
                String pair[] = line.split(",");
                String code = pair[5];
                Double house = 0.0;
                if(staticalDataMap.containsKey(pair[1])) {
                    String data[] = staticalDataMap.get(pair[1]).split(","); // 4次メッシュの人口を取得
                    for (int i = 1; i < data.length; i++) {
                        line = line + "," + (Double.parseDouble(data[i])/4);
                        house = (Double.parseDouble(data[i])/4);
                    }
                }
                else {
                    line = line + ",0,0,0,0";
                }
                if(code.startsWith("0")) {
                    code = code.substring(1);
                }
                if(buildingYearMap.containsKey(code)) {
                    String data[] = buildingYearMap.get(code).split(",");
                    for (int i = 3; i < data.length; i++) {
                        line = line + "," + (house * Double.parseDouble(data[i]));
                    }
                }
                pw.write("\n" + line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void estimateDamage1(File inFile, File rateFile1, File rateFile2, File outFile) {
        try(BufferedReader brIn = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"));
            BufferedReader brRate1 = new BufferedReader(new InputStreamReader(new FileInputStream(rateFile1), "Shift_JIS"));
            BufferedReader brRate2 = new BufferedReader(new InputStreamReader(new FileInputStream(rateFile2), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 全壊率テーブルを作成する
            HashMap<String, List<Double>> zenkaiMap = new HashMap<String, List<Double>>();
            String line = brRate1.readLine();
            while((line = brRate1.readLine()) != null) {
                String pair[] = line.split(",");
                List<Double> list = new ArrayList<>();
                for(int i = 1; i<pair.length; i++) {
                    list.add(Double.parseDouble(pair[i]));
                }
                zenkaiMap.put(pair[0], list);
            }

            // 全半壊率テーブルを作成する
            HashMap<String, List<Double>> zenhankaiMap = new HashMap<String, List<Double>>();
            line = brRate2.readLine();
            while((line = brRate2.readLine()) != null) {
                String pair[] = line.split(",");
                List<Double> list = new ArrayList<>();
                for(int i = 1; i<pair.length; i++) {
                    list.add(Double.parseDouble(pair[i]));
                }
                zenhankaiMap.put(pair[0], list);
            }

            line = brIn.readLine();
            pw.write(line + ",pa_w1,pa_w2,pa_w3,pa_w4,pa_w5,pa_w6,pa_nw1,pa_nw2,pa_nw3," +
                    "ph_w1,ph_w2,ph_w3,ph_w4,ph_w5,ph_w6,ph_nw1,ph_nw2,ph_nw3," +
                    "a_w1,a_w2,a_w3,a_w4,a_w5,a_w6,a_nw1,a_nw2,a_nw3," +
                    "h_w1,h_w2,h_w3,h_w4,h_w5,h_w6,h_nw1,h_nw2,h_nw3," +
                    "num_a,num_h,num_dead,num_injured,num_severe,num_evacuee1");
            while((line = brIn.readLine()) != null) {
                String pair[] = line.split(",");
                // 震度を小数点第2位で四捨五入
                BigDecimal bi = new BigDecimal(String.valueOf(Double.parseDouble(pair[13])));
                Double si = bi.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                if(si>7.0)
                    si = 7.0;

                List<Double> zenkaiP = zenkaiMap.get(Double.toString(si));
                for(int i=0; i<zenkaiP.size(); i++) {
                    line = line + "," + zenkaiP.get(i);
                }

                List<Double> zenhankaiP = zenhankaiMap.get(Double.toString(si));
                for(int i=0; i<zenhankaiP.size(); i++) {
                    line = line + "," + zenhankaiP.get(i);
                }

                double num_a = 0.0;
                double num_h = 0.0;

                for(int i=18; i<pair.length; i++) {
                    line = line + "," + Double.parseDouble(pair[i]) * zenkaiP.get(i - 18);
                    num_a = num_a + Double.parseDouble(pair[i]) * zenkaiP.get(i - 18);
                }
                for(int i=18; i<pair.length; i++) {
                        line = line + "," + Double.parseDouble(pair[i]) * zenhankaiP.get(i-18);
                        num_h = num_h + Double.parseDouble(pair[i]) * zenhankaiP.get(i-18);
                }
                line = line + "," + num_a + "," + num_h;

                // 死者数の計算
                String str[] = line.split(",");
                double num_dead = (Double.parseDouble(str[45]) + Double.parseDouble(str[46]) + Double.parseDouble(str[47]) + Double.parseDouble(str[48])
                        + Double.parseDouble(str[49]) + Double.parseDouble(str[50])) * 0.0676 + (Double.parseDouble(str[51]) + Double.parseDouble(str[52]) + Double.parseDouble(str[53])) * 0.0240;

                // 負傷者の計算
                LinkedList<Double> injuredRateList = new LinkedList<>();
                injuredRateList.addLast(Double.parseDouble(str[27]) + 0.5 * Double.parseDouble(str[36]));
                injuredRateList.addLast(Double.parseDouble(str[28]) + 0.5 * Double.parseDouble(str[37]));
                injuredRateList.addLast(Double.parseDouble(str[29]) + 0.5 * Double.parseDouble(str[38]));
                injuredRateList.addLast(Double.parseDouble(str[30]) + 0.5 * Double.parseDouble(str[39]));
                injuredRateList.addLast(Double.parseDouble(str[31]) + 0.5 * Double.parseDouble(str[40]));
                injuredRateList.addLast(Double.parseDouble(str[32]) + 0.5 * Double.parseDouble(str[41]));
                injuredRateList.addLast(Double.parseDouble(str[33]) + 0.5 * Double.parseDouble(str[42]));
                injuredRateList.addLast(Double.parseDouble(str[34]) + 0.5 * Double.parseDouble(str[43]));
                injuredRateList.addLast(Double.parseDouble(str[35]) + 0.5 * Double.parseDouble(str[44]));

                for(int i =0 ; i < injuredRateList.size(); i++) {
                    double d = injuredRateList.get(i);
                    if(d < 0.25) {
                        injuredRateList.add(i, 0.12 * d);
                    }
                    else if(d < 0.375) {
                        injuredRateList.add(i, 0.07 - 0.16 * d);
                    }
                    else {
                        injuredRateList.add(i, 0.01);
                    }
                    injuredRateList.remove(i+1);
                }

                double pop_house = 0.0;
                if(Double.parseDouble(str[17])!=0) { // [14] / [17]
                    pop_house = Double.parseDouble(str[14]) / Double.parseDouble(str[17]);
                }

                double num_injured = 0.0;
                for(int i=0; i < injuredRateList.size(); i++) {
                    num_injured = num_injured + pop_house * Double.parseDouble(str[i+18]) * injuredRateList.get(i);
                }

                double num_severe = 0.0;
                for(int i=0; i < 9; i++) {
                    num_severe = num_severe + pop_house * Double.parseDouble(str[i+18]) * 0.0309 * Double.parseDouble(str[i+27]);
                }

                // 避難者の計算
                double num_evacuee0 = 0.0;
                for(int i=0; i< 9; i++) {
                    num_evacuee0 = num_evacuee0 + pop_house * Double.parseDouble(str[i+45]) + pop_house * Double.parseDouble(str[i+54]) * 0.503;
                }

                line = line + "," + num_dead + "," + num_injured + "," + num_severe + "," + num_evacuee0;
                pw.write("\n" + line);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void estimateDamage2(File inFile1, File inFile2, File outFile) {
        try(BufferedReader brIn1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
            BufferedReader brIn2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 5次メッシュからの集計
            HashMap<String, LinkedList<Double>> meshMap = new HashMap<>();
            String line = brIn1.readLine(); // 1行目は見出し
            while((line = brIn1.readLine()) != null) {
                LinkedList<Double> list;
                String pair[] = line.split(",");
                if(meshMap.containsKey(pair[5])) {
                    list = meshMap.get(pair[5]);
                }
                else {
                    list = new LinkedList<>();
                    for(int i=0; i<6; i++) {
                        list.addLast(0.0);
                    }
                }
                for(int i=0; i<list.size(); i++) {
                    double d = list.get(i);
                    d = d + Double.parseDouble(pair[i + 63]);
                    list.add(i, d);
                    list.remove(i+1);
                }
                meshMap.put(pair[5], list);
            }

            // 市区町村被害を書き出す
            line = brIn2.readLine(); // 市区町村ファイルの1行目は見出し
            pw.write(line + ",num_zenkai,num_zenhankai,num_dead,num_injured,num_severe,num_evacuee0");
            while((line = brIn2.readLine()) != null) {
                String pair[] = line.split(",");
                LinkedList<Double> list = meshMap.get(pair[0]);
                for(int i=0; i<list.size(); i++) {
                    line = line + "," + list.get(i);
                }
                pw.write("\n" + line);
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
