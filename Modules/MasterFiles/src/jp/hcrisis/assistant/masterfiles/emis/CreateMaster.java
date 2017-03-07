package jp.hcrisis.assistant.masterfiles.emis;

import com.google.code.geocoder.model.GeocoderResult;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import gis.CreateShape;
import gis.GeoCoding;
import gis.MeshMap;
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
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manab on 2016/07/22.
 */
public class CreateMaster {

    private static File outDir = null;
    private static File emisMedicalInstituteMaster = null;
    private static File emisMedicalInstituteInfoMaster = null;
    private static File municipalitiesShapeFile = null;
    private static String municipalitiesShapeFileEncode = null;
    private static File medicalAreaShapeFile = null;
    private static String medicalAreaShapeFileEncode = null;
    private static File meshShapeFileDir = null;
    private static String meshShapeFileDirEncoding = null;

    /**
     * EMISから提供された医療機関csvを用いてH-CRISIS用のマスターファイルを作成するプログラム
     * @param args
     */
    public static void main(String args[]) {

        if(args.length==1) { // 引数1つの場合
            outDir = new File("files/OutFiles/EmisOutFiles/");
            emisMedicalInstituteMaster = new File("files/MasterFiles/EmisMasterFiles/20160729_emis_master.csv");
            emisMedicalInstituteInfoMaster = new File("files/MasterFiles/EmisMasterFiles/20160729_saigai_kihon.csv");
            municipalitiesShapeFile = new File("files/shape/municipalities/municipalities.shp");
            municipalitiesShapeFileEncode = "UTF-8";
            medicalAreaShapeFile = new File("files/shape/medical_area/medical_area.shp");
            medicalAreaShapeFileEncode = "UTF-8";
            meshShapeFileDir = new File("files/shape/mesh");
            meshShapeFileDirEncoding = "UTF-8";
        } else if(args.length==9) { // 引数でファイルが指定された場合
            outDir = new File(args[0]);
            emisMedicalInstituteMaster = new File(args[1]);
            emisMedicalInstituteInfoMaster = new File(args[2]);
            municipalitiesShapeFile = new File(args[3]);
            municipalitiesShapeFileEncode = args[4];
            medicalAreaShapeFile = new File(args[5]);
            medicalAreaShapeFileEncode = args[6];
            meshShapeFileDir = new File(args[7]);
            meshShapeFileDirEncoding = args[8];
        } else {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 引数の数が違います");
            System.exit(1);
        }

        // ファイルとフォルダのチェック
        if(!outDir.exists()) { // 出力フォルダが存在しない時の処理
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 出力フォルダが存在しません。" + outDir.getPath() + "/" + outDir.getName());
            System.exit(1);
        }
        else if(!emisMedicalInstituteMaster.exists()) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 入力ファイルが存在しません。" + emisMedicalInstituteMaster.getPath() + "/" + emisMedicalInstituteMaster.getName());
            System.exit(1);
        } else if(!emisMedicalInstituteMaster.exists()) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 入力ファイルが存在しません。" + emisMedicalInstituteInfoMaster.getPath() + "/" + emisMedicalInstituteInfoMaster.getName());
            System.exit(1);
        } else if(!municipalitiesShapeFile.exists()) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 入力ファイルが存在しません。" + municipalitiesShapeFile.getPath() + "/" + municipalitiesShapeFile.getName());
            System.exit(1);
        } else if(!medicalAreaShapeFile.exists()) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 入力ファイルが存在しません。" + medicalAreaShapeFile.getPath() + "/" + medicalAreaShapeFile.getName());
            System.exit(1);
        } else if(!meshShapeFileDir.exists()) { // 出力フォルダが存在しない時の処理
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 出力フォルダが存在しません。" + meshShapeFileDir.getPath() + "/" + meshShapeFileDir.getName());
            System.exit(1);
        }

        //createMedicalInstituteMaster(outDir, emisMedicalInstituteMaster, emisMedicalInstituteInfoMaster);
        updateCities2MedicalInstiuteMaster(outDir, municipalitiesShapeFile, municipalitiesShapeFileEncode);
        //updateMedicalArea2MedicalInstiuteMaster(outDir, medicalAreaShapeFile, medicalAreaShapeFileEncode);
        //updateMesh2MedicalInstiuteMaster(outDir, meshShapeFileDir, meshShapeFileDirEncoding);
        //createMedicalInstituteMasterShape(outDir, "Shift_JIS");
    }

    /**
     * EMISから取得した全医療機関マスターファイルをHCRISIS用に変換する
     * @param outDir 出力フォルダ
     * @param emisMedicalInstituteMasterFile    EMIS全医療機関マスターファイル（SHIFTJIS）
     * @param emisMedicalInstituteInfoMasterFile EMIS全医療機関基本情報マスターファイル（SHIFTJIS）
     */
    public static void createMedicalInstituteMaster(File outDir, File emisMedicalInstituteMasterFile, File emisMedicalInstituteInfoMasterFile) {
        System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: H-CRISIS版EMIS全医療機関マスターファイルを作成します。");

        // 出力ファイルの作成
        File hcrisisMedicalInstituteMasterFile = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master.csv");
        File hcrisisMedicalInstituteMasterFileFull = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master_full.csv");

        // EMISのマスターファイルを読む
        try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(emisMedicalInstituteMasterFile), "Shift_JIS"));
             BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(emisMedicalInstituteInfoMasterFile), "Shift_JIS"));
             PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"));
             PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))) {

            // 見出しの書き込み
            pw1.write("hcode,ecode,name1,name2,pref_code,city_code,pref_name,gun_name,city_name,post_code,address,lat,lon,saigai,kyukyu,hibaku,dmat");
            pw2.write("hcode,ecode,name1,name2,pref_code,city_code,pref_name,gun_name,city_name,post_code,address,lat,lon,saigai,kyukyu,hibaku,dmat," +
                    "TEL,FAX,施設管理者,担当者所属,担当者名,EMIS入力担当者1,EMIS入力担当者2,開設者種別,事業区分,事業区分:指定年度,救急医療体制の参加:初期,救急医療体制の参加:2次,救急医療体制の参加:救命救急センター," +
                    "ドクターヘリ基地病院の指定,DMAT指定,DMAT指定:指定年度,DMATチーム数,DMATチーム数:医師数,DMATチーム数:看護師数,DMATチーム数:業務調整員数," +
                    "勤務医師数:医師総数,勤務医師数:救急科医師数（救急専従医）,病棟情報:許可病床数,病棟情報:実働病床数,病棟情報:ＩＣＵ病床数,病棟情報:手術室数,病棟情報:結核病床数,病棟情報:感染症病床数," +
                    "年間救急患者数:年間救急外来患者数,年間救急患者数:年間受け入れ救急車数,年間救急患者数:年間緊急入院患者数,年間救急患者数:三次救急患者数," +
                    "標榜科目情報:救急科,標榜科目情報:呼吸器科,標榜科目情報:消化器科（胃腸科）,標榜科目情報:循環器科,標榜科目情報:小児科,標榜科目情報:精神科,標榜科目情報:神経科（神経内科）,標榜科目情報:外科," +
                    "標榜科目情報:整形外科,標榜科目情報:形成外科,標榜科目情報:脳神経外科,標榜科目情報:心臓血管外科,標榜科目情報:産婦人科（産科、婦人科）,標榜科目情報:眼科,標榜科目情報:耳鼻咽喉科," +
                    "標榜科目情報:皮膚科,標榜科目情報:泌尿器科,標榜科目情報:放射線科,標榜科目情報:麻酔科,標榜科目情報:歯科,標榜科目情報:内科,標榜科目情報:その他,標榜科目情報:備考");
            String line1 = br1.readLine(); // 1行目は見出し
            String line2 = br2.readLine(); // 1、2行目は見出し
            line2 = br2.readLine();

            //医療機関の基本情報をデータベース化
            HashMap<String, String> medicalInstituteBaseDB = new HashMap<>();
            while((line2 = br2.readLine()) != null) {
                String pair[] = line2.split(",");
                String str = "";
                for(int i=10; i<pair.length; i++) {
                    str = str + "," + pair[i];
                }
                medicalInstituteBaseDB.put("e" + pair[1], str);
            }

            while ((line1 = br1.readLine()) != null) {
                String pair[] = line1.split(",");
                String[] str = {"h0000000000", "e000000000000", "自動入力医療機関名1", "", "00", "00000", "都道府県", "郡", "市区町村", "0000000", "住所未入力",
                        "0.00", "0.00", "0", "0", "0", "0"};

                if (pair.length >= 1) { // 項目1(都道府県コード)の変換
                    if(pair[0].length()==1) {
                        str[4] = "0" + pair[0];
                    }
                    else {
                        str[4] = pair[0];
                    }
                }

                if (pair.length >= 2) { // 項目2(都道府県名)の変換
                    str[6] = pair[1];
                }

                if (pair.length >= 3) { // 項目3(EMIS医療機関コード 都道府県でユニークなので都道府県コードを追加)の変換
                    str[1] = "e" + str[4] + pair[2];
                }

                if (pair.length >= 4) { // 項目4(医療機関名)の変換
                    str[2] = pair[3];
                }

                if (pair.length >= 5) // 項目5の変換
                    str[3] = pair[4];

                if (pair.length >= 6) // 項目6の変換
                    str[9] = pair[5];

                if (pair.length >= 7) // 項目7の変換
                    str[10] = pair[6];

                if (pair.length >= 10) { // 項目10の変換
                    if (pair[9].equals("災拠"))
                        str[13] = "1";
                }

                if (pair.length >= 11) { // 項目11の変換
                    if (pair[10].equals("救命"))
                        str[14] = "1";
                }

                if (pair.length >= 12) { // 項目12の変換
                    if (pair[11].equals("被ばく"))
                        str[15] = "1";
                }

                if (pair.length >= 13) { // 項目13の変換
                    if (pair[12].equals("DMAT"))
                        str[16] = "1";
                }

                if (pair.length >= 14) // 項目14の変換
                    str[11] = pair[13];

                if (pair.length >= 15) // 項目15の変換
                    str[12] = pair[14];

                if (str[11].equals("0.00") || str[12].equals("0.00") || str[11].equals("") || str[12].equals("")) { // 緯度経度がない医療機関はGoogleでGeoCoding
                    //System.out.print("\t" + str[2]);
                    List<GeocoderResult> results = null;
                    results = GeoCoding.getGeocoderResult(str[6] + " " + str[2], "jp");
                    if(results.size()<1)
                        results = GeoCoding.getGeocoderResult(str[10], "jp");

                    if (results.size() > 0) {
                        GeocoderResult result = results.get(0);
                        str[11] = result.getGeometry().getLocation().getLat().toString();
                        str[12] = result.getGeometry().getLocation().getLng().toString();
                    }
                }


                String write = str[0];
                for (int j = 1; j < str.length; j++)
                    write = write + "," + str[j];

                pw1.write("\n" + write);
                pw2.write("\n" + write + medicalInstituteBaseDB.get(str[1]));
            }
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: HCRISIS全医療機関マスターファイルを作成しました。");

        } catch (UnsupportedEncodingException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 文字コードはサポートしていません。");
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: ファイルが見つかりません。");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: ファイルを読み込み中にエラーが生じました。");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * HCRISIS全医療機関マスターファイルに市区町村情報を追加する
     * @param outDir 作業フォルダ
     */
    public static void updateCities2MedicalInstiuteMaster(File outDir, File municipalitiesShapeFile, String municipalitiesShapeFileEncode) {
        System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: HCRISIS全医療機関マスターファイルに市区町村情報を追加します。");

        File hcrisisMedicalInstituteMasterFile = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master.csv");
        File hcrisisMedicalInstituteMasterFileFull = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master_full.csv");

        LinkedList<String> list = new LinkedList<>(); // 簡易マスター
        HashMap<String, String> map = new HashMap<>(); // フルマスター

        // HCRISISの簡易マスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                list.addLast(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // HCRISISのフルマスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                map.put(pair[1], line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"));
             PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))) {

            pw1.write(list.get(0));
            pw2.write(map.get("ecode"));

            // 各医療機関がどの市区町村に所属するかを判別する
            // Shapeファイルを指定する
            ShapefileDataStore store = new ShapefileDataStore(municipalitiesShapeFile.toURI().toURL());
            store.setCharset(Charset.forName(municipalitiesShapeFileEncode));
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection c = featureSource.getFeatures();

            int count = 0;
            int counter = 0;
            for (int i = 1; i < list.size(); i++) {
                String pair[] = list.get(i).split(",");
                double lat = Double.parseDouble(pair[11]); // 緯度
                double lon = Double.parseDouble(pair[12]); // 経度

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));

                FeatureIterator iterator = c.features();
                while (iterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    if (point.within((MultiPolygon) feature.getAttribute("the_geom")) || point.touches((MultiPolygon) feature.getAttribute("the_geom"))) {
                        String code = feature.getAttribute("code").toString();
                        String prefcture = feature.getAttribute("prefecture").toString();
                        String gun = feature.getAttribute("name1").toString();
                        String sikuchoson = feature.getAttribute("name2").toString();
                        if(gun.endsWith("市")) {
                            gun = "";
                            sikuchoson = feature.getAttribute("name1").toString() + feature.getAttribute("name2").toString();
                        }
                        else if(gun.endsWith("区")) {
                            gun = "";
                            sikuchoson = feature.getAttribute("name1").toString();
                        }
                        else if(gun.endsWith("郡")) {
                            sikuchoson = feature.getAttribute("name2").toString();
                        }

                        pair[5] = code;
                        pair[6] = prefcture;
                        pair[7] = gun;
                        pair[8] = sikuchoson;
                        break;
                    }
                }
                iterator.close();

                // 市区町村が見つからなかったときの処理
                if(pair[8].equals("市区町村")) {
                    try {
                        List<GeocoderResult> results = null;
                        results = GeoCoding.getGeocoderResult(pair[6] + " " + pair[2], "jp");
                        if (results.size() < 1)
                            results = GeoCoding.getGeocoderResult(pair[10], "jp");

                        if (results.size() > 0) {
                            GeocoderResult result = results.get(0);
                            pair[11] = result.getGeometry().getLocation().getLat().toString();
                            pair[12] = result.getGeometry().getLocation().getLng().toString();
                        }
                        FeatureIterator iterator2 = c.features();
                        while (iterator2.hasNext()) {
                            SimpleFeature feature = (SimpleFeature) iterator2.next();
                            if (point.within((MultiPolygon) feature.getAttribute("the_geom")) || point.touches((MultiPolygon) feature.getAttribute("the_geom"))) {
                                String code = feature.getAttribute("code").toString();
                                String prefcture = feature.getAttribute("prefecture").toString();
                                String gun = feature.getAttribute("name1").toString();
                                String sikuchoson = "";
                                if (gun.endsWith("市")) {
                                    gun = "";
                                    sikuchoson = feature.getAttribute("name1").toString() + feature.getAttribute("name2").toString();
                                } else if (gun.endsWith("区")) {
                                    gun = "";
                                    sikuchoson = feature.getAttribute("name1").toString();
                                } else if (gun.endsWith("郡")) {
                                    sikuchoson = feature.getAttribute("name2").toString();
                                }

                                pair[5] = code;
                                pair[6] = prefcture;
                                pair[7] = gun;
                                pair[8] = sikuchoson;
                                break;
                            }
                        }
                        iterator2.close();
                    }
                    catch(Exception e) {
                        System.out.println(list);
                        System.exit(1);
                    }
                }

                // 住所から市区町村を取り除く
                if(pair[10].startsWith(pair[7] + pair[8])) {
                    pair[10] = pair[10].substring((pair[7]+pair[8]).length());
                }

                String write1 = pair[0];
                for (int j = 1; j < pair.length; j++) {
                    write1 = write1 + "," + pair[j];
                }
                pw1.write("\n" + write1);

                String write2[] = map.get(pair[1]).split(",");
                for(int j=17; j<write2.length; j++) {
                    write1 = write1 + "," + write2[j];
                }
                pw2.write("\n" + write1);

                count++;
                if(count>(list.size()/100)) {
                    counter++;
                    System.out.println("\tjp.hcrisis.assistant.masterfiles.emis.CreateMaster:" + counter + " %完了しました( " + list.size() + " 中)。");
                    count = 0;
                }
            }
            store.dispose();

        } catch (FileNotFoundException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: ファイルが見つかりません。");
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 市区町村行政Shapeファイルが指定した文字コードをサポートしていません。");
            e.printStackTrace();
            System.exit(1);
        } catch (MalformedURLException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: 市区町村行政Shapeファイルを開いている際にエラーが生じました。");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: HCRISIS全医療機関マスターファイルに市区町村情報を追加しました。");
    }

    /**
     * HCRISIS全医療機関マスターファイルに医療圏情報を追加する
     * @param outDir
     * @param medicalAreaShapeFile
     * @param medicalAreaShapeFileEncode
     */
    public static void updateMedicalArea2MedicalInstiuteMaster(File outDir, File medicalAreaShapeFile, String medicalAreaShapeFileEncode) {
        System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: HCRISIS全医療機関マスターファイルに医療圏情報を追加します。");

        File hcrisisMedicalInstituteMasterFile = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master.csv");
        File hcrisisMedicalInstituteMasterFileFull = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master_full.csv");

        LinkedList<String> list = new LinkedList<>(); // 簡易マスター
        HashMap<String, String> map = new HashMap<>(); // フルマスター

        // HCRISISの簡易マスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                list.addLast(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // HCRISISのフルマスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                map.put(pair[1], line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"));
             PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))) {

            // 見出しの書き込み
            pw1.write(list.get(0) + ",marea_code,marea_name");
            String write2 = list.get(0) + ",marea_code,marea_name";
            String pairWrite2[] = map.get("ecode").split(",");
            for(int i=17; i<pairWrite2.length; i++) {
                write2 = write2 + "," + pairWrite2[i];
            }
            pw2.write(write2);

            // 各医療機関がどの医療圏に所属するかを判別する
            // Shapeファイルを指定する
            ShapefileDataStore store = new ShapefileDataStore(medicalAreaShapeFile.toURI().toURL());
            store.setCharset(Charset.forName(medicalAreaShapeFileEncode));
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection c = featureSource.getFeatures();

            int count = 0;
            int counter = 0;

            for (int i = 1; i < list.size(); i++) {
                String pair[] = list.get(i).split(",");
                double lat = Double.parseDouble(pair[11]); // 緯度
                double lon = Double.parseDouble(pair[12]); // 経度

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));

                FeatureIterator iterator = c.features();
                String code = ""; // 医療圏コード
                String name = ""; // 医療圏名
                while (iterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    if (point.within((MultiPolygon) feature.getAttribute("the_geom"))) {
                        code = feature.getAttribute("code").toString();
                        name = feature.getAttribute("name").toString();
                        break;
                    }
                }
                iterator.close();

                String write1 = list.get(i);
                write1 = write1 + "," + code + "," + name;

                pw1.write("\n" + write1);

                write2 = write1;
                pairWrite2 = map.get(pair[1]).split(",");
                for(int j=17; j<pairWrite2.length; j++) {
                    write2 = write2 + "," + pairWrite2[j];
                }
                pw2.write("\n" + write2);

                count++;
                if(count>(list.size()/100)) {
                    counter++;
                    System.out.println("\tjp.hcrisis.assistant.masterfiles.emis.CreateMaster: " + counter + " %完了しました( " + list.size() + " 中)。");
                    count = 0;
                }
            }
            store.dispose();
        } catch (FileNotFoundException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: ファイルが見つかりません。");
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: Shapeファイルが指定した文字コードをサポートしていません。");
            e.printStackTrace();
            System.exit(1);
        } catch (MalformedURLException e) {
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster Error: Shapeファイルを開いている際にエラーが生じました。");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: HCRISIS全医療機関マスターファイルに医療圏情報を追加しました。");
    }

    /**
     * HCRISIS全医療機関マスターファイルにメッシュ情報を追加する
     * @param outDir
     * @param meshShapeFileDir
     * @param meshShapeFileDirEncoding
     */
    public static void updateMesh2MedicalInstiuteMaster(File outDir, File meshShapeFileDir, String meshShapeFileDirEncoding) {
        System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: HCRISIS全医療機関マスターファイルにメッシュ情報を追加します。");
        MeshMap mm = new MeshMap(meshShapeFileDir, meshShapeFileDirEncoding);

        System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster: メッシュDBの作成が完了しました。");

        File hcrisisMedicalInstituteMasterFile = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master.csv");
        File hcrisisMedicalInstituteMasterFileFull = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master_full.csv");

        LinkedList<String> list = new LinkedList<>(); // 簡易マスター
        HashMap<String, String> map = new HashMap<>(); // フルマスター

        // HCRISISの簡易マスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                list.addLast(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // HCRISISのフルマスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                map.put(pair[1], line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // HCRISISのマスターファイルを読む
        try (PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"));
             PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))){

            // 見出しの書き込み
            pw1.write(list.get(0) + "," + "mesh5th");
            String write2 = list.get(0) + ",mesh5th";
            String pairWrite2[] = map.get("ecode").split(",");
            for(int i=19; i<pairWrite2.length; i++) {
                write2 = write2 + "," + pairWrite2[i];
            }
            pw2.write(write2);

            int count = 0;
            int counter = 0;
            for(int i=1; i<list.size(); i++) {
                String pair[] = list.get(i).split(",");
                double lat = Double.parseDouble(pair[11]); // 緯度
                double lon = Double.parseDouble(pair[12]); // 経度

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));

                LinkedList<String> meshIDs = mm.getMeshIDs(5, point);
                if(meshIDs.size()==5) {
                    pw1.write("\n" + list.get(i) + "," + meshIDs.get(4));
                    write2 = list.get(i) + "," + meshIDs.get(4);
                } else {
                    pw1.write("\n" + list.get(i) + ",");
                    write2 = list.get(i) + ",";
                }


                pairWrite2 = map.get(pair[1]).split(",");
                for(int j=19; j<pairWrite2.length; j++) {
                    write2 = write2 + "," + pairWrite2[j];
                }
                pw2.write("\n" + write2);

                count++;
                if(count>(list.size()/100)) {
                    counter++;
                    System.out.println("\tjp.hcrisis.assistant.masterfiles.emis.CreateMaster: " + counter + " %完了しました( " + list.size() + " 中)。");
                    count = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createMedicalInstituteMasterShape(File outDir, String shapeFileEncoding) {
        File hcrisisMedicalInstituteMasterFileFull = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master_full.csv");

        String name = "EMIS Medical Institute";
        String geom = "the_geom:Point:srid=4326,";

        String text = "code:String,name:String,pref:String,gun:String,city:String,address:String,saigai:int,kyukyu:int,hibaku:int,dmat:int," +
                "hd01:int,hd02:int,hd03:int,hd04:int,hd05:int,hd06:int,hd07:int,hd08:int,hd09:int," +
                "hd10:int,hd11:int,hd12:int,hd13:int,hd14:int,hd15:int,hd16:int,hd17:int,hd18:int,hd19:int,hd20:int," +
                "hd21:int,hd22:int,rd:String";
        String text1 = text;
        String text2 = text1 + ",mds:String,e001:String,e002:String,e003:String,e004:String,e005:String,e006:String,e007:String,clr:int";

        // FeatureTypeを生成する。
        SimpleFeatureType type1 = null;
        SimpleFeatureType type2 = null;

        try {
            type1 = DataUtilities.createType(name, geom + text1);
            type2 = DataUtilities.createType(name, geom + text2);
        } catch (SchemaException e) {
            e.printStackTrace();
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster.createMedicalInstituteMasterShape Error: FeatureTypeの生成エラー");
            System.exit(1);
        }

        // Featureを生成する準備
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder1 = new SimpleFeatureBuilder(type1);
        SimpleFeatureBuilder featureBuilder2 = new SimpleFeatureBuilder(type2);

        List<SimpleFeature> features1 = new ArrayList<SimpleFeature>();
        List<SimpleFeature> features2 = new ArrayList<SimpleFeature>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMasterFileFull), "Shift_JIS"))) {
            String line = br.readLine(); // 1行目は見出し
            while((line = br.readLine())!=null) {
                String pair[] = line.split(",");
                double lat = Double.parseDouble(pair[11]); // 緯度
                double lon = Double.parseDouble(pair[12]); // 経度
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
                featureBuilder1.add(point);
                featureBuilder2.add(point);

                int index[] = {1,2,6,7,8,10,13,14,15,16,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73};
                for(int i : index) {
                    featureBuilder1.add(pair[i]);
                    featureBuilder2.add(pair[i]);
                }
                SimpleFeature feature1 = featureBuilder1.buildFeature(null);
                SimpleFeature feature2 = featureBuilder2.buildFeature(null);
                features1.add(feature1);
                features2.add(feature2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster.createMedicalInstituteMasterShape Error: 読み込みファイルの生成エラー");
            System.exit(1);
        }

        File outFile1 = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master_full_general.shp");
        File outFile2 = new File(outDir.getPath() + "/h-crisis_emis_medical_institute_master_full_crisis.shp");

        try {
            CreateShape.createShapeFile(outFile1, shapeFileEncoding, type1, features1);
            features1.clear();

            CreateShape.createShapeFile(outFile2, shapeFileEncoding, type2, features2);
            features2.clear();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("jp.hcrisis.assistant.masterfiles.emis.CreateMaster.createMedicalInstituteMasterShape Error: 出力ファイルの生成エラー");
            System.exit(1);
        }
    }
}
