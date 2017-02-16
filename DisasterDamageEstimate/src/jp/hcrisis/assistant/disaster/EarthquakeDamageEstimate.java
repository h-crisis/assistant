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
    private static File hospitalsFile;
    private static File nearHospitalFile;
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
        buildingYearFile = new File(masterDir.getPath() + "/building_year.csv"); // 市町村別築年数データ
        sheltersFile = new File(masterDir.getPath() + "/shelters.csv"); // 避難所データ
        nearShelterFile = new File(masterDir.getPath() + "/near_shelters.csv"); // 近傍避難所データ
        hospitalsFile = new File(masterDir.getPath() + "/hospitals.csv"); // 避難所データ
        nearHospitalFile = new File(masterDir.getPath() + "/near_hospital.csv"); // 近傍医療機関データ
        this.siFile = siFile; // 震度分布ファイル

        File[] masterFiles = {this.meshBaseFile, this.rateFile1, this.rateFile2, this.buildingYearFile, this.sheltersFile, this.nearShelterFile, this.siFile, outDir};
        for(File file : masterFiles) {
            if(!file.exists()) {
                System.out.println("jp.hcrisis.assistant.disaster.EarthquakeDamageEstimate: " + file.getPath() + "/" + file.getName() + " が見つかりません。");
                System.exit(1);
            }
        }

        File outFile1 = new File(outDir.getPath() + "/" + code + "_mesh_base_01_SI.csv"); // 被災地のみの5次メッシュ
        File outFile2 = new File(outDir.getPath() + "/" + code + "_mesh_base_02_damage.csv"); // 5次メッシュ被害推計
        File outFile3 = new File(outDir.getPath() + "/" + code + "_mesh_base_03_damage_simple.csv"); // 5次メッシュの被害シンプル化
        File outFile4 = new File(outDir.getPath() + "/" + code + "_hospital_01_damage.csv"); // 医療機関ごとの被害を推計
        File outFile5 = new File(outDir.getPath() + "/" + code + "_hospital_01_damage_simple.csv"); // 医療機関ごとの被害を推計
        File outFile6 = new File(outDir.getPath() + "/" + code + "_shelter_01_evacuee.csv"); // 避難所ごとの避難者数を推計
        File outFile7 = new File(outDir.getPath() + "/shelter"); // 避難所フォルダ
        outFile7.mkdir();

        // 5次メッシュ被害ファイルの作成
        extractDisasterArea(this.meshBaseFile, this.siFile, outFile1, 5); // 被災地5次メッシュの抽出
        estimateDamageMesh(outFile1, this.buildingYearFile, this.rateFile1, this.rateFile2, outFile2);
        estimateDamageMeshSimple(outFile2, outFile3); // シンプル化した被害ファイル

        // 医療機関・避難所の被害ファイル作成
        calcHospitalDamage(outFile3, this.nearHospitalFile, this.hospitalsFile, outFile4, outFile5);
        calcShelterDamage(outFile3, this.nearShelterFile, this.sheltersFile, this.siFile, outFile6, outFile7);

        //createMunicipalitiesGisFiles(shapeDir, outFile6, outDir);

        /*
        File outFile2 = new File(outDir.getPath() + "/" + code + "_municipalities_base_01_SI.csv"); // 市区町村ごとの震度分布
        File outFile5 = new File(outDir.getPath() + "/" + code + "_mesh_base_03_damage_simple.csv"); // 5次メッシュの被害シンプル化
        File outFile6 = new File(outDir.getPath() + "/" + code + "_municipalities_base_02_damage.csv"); // 市区町村ごと被害値計算
        File outFile7 = new File(outDir.getPath() + "/" + code + "_shelter_01_evacuee.csv"); // 避難所ごとの避難者数を推計
        File outFile8 = new File(outDir.getPath() + "/shelter"); // 避難所フォルダ
        outFile8.mkdir();
        File outFile9 = new File(outDir.getPath() + "/" + code + "_hospital_01_patient.csv"); // 避難所ごとの避難者数を推計
        File outFile10 = new File(outDir.getPath() + "/" + code + "_hospital_01_patient_simple.csv"); // 避難所ごとの避難者数を推計
        */

        /*
        combineData(outFile1, this.mesh4thDataFile, this.buildingYearFile, outFile3); // 被災地5次メッシュに人口・世帯・建築年分布を追記
        estimateDamage5mesh1(outFile3, rateFile1, rateFile2, outFile4); // 被害ファイルの作成
        estimateDamage5mesh2(outFile4, outFile5); // シンプル化した被害ファイル
        extractDisasterMunicipalities(outFile1, outFile2);
        estimateDamage3(outFile4, outFile2, outFile6);
        estimateDamage4(outFile5, this.nearShelterFile, this.sheltersFile, this.siFile, outFile7, outFile8);
        estimateDamage5(outFile5, this.nearHospitalFile, this.hospitalsFile, outFile9, outFile10);
        createMunicipalitiesGisFiles(shapeDir, outFile6, outDir);
        */
    }

    /**
     * 5次メッシュのなかで被災地だけを抽出する（震度０より大きいメッシュ）
     * @param meshBaseFile 5次メッシュのベースファイル
     * @param siFile 震度分布ファイル
     * @param outFile 出力ファイル
     * @param meshLevel 震度のメッシュレベル（1-5）
     */
    public static void extractDisasterArea(File meshBaseFile, File siFile, File outFile, int meshLevel) {
        System.out.println("5次メッシュのなかで被災地だけを抽出します（震度０より大きいメッシュ）。");
        int count = 0;
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
                    count++;
                    pw.write("\n" + line + "," + disasterAreaMap.get(pair[5 - meshLevel]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("5次メッシュのなかで被災地だけを抽出しました。被災5次メッシュは " + count + " メッシュです。");
    }

    /**
     *
     * @param meshFile 5次メッシュファイル（震度つき）
     * @param buildingYearFile 市区町村ごと築年数ファイル
     * @param rateFile1 全壊率テーブル
     * @param rateFile2 全半壊率テーブル
     * @param outFile
     */
    public static void estimateDamageMesh(File meshFile, File buildingYearFile, File rateFile1, File rateFile2, File outFile) {
        System.out.println("5次メッシュ被災地の被害を計算します。");
        try(BufferedReader brMesh = new BufferedReader(new InputStreamReader(new FileInputStream(meshFile), "Shift_JIS"));
            BufferedReader brBuildingYear = new BufferedReader(new InputStreamReader(new FileInputStream(buildingYearFile), "Shift_JIS"));
            BufferedReader brRate1 = new BufferedReader(new InputStreamReader(new FileInputStream(rateFile1), "Shift_JIS"));
            BufferedReader brRate2 = new BufferedReader(new InputStreamReader(new FileInputStream(rateFile2), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 市区町村ごと築年数ファイルのDB化
            String buildingYearline = brBuildingYear.readLine();
            HashMap<String, String> buildingYearMap = new HashMap<>(); // 市区町村ごとのデータ作成、市区町村コードをキーに、その行を値に持つマップ変数を作る
            while((buildingYearline = brBuildingYear.readLine()) != null) {
                String pair[] = buildingYearline.split(",");
                buildingYearMap.put(pair[0], buildingYearline);
            }

            // 全壊率テーブルを作成する
            HashMap<String, String> zenkaiMap = new HashMap<String, String>();
            String zenkaiLine = brRate1.readLine();
            while((zenkaiLine = brRate1.readLine()) != null) {
                String pair[] = zenkaiLine.split(",");
                zenkaiMap.put(pair[0], zenkaiLine);
            }

            // 全半壊率テーブルを作成する
            HashMap<String, String> zenhankaiMap = new HashMap<String, String>();
            String zenhankaiLine = brRate2.readLine();
            while((zenhankaiLine = brRate2.readLine()) != null) {
                String pair[] = zenhankaiLine.split(",");
                zenhankaiMap.put(pair[0], zenhankaiLine);
            }

            String meshLine = brMesh.readLine(); // 1行目は見出し
            pw.print(meshLine + ",num_a,num_h,num_dead,num_injured,num_severe,num_evacuee");
            while((meshLine = brMesh.readLine())!=null) {
                String pair[] = meshLine.split(",");
                String code = pair[0];
                String jcode = pair[7];
                if(jcode.startsWith("0")) {
                    jcode = jcode.substring(1);
                }
                BigDecimal bi = new BigDecimal(String.valueOf(Double.parseDouble(pair[16]))); // 震度を小数点第2位で四捨五入
                double si = bi.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                if(si>7.0) // 震度7以上は７に丸め込み
                    si = 7;
                if(!buildingYearMap.containsKey(jcode)) {
                    System.out.println(meshLine);
                    System.exit(1);
                }
                double damage[] = calcDamage(Double.parseDouble(pair[10]), Double.parseDouble(pair[13]), buildingYearMap.get(jcode),
                        zenkaiMap.get(Double.toString(si)), zenhankaiMap.get(Double.toString(si)));
                for(double d : damage) {
                    meshLine = meshLine + "," + d;
                }
                pw.print("\n" + meshLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("5次メッシュ被災地の被害を計算しました。");
    }

    /**
     * メッシュの被害計算メソッド
     * @param pop
     * @param house
     * @param buildingYearLine
     * @param rate1Line
     * @param rate2Line
     * @return
     */
    public static double[] calcDamage(double pop, double house, String buildingYearLine,
                                      String rate1Line, String rate2Line) {
        double pop_rate = 0.0;
        if(house>0)
            pop_rate = pop / house;

        // 築年数別世帯数の取得
        String building[] = buildingYearLine.split(",");
        double w1 = house * Double.parseDouble(building[3]); // 木造旧築年
        double w2 = house * Double.parseDouble(building[4]); // 木造中築年1
        double w3 = house * Double.parseDouble(building[5]); // 木造中築年2
        double w4 = house * Double.parseDouble(building[6]); // 木造新築年1
        double w5 = house * Double.parseDouble(building[7]); // 木造新築年2
        double w6 = house * Double.parseDouble(building[8]); // 木造新築年3
        double nw1 = house * Double.parseDouble(building[9]); // 非木造旧築年
        double nw2 = house * Double.parseDouble(building[10]); // 非木造中築年
        double nw3 = house * Double.parseDouble(building[11]); // 非木造真築年

        // 全壊建物数の計算
        String rate1[] = rate1Line.split(",");
        double a_w1 = w1 * Double.parseDouble(rate1[1]); // 木造旧築年
        double a_w2 = w2 * Double.parseDouble(rate1[2]); // 木造中築年1
        double a_w3 = w3 * Double.parseDouble(rate1[3]); // 木造中築年2
        double a_w4 = w4 * Double.parseDouble(rate1[4]); // 木造新築年1
        double a_w5 = w5 * Double.parseDouble(rate1[5]); // 木造新築年2
        double a_w6 = w6 * Double.parseDouble(rate1[6]); // 木造新築年3
        double a_nw1 = nw1 * Double.parseDouble(rate1[7]); // 非木造旧築年
        double a_nw2 = nw2 * Double.parseDouble(rate1[8]); // 非木造中築年
        double a_nw3 = nw3 * Double.parseDouble(rate1[9]); // 非木造真築年
        double sum_a = a_w1 + a_w2 + a_w3 + a_w4 + a_w5 + a_w6 + a_nw1 + a_nw2 + a_nw3;

        // 半壊建物数の計算
        String rate2[] = rate2Line.split(",");
        double h_w1 = w1 * Double.parseDouble(rate2[1]) - a_w1; // 木造旧築年
        double h_w2 = w2 * Double.parseDouble(rate2[2]) - a_w2; // 木造中築年1
        double h_w3 = w3 * Double.parseDouble(rate2[3]) - a_w3; // 木造中築年2
        double h_w4 = w4 * Double.parseDouble(rate2[4]) - a_w4; // 木造新築年1
        double h_w5 = w5 * Double.parseDouble(rate2[5]) - a_w5; // 木造新築年2
        double h_w6 = w6 * Double.parseDouble(rate2[6]) - a_w6; // 木造新築年3
        double h_nw1 = nw1 * Double.parseDouble(rate2[7]) - a_nw1; // 非木造旧築年
        double h_nw2 = nw2 * Double.parseDouble(rate2[8]) - a_nw2; // 非木造中築年
        double h_nw3 = nw3 * Double.parseDouble(rate2[9]) - a_nw3; // 非木造真築年
        double sum_h = h_w1 + h_w2 + h_w3 + h_w4 + h_w5 + h_w6 + h_nw1 + h_nw2 + h_nw3;

        // 死者数の計算
        double num_dead = (a_w1 + a_w2 + a_w3 + a_w4 + a_w5 + a_w6) * 0.0676 + (a_nw1 + a_nw2 + a_nw3) * 0.00840;

        // 負傷者の計算
        double num_injured = (sum_a + sum_h) * 0.177;

        // 重傷者の計算
        double num_severe = sum_a * 0.1;

        // 避難者の計算
        double num_evacuee = pop_rate * (sum_a + sum_h * 0.13);

        double num[] = {sum_a, sum_h, num_dead, num_injured, num_severe, num_evacuee};
        return num;
    }

    /**
     * 5次メッシュ被害推計ファイルを、5次メッシュコード、全壊、全半壊、死者数、負傷者数、重傷者数、避難者数にする
     * @param inFile 5次メッシュ被害推計ファイル
     * @param outFile シンプル化した5次メッシュ被害ファイル
     */
    public static void estimateDamageMeshSimple(File inFile, File outFile) {
        try(BufferedReader brIn = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = brIn.readLine(); // 1行目は見出し
            String pair[] = line.split(",");
            pw.write(pair[0] + "," + pair[16] + "," + pair[17] + "," + pair[18] + "," + pair[19] + "," + pair[20] + "," + pair[21] + "," + pair[22]);
            while((line = brIn.readLine()) != null) {
                String tmp[] = line.split(",");
                pw.write("\n" + tmp[0] + "," + tmp[16] + "," + tmp[17] + "," + tmp[18] + "," + tmp[19] + "," + tmp[20] + "," + tmp[21] + "," + tmp[22]);
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
    public static void calcShelterDamage(File inFile1, File inFile2, File inFile3, File inFile4, File outFile, File shelterDir) {
        try(BufferedReader brIn1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
            BufferedReader brIn2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
            BufferedReader brIn3 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile3), "Shift_JIS"));
            BufferedReader brIn4 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile4), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"));
            PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream((new File(shelterDir.getPath() + "/shelter_location.csv")), false), "Shift_JIS"));
            PrintWriter pw3 = new PrintWriter(new OutputStreamWriter(new FileOutputStream((new File(shelterDir.getPath() + "/shelter_list.csv")), false), "Shift_JIS"))) {

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
                    pw.write(pair[2] + "," + pair[7] + "," + pair[4] + "," + pair[6] + "," + pair[8] + ",si,evacuee");
                    pw2.write(pair[2] + "," + pair[9] + "," + pair[10]);
                    pw3.write(pair[2] + "," + pair[7] + "," + pair[4] + "," + pair[6] + "," + pair[8]);
                    midashi = false;
                }
                else if(shelterDB.containsKey(pair[2])) {
                    pw2.write("\n" + pair[2] + "," + pair[9] + "," + pair[10]);
                    pw3.write("\n" + pair[2] + "," + pair[7] + "," + pair[4] + "," + pair[6] + "," + pair[8]);
                    if(siDB.get(pair[20])==null) {
                        pw.write("\n" + pair[2] + "," + pair[7] + "," + pair[4] + "," + pair[6] + "," + pair[8] + "," + "0.0" + "," + shelterDB.get(pair[2]));
                    }
                    else {
                        pw.write("\n" + pair[2] + "," + pair[7] + "," + pair[4] + "," + pair[6] + "," + pair[8] + "," + siDB.get(pair[20]) + "," + shelterDB.get(pair[2]));
                    }
                }
                line3 = brIn3.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void calcHospitalDamage(File meshDamageFile, File nearHospitalFile, File hospitalsFile, File outFile1, File outFile2) {
        try(
            BufferedReader brMeshDamage = new BufferedReader(new InputStreamReader(new FileInputStream(meshDamageFile), "Shift_JIS"));
            BufferedReader brNearHospital = new BufferedReader(new InputStreamReader(new FileInputStream(nearHospitalFile), "Shift_JIS"));
            BufferedReader brHospitals = new BufferedReader(new InputStreamReader(new FileInputStream(hospitalsFile), "Shift_JIS"));
            PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile1, false), "Shift_JIS"));
            PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile2, false), "Shift_JIS"))
        ) {

            // 震度DBの作成
            HashMap<String, Double> siDB = new HashMap<>(); // mesh5thがキー、震度が値
            HashMap<String, Double> meshDB1 = new HashMap<>(); // mesh5thがキー、患者数が値
            HashMap<String, Double> meshDB2 = new HashMap<>(); // mesh5thがキー、重症患者数が値
            String line1 = brMeshDamage.readLine(); // 1行目は見出し
            while((line1 = brMeshDamage.readLine())!=null) {
                String pair[] = line1.split(",");
                if(pair[1].equals("nan")) {
                    siDB.put(pair[0], 0.0);
                    meshDB1.put(pair[0], 0.0);
                    meshDB2.put(pair[0], 0.0);
                }
                else {
                    siDB.put(pair[0], Double.parseDouble(pair[1]));
                    meshDB1.put(pair[0], Double.parseDouble(pair[5]));
                    meshDB2.put(pair[0], Double.parseDouble(pair[6]));
                }
            }

            // 5次メッシュと病院DBの作成
            String line2 = brNearHospital.readLine(); // 1行目は見出し
            HashMap<String, String> meshDB3 = new HashMap<>(); // mesh5thがキー、病院コードが値
            while((line2=brNearHospital.readLine())!=null) {
                String pair[] = line2.split(",");
                meshDB3.put(pair[0],pair[1]);
            }

            // 病院DBの作成
            HashMap<String, String> hospitalDB1 = new HashMap<>();
            ArrayList<String> hospitalList = new ArrayList<>();
            String line3;
            while((line3=brHospitals.readLine())!=null) {
                String pair[] = line3.split(",");
                hospitalDB1.put(pair[1],line3);
                hospitalList.add(pair[1]);
            }

            HashMap<String, Double> hospitalDB2 = new HashMap<>(); // 患者の集計
            HashMap<String, Double> hospitalDB3 = new HashMap<>(); // 重症患者の集計
            // 患者推計
            for(String key : meshDB1.keySet()) {
                String hospitalKey = meshDB3.get(key);
                double d = 0;
                if (hospitalDB2.containsKey(hospitalKey)) {
                    d = hospitalDB2.get(hospitalKey);
                }
                d = d + meshDB1.get(key);
                hospitalDB2.put(hospitalKey, d);
            }
            // 重症患者推計
            for(String key : meshDB2.keySet()) {
                String hospitalKey = meshDB3.get(key);
                double d = 0;
                if (hospitalDB3.containsKey(hospitalKey)) {
                    d = hospitalDB3.get(hospitalKey);
                }
                d = d + meshDB2.get(key);
                hospitalDB3.put(hospitalKey, d);
            }

            // ファイル出力
            boolean midashi = true;

            for(int i=0; i<hospitalList.size(); i++) {
                String code = hospitalList.get(i);
                String lineL = hospitalDB1.get(code);
                String lineS = code;
                String pair[] = lineL.split(",");
                double si = 0;
                if(siDB.containsKey(pair[19])) {
                    si = siDB.get(pair[19]).doubleValue();
                }

                if(midashi) {
                    pw1.write(lineL + ",si,num_injured,num_severe");
                    pw2.write(lineS + ",name,prefecture,sikuchoson,address,saigai,kyukyu,hibaku,dmat,marea_name,si,num_injured,num_severe");
                    midashi = false;
                }
                else {
                    lineL = lineL + "," + si;
                    lineS = lineS + "," + pair[2] + "," + pair[6] + "," + pair[8] + "," + pair[10] + "," + pair[13] + "," + pair[14] + "," + pair[15] + "," + pair[16] +  "," +  pair[18] + "," + si;
                    if(hospitalDB2.containsKey(code)) {
                        lineL = lineL + "," + hospitalDB2.get(code);
                        lineS = lineS + "," + hospitalDB2.get(code);
                    }
                    else {
                        lineL = lineL + ",0.0";
                        lineS = lineS + ",0.0";
                    }

                    if(hospitalDB3.containsKey(code)) {
                        lineL = lineL + "," + hospitalDB3.get(code);
                        lineS = lineS + "," + hospitalDB3.get(code);
                    }
                    else {
                        lineL = lineL + ",0.0";
                        lineS = lineS + ",0.0";
                    }
                    pw1.write("\n" + lineL);
                    pw2.write("\n" + lineS);
                }
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
}
