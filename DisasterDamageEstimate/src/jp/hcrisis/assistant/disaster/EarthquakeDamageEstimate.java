package jp.hcrisis.assistant.disaster;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import files.FileManagement;
import gis.CreateShape;
import gis.Shape2GeoJson;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 地震の震度分布から被害を推計するプログラム
 * Created by manab on 2016/08/08.
 */
public class EarthquakeDamageEstimate {

    private static File meshBaseFile;
    private static File rateFile1;
    private static File rateFile2;
    private static File mesh4thDataFile;
    private static File buildingYearFile;
    private static File sheltersFile;
    private static File nearShelterFile;
    private static File siFile;
    private static File shapeDir;

    /**
     * EarthquakeDamageEstimateのコンストラクタ。各ファイルの設定を行う。
     * @param masterDir マスターファイルのフォルダ
     * @param siFile 震度分布ファイル
     * @param outDir 出力ファイルのフォルダ
     * @param code 災害コード
     */
    EarthquakeDamageEstimate(File masterDir, File shapeDir, File siFile, File outDir, String code) throws Exception {
        meshBaseFile = new File(masterDir.getPath() + "/mesh_base.csv");
        this.shapeDir = shapeDir; // Shapeファイルがあるフォルダ
        rateFile1 = new File(masterDir.getPath() + "/rate_zenkai.csv"); // 全壊率テーブル
        rateFile2 = new File(masterDir.getPath() + "/rate_zenhankai.csv"); // 全半壊率テーブル
        mesh4thDataFile = new File(masterDir.getPath() + "/mesh4th_data.csv"); // 4次メッシュの統計データ
        buildingYearFile = new File(masterDir.getPath() + "/building_year.csv"); // 市町村別築年数データ
        sheltersFile = new File(masterDir.getPath() + "/shelters.csv"); // 避難所データ
        nearShelterFile = new File(masterDir.getPath() + "/near_shelters.csv"); // 近傍避難所データ
        this.siFile = siFile; // 震度分布ファイル

        File[] masterFiles = {this.meshBaseFile, this.rateFile1, this.rateFile2, this.mesh4thDataFile, this.buildingYearFile, this.sheltersFile, this.nearShelterFile, this.siFile, outDir};
        for(File file : masterFiles) {
            if(!file.exists()) {
                System.out.println("jp.hcrisis.assistant.disaster.EarthquakeDamageEstimate: " + file.getPath() + "/" + file.getName() + " が見つかりません。");
                System.exit(1);
            }
        }

        File outFile1 = new File(outDir.getPath() + "/" + code + "_mesh_base_01_SI.csv"); // 被災地のみの5次メッシュ
        File outFile2 = new File(outDir.getPath() + "/" + code + "_municipalities_base_01_SI.csv"); // 市区町村ごとの震度分布
        File outFile3 = new File(outDir.getPath() + "/" + code + "_mesh_base_02_statical.csv"); // 被災地域に人口と世帯数を付加
        File outFile4 = new File(outDir.getPath() + "/" + code + "_mesh_base_03_damage.csv"); // 被害値の計算
        File outFile5 = new File(outDir.getPath() + "/" + code + "_mesh_base_03_damage_simple.csv"); // 5次メッシュの被害シンプル化
        File outFile6 = new File(outDir.getPath() + "/" + code + "_municipalities_base_02_damage.csv"); // 市区町村ごと被害値計算
        File outFile7 = new File(outDir.getPath() + "/" + code + "_shelter_01_evacuee.csv"); // 避難所ごとの避難者数を推計

        extractDisasterArea(this.meshBaseFile, this.siFile, outFile1, 5);
        extractDisasterMunicipalities(outFile1, outFile2);
        combineData(outFile1, this.mesh4thDataFile, this.buildingYearFile, outFile3);
        estimateDamage1(outFile3, rateFile1, rateFile2, outFile4);
        estimateDamage2(outFile4, outFile5);
        estimateDamage3(outFile4, outFile2, outFile6);
        estimateDamage4(outFile5, this.nearShelterFile, this.sheltersFile, this.siFile, outFile7);

        createMunicipalitiesGisFiles(shapeDir, outFile6, outDir);
    }

    /**
     * 5次メッシュのなかで被災地だけを抽出する（震度０より大きいメッシュ）
     * @param meshBaseFile 5次メッシュのベースファイル
     * @param siFile 震度分布ファイル
     * @param outFile 出力ファイル
     * @param meshLevel 震度のメッシュレベル（1-5）
     */
    public static void extractDisasterArea(File meshBaseFile, File siFile, File outFile, int meshLevel) {
        try(BufferedReader brMesh5File = new BufferedReader(new InputStreamReader(new FileInputStream(meshBaseFile), "Shift_JIS"));
            BufferedReader brSiFile = new BufferedReader(new InputStreamReader(new FileInputStream(siFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 被災メッシュと震度をデータ化する
            HashMap<String, Double> disasterAreaMap = new HashMap<>();
            String line = brSiFile.readLine(); //1行目は項目名なので読み込んでおく
            while((line = brSiFile.readLine()) != null) {
                String pair[] = line.split(",");
                if(!pair[1].equals("nan")) { // 被災地のなかで震度が入っていないところを除く
                    //disasterAreaMap.put(pair[0], Double.parseDouble(pair[5])); // J-SHISは1項目目がメッシュコード、6項目目が計測震度
                    disasterAreaMap.put(pair[0], Double.parseDouble(pair[1])); // 1項目目がメッシュコード、2項目目が計測震度
                }
            }

            // 被災メッシュだけ書き出す
            line = brMesh5File.readLine(); // mesh_base.csvの1行目は項目名なので書き出す
            pw.write(line + ",SI");
            while((line=brMesh5File.readLine())!=null) {
                String pair[] = line.split(",");
                if(disasterAreaMap.containsKey(pair[5-meshLevel])) {
                    if(!pair[5].equals("")) {
                        pw.write("\n" + line + "," + disasterAreaMap.get(pair[5 - meshLevel]));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 5次メッシュの震度情報を用いて市区町村別の最大、最小、平均の震度を求める
     * @param inFile 震度情報の付いた5次メッシュファイル（被災地のみ）
     * @param outFile 市区町村別の震度分布ファイル
     */
    public static void extractDisasterMunicipalities(File inFile, File outFile) {
        // 被災市区町村の最大震度、平均震度、最小震度を求める
        HashMap<String, LinkedList<Double>> municipalitiesMap = new HashMap<>(); // 市区町村コードをキーに、市区町村内の震度をリストに持つマップ変数
        LinkedList<String> municipalitiesList = new LinkedList<>(); // 市区町村コードを保持する
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = br.readLine(); // 一行目は項目名
            pw.write("JCODE,KEN,SICHO,GUN,SEIREI,SIKUCHOSON,P_NUM,H_NUM,MAX_SI,MIN_SI,AVE_SI"); // *注意
            while((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                LinkedList<Double> list;
                if(municipalitiesMap.containsKey(pair[5])) { // すでに市区町村コードをキーとする震度リストがある場合
                    list = municipalitiesMap.get(pair[5]); // 震度リストを取得
                }
                else { // 市区町村コードをキーとする震度リストがない場合
                    municipalitiesList.addLast(pair[5] + "," + pair[6] + "," + pair[7] + "," + pair[8] + "," + pair[9]
                            + "," + pair[10] + "," + pair[11] + "," + pair[12]); // 出力ファイルに必須事項を抜き出す（＊注意の震度以外の項目を抜き出す）
                    list = new LinkedList<>(); // 震度リストを作成する
                }
                list.addLast(Double.parseDouble(pair[13]));
                municipalitiesMap.put(pair[5], list);
            }

            // 市区町村の最大、最小、平均震度を求める
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

    /**
     * 5次メッシュに4次メッシュの人口、世帯数および市区町村ごとの築年数データを用いて、5次メッシュごとの築年数別世帯数ファイルを作成する
     * @param meshFile 5次メッシュファイル（震度つき）
     * @param staticalDataFile 4次メッシュデータファイル（人口・世帯数）
     * @param buildingYearFile 市区町村ごと築年数ファイル
     * @param outFile 5次メッシュごとの築年数別世帯数ファイル
     */
    public static void combineData(File meshFile, File staticalDataFile, File buildingYearFile, File outFile) {
        try(BufferedReader brMesh = new BufferedReader(new InputStreamReader(new FileInputStream(meshFile), "Shift_JIS"));
            BufferedReader brStaticalData = new BufferedReader(new InputStreamReader(new FileInputStream(staticalDataFile), "Shift_JIS"));
            BufferedReader brBuildingYear = new BufferedReader(new InputStreamReader(new FileInputStream(buildingYearFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = brStaticalData.readLine();
            // 人口と世帯数のデータ作成、4次メッシュコードをキーに、その行を値に持つマップ変数を作る
            HashMap<String, String> staticalDataMap = new HashMap<>();
            while((line = brStaticalData.readLine()) != null) {
                String pair[] = line.split(",");
                staticalDataMap.put(pair[0], line);
            }

            line = brBuildingYear.readLine();
            // 市区町村ごとのデータ作成、市区町村コードをキーに、その行を値に持つマップ変数を作る
            HashMap<String, String> buildingYearMap = new HashMap<>();
            while((line = brBuildingYear.readLine()) != null) {
                String pair[] = line.split(",");
                buildingYearMap.put(pair[0], line);
            }

            line = brMesh.readLine(); // 1行目は項目名
            pw.write(line + ",POP,POP_M,POP_F,HOUSE,w1,w2,w3,w4,w5,w6,nw1,nw2,nw3"); // 築年数ごとの世帯数を保持する項目名の準備
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
                    line = line + ",0,0,0,0"; // 4次メッシュ人口がない場合は0
                }
                if(code.startsWith("0")) { // 市区町村コードが0から始まる場合、最初の1桁を取り除く処理
                    code = code.substring(1);
                }
                if(buildingYearMap.containsKey(code)) { // 築年数の計算
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

    /**
     * 全壊棟数、全半壊棟数を計算する
     * @param inFile 築年別建物数が保持される5次メッシュファイル
     * @param rateFile1 全壊率テーブル
     * @param rateFile2 全半壊率テーブル
     * @param outFile 出力ファイル
     */
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
                    "num_a,num_h,num_dead,num_injured,num_severe,num_evacuee1"); // 項目名の準備
            while((line = brIn.readLine()) != null) {
                String pair[] = line.split(",");
                // 震度を小数点第2位で四捨五入
                BigDecimal bi = new BigDecimal(String.valueOf(Double.parseDouble(pair[13])));
                Double si = bi.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                if(si>7.0) // 震度7以上は７に丸め込み
                    si = 7.0;

                // 全壊率の追加
                List<Double> zenkaiP = zenkaiMap.get(Double.toString(si));
                for(int i=0; i<zenkaiP.size(); i++) {
                    line = line + "," + zenkaiP.get(i);
                }

                // 全半壊率の追加
                List<Double> zenhankaiP = zenhankaiMap.get(Double.toString(si));
                for(int i=0; i<zenhankaiP.size(); i++) {
                    line = line + "," + zenhankaiP.get(i);
                }

                double num_a = 0.0; // 全壊棟数
                double num_h = 0.0; // 全半壊棟数

                for(int i=18; i<pair.length; i++) { // 築年別全壊数の計算
                    line = line + "," + Double.parseDouble(pair[i]) * zenkaiP.get(i - 18);
                    num_a = num_a + Double.parseDouble(pair[i]) * zenkaiP.get(i - 18);
                }
                for(int i=18; i<pair.length; i++) {  // 築年別全半壊数の計算
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

                // 負傷者率の計算（建物崩壊率に依存する）
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

                // 平均住民数を計算
                double pop_house = 0.0;
                if(Double.parseDouble(str[17])!=0) { // [14] / [17]
                    pop_house = Double.parseDouble(str[14]) / Double.parseDouble(str[17]);
                }

                // 負傷者の計算
                double num_injured = 0.0;
                for(int i=0; i < injuredRateList.size(); i++) {
                    num_injured = num_injured + pop_house * Double.parseDouble(str[i+18]) * injuredRateList.get(i);
                }

                // 重傷者の計算
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

    /**
     * 5次メッシュ被害推計ファイルを、5次メッシュコード、全壊、全半壊、死者数、負傷者数、重傷者数、避難者数にする
     * @param inFile 5次メッシュ被害推計ファイル
     * @param outFile シンプル化した5次メッシュ被害ファイル
     */
    public static void estimateDamage2(File inFile, File outFile) {
        try(BufferedReader brIn = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = brIn.readLine(); // 1行目は見出し
            String pair[] = line.split(",");
            pw.write(pair[0] + "," + pair[13] + "," + pair[63] + "," + pair[64] + "," + pair[65] + "," + pair[66] + "," + pair[67] + "," + pair[68]);
            while((line = brIn.readLine()) != null) {
                String tmp[] = line.split(",");
                pw.write("\n" + tmp[0] + "," + tmp[13] + "," + tmp[63] + "," + tmp[64] + "," + tmp[65] + "," + tmp[66] + "," + tmp[67] + "," + tmp[68]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 市区町村別の被害情報を集計する
     * @param inFile1 5次メッシュの被害CSV
     * @param inFile2 市区町村の被害CSV
     * @param outFile 出力ファイル
     */
    public static void estimateDamage3(File inFile1, File inFile2, File outFile) {
        try(BufferedReader brIn1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
            BufferedReader brIn2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 5次メッシュからの集計　各メッシュの被害値を抜き出す
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

    /**
     * 避難所ごとに避難者数を集計する
     * @param inFile1 シンプル化した5次メッシュ被害ファイル
     * @param inFile2 近傍避難所データ
     * @param inFile3 避難所データ
     * @param inFile4 震度分布ファイル
     * @param outFile
     */
    public static void estimateDamage4(File inFile1, File inFile2, File inFile3, File inFile4, File outFile) {
        try(BufferedReader brIn1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
            BufferedReader brIn2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
            BufferedReader brIn3 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile3), "Shift_JIS"));
            BufferedReader brIn4 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile4), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

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

            // 5次メッシュ避難者数DBの作成
            String line1 = brIn1.readLine(); // 1行目は見出し
            HashMap<String, Double> meshDB1 = new HashMap<>(); // mesh5thがキー、避難者数が値
            while((line1 = brIn1.readLine()) != null) {
                String pair[] = line1.split(",");
                meshDB1.put(pair[0], Double.parseDouble(pair[7]));
            }

            // 5次メッシュと避難所DBの作成
            String line2 = brIn2.readLine(); // 1行目は見出し
            HashMap<String, HashMap<String, Double>> meshDB2 = new HashMap<>(); // mesh5thがキー、（避難所コードがキー、距離の逆数が値）が値
            HashSet<String> col = new HashSet(); // 全ての避難所をストックする
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

            // 避難所DBの作成
            HashMap<String, Double> shelterDB = new HashMap<>(); // 避難所コードがキー、避難者数が値
            Iterator iCol = col.iterator();
            while(iCol.hasNext()) {
                String s = (String) iCol.next();
                shelterDB.put(s, 0.0);
            }

            // 避難者の推計
            for(String key : meshDB2.keySet()) {
                HashMap<String, Double> map = meshDB2.get(key);
                double d = 0.0; // 距離の総和
                for(String s : map.keySet()) {
                    d = d + map.get(s);
                }

                for(String s : map.keySet()) {
                    double evacuee = shelterDB.get(s);
                    if(d > 0 && meshDB1.containsKey(key)) {
                        evacuee = evacuee + meshDB1.get(key) * (map.get(s) / d);
                        shelterDB.put(s, evacuee);
                    }
                }
            }

            // ファイル出力
            String line3 = brIn3.readLine(); // 1行目は見出し
            boolean midashi = true;
            while(line3!=null) {
                String pair[] = line3.split(",");
                if(midashi) {
                    pw.write(pair[0] + "," + pair[5] + "," + pair[2] + "," + pair[4] + "," + pair[6] + ",si,evacuee");
                    midashi = false;
                }
                else if(shelterDB.containsKey(pair[1])) {
                    if(siDB.get(pair[17])==null) {
                        pw.write("\n" + pair[0] + "," + pair[5] + "," + pair[2] + "," + pair[4] + "," + pair[6] + "," + "0.0" + "," + shelterDB.get(pair[1]));
                    }
                    else {
                        pw.write("\n" + pair[0] + "," + pair[5] + "," + pair[2] + "," + pair[4] + "," + pair[6] + "," + siDB.get(pair[18]) + "," + shelterDB.get(pair[0]));
                    }
                }
                line3 = brIn3.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createMunicipalitiesGisFiles(File shapeDir, File inFile, File outDir) throws SchemaException {
        // 市区町村の被害DBを作る
        HashMap<String, String> municipalitiesMap = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"))) {
            String line = br.readLine(); // 1行目は項目名
            while((line = br.readLine())!=null) {
                String pair[] = line.split(",");
                municipalitiesMap.put(pair[0], line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        File hallShape = new File(shapeDir.getPath() + "/halls/halls.shp");
        ShapefileDataStore hallStore = null;
        SimpleFeatureSource hallFeatureSource;
        SimpleFeatureCollection hallC;
        FeatureIterator hallI;

        // Featureを生成する準備
        String name = "市区町村被害";
        String geom = "the_geom:Point:srid=4612,";
        SimpleFeatureType type = DataUtilities.createType(name, geom +
                "code:String,type:int,name:String,address:String,ken:String,sicho:String,gun:String,seirei:String,sikuchoson:String," + //9
                "p_num:int,h_num:int,max_si:double,min_si:double,ave_si:double,zenkai:double,zenhankai:double,dead:double,injured:double,severe:double,evacuee:double" //11
        );
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        List<SimpleFeature> features = new ArrayList<SimpleFeature>();

        try {
            hallStore = new ShapefileDataStore(hallShape.toURI().toURL());
            hallStore.setCharset(Charset.forName("UTF-8"));
            hallFeatureSource = hallStore.getFeatureSource();
            hallC = hallFeatureSource.getFeatures();
            hallI = hallC.features();
            while(hallI.hasNext()) {
                SimpleFeature feature = (SimpleFeature)hallI.next();
                Point point = geometryFactory.createPoint(((Point) feature.getAttribute("the_geom")).getCoordinate());
                String code = (String)feature.getAttribute("code");
                if(municipalitiesMap.containsKey(code)) {
                    String pair[] = municipalitiesMap.get(code).split(",");
                    featureBuilder.add(point);
                    featureBuilder.add(feature.getAttribute("code"));
                    featureBuilder.add(Integer.parseInt((String)feature.getAttribute("type")));
                    featureBuilder.add(feature.getAttribute("name"));
                    featureBuilder.add(feature.getAttribute("address"));
                    for(int i=1; i<17; i++) {
                        if(i<6) {
                            featureBuilder.add(pair[i]);
                        }
                        else if(i<8) {
                            featureBuilder.add(Integer.parseInt(pair[i]));
                        }
                        else {
                            // 小数点第2位で四捨五入
                            BigDecimal bd = new BigDecimal(String.valueOf(Double.parseDouble(pair[i])));
                            Double d = bd.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                            featureBuilder.add(d);
                        }
                    }
                    SimpleFeature f = featureBuilder.buildFeature(null);
                    features.add(f);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hallStore.dispose();
        }

        try {
            File hall = new File(outDir.getPath() + "/halls");
            hall.mkdir();
            CreateShape.createShapeFile(new File(hall.getPath() + "/halls.shp"), "UTF-8", type, features);
            Shape2GeoJson.createGeoJson(new File(hall.getPath() + "/halls.shp"), "UTF-8", new File(hall.getPath() + "/halls.geojson"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
