package ichikawa.assistant.emis;

import com.google.code.geocoder.model.GeocoderResult;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import ichikawa.common.GeoCoding;
import ichikawa.common.MeshMap;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manab on 2016/07/22.
 */
public class CreateMaster {
    public static void main(String args[]) {

        File emisMedicalInstituteMaster = new File("files/emis/emis_medical_institute_master_20160729.csv");
        File hcrisisMedicalInstituteMaster = new File("files/hcrisis/hcrisis_medical_institute_master_20160729.csv");
        File hcrisisMedicalInstituteMasterWithMesh = new File("files/hcrisis/hcrisis_medical_institute_master_mesh_20160729.csv");

        //createMedicalInstituteMaster(emisMedicalInstituteMaster, hcrisisMedicalInstituteMaster);
        //updateCities2MedicalInstiuteMaster(hcrisisMedicalInstituteMaster);
        updateMedicalArea2MedicalInstiuteMaster(hcrisisMedicalInstituteMaster);
        //updateMesh2MedicalInstiuteMaster(hcrisisMedicalInstituteMaster, hcrisisMedicalInstituteMasterWithMesh, 5);
    }

    /**
     * EMISから取得した全医療機関マスターファイルをHCRISIS用に変換する
     *
     * @param emisMedicalInstituteMasterFile    EMIS全医療機関マスターファイル（SHIFTJIS）
     * @param hcrisisMedicalInstituteMasterFile HCRISIS全医療機関マスターファイル（SHIFTJIS）
     */
    public static void createMedicalInstituteMaster(File emisMedicalInstituteMasterFile, File hcrisisMedicalInstituteMasterFile) {
        System.out.println("EMIS全医療機関マスターファイルを作成します。");

        // EMISのマスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(emisMedicalInstituteMasterFile), "Shift_JIS"));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterFile), "Shift_JIS"))) {
            pw.write("hcode,ecode,name1,name2,pref_code,city_code,pref_name,city_name,post_code,address,lat,lon,saigai,kyukyu,hibaku,dmat");
            String line = br.readLine(); // 1行目は見出し
            while ((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                String[] str = {"h0000000000", "e0000000000", "自動入力医療機関名1", "", "00", "00000", "都道府県", "市区町村", "0000000", "住所未入力",
                        "0.00", "0.00", "0", "0", "0", "0"};

                if (pair.length >= 1) // 項目1の変換
                    str[4] = pair[0];

                if (pair.length >= 2) // 項目2の変換
                    str[6] = pair[1];

                if (pair.length >= 3) // 項目3の変換
                    str[1] = "e" + pair[2];

                if (pair.length >= 4) // 項目4の変換
                    str[2] = pair[3];

                if (pair.length >= 5) // 項目5の変換
                    str[3] = pair[4];

                if (pair.length >= 6) // 項目6の変換
                    str[8] = pair[5];

                if (pair.length >= 7) // 項目7の変換
                    str[9] = pair[6];

                if (pair.length >= 10) { // 項目10の変換
                    if (pair[9].equals("災拠"))
                        str[12] = "1";
                }

                if (pair.length >= 11) { // 項目11の変換
                    if (pair[10].equals("救命"))
                        str[13] = "1";
                }

                if (pair.length >= 12) { // 項目12の変換
                    if (pair[11].equals("被ばく"))
                        str[14] = "1";
                }

                if (pair.length >= 13) { // 項目13の変換
                    if (pair[12].equals("DMAT"))
                        str[15] = "1";
                }

                if (pair.length >= 14) // 項目14の変換
                    str[10] = pair[13];

                if (pair.length >= 15) // 項目15の変換
                    str[11] = pair[14];

                if (str[10].equals("0.00") || str[11].equals("0.00") || str[10].equals("") || str[11].equals("")) { // 緯度経度がない医療機関はGoogleでGeoCoding
                    System.out.print("\t" + str[2]);
                    List<GeocoderResult> results = null;
                    results = GeoCoding.getGeocoderResult(str[6] + " " + str[2], "jp");
                    if(results.size()<1)
                        results = GeoCoding.getGeocoderResult(str[9], "jp");

                    if (results.size() > 0) {
                        GeocoderResult result = results.get(0);
                        str[10] = result.getGeometry().getLocation().getLat().toString();
                        str[11] = result.getGeometry().getLocation().getLng().toString();
                        System.out.println(" " + str[10] + " " + str[11]);
                    }
                }


                String write = str[0];
                for (int j = 1; j < str.length; j++)
                    write = write + "," + str[j];

                pw.write("\n" + write);
            }
            System.out.println("HCRISIS全医療機関マスターファイルを作成しました。");

        } catch (UnsupportedEncodingException e) {
            System.out.println("EMIS全医療機関マスターファイルの文字コードはサポートしていません。");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("EMIS全医療機関マスターファイルが見つかりません。");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("EMIS全医療機関マスターファイルを読み込み中にエラーが生じました。");
            e.printStackTrace();
        }
    }

    public static void updateCities2MedicalInstiuteMaster(File hcrisisMedicalInstituteMaster) {
        System.out.println("HCRISIS全医療機関マスターファイルに市区町村情報を追加します。");
        LinkedList<String> list = new LinkedList<String>();

        // HCRISISのマスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMaster), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                list.addLast(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMaster), "Shift_JIS"))) {
            // 各医療機関がどの市区町村に所属するかを判別する
            // Shapeファイルを指定する
            File file = new File("files/shape/municipalities/municipalities.shp");
            ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
            store.setCharset(Charset.forName("UTF-8"));
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection c = featureSource.getFeatures();

            int count = 0;
            int counter = 0;
            pw.write(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                String pair[] = list.get(i).split(",");
                double lat = Double.parseDouble(pair[10]); // 緯度
                double lon = Double.parseDouble(pair[11]); // 経度

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));

                FeatureIterator iterator = c.features();
                while (iterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    if (point.within((MultiPolygon) feature.getAttribute("the_geom")) || point.touches((MultiPolygon) feature.getAttribute("the_geom"))) {
                        pair[5] = feature.getAttribute("code").toString();
                        pair[7] = feature.getAttribute("name2").toString();
                        if(pair[7].equals("")) {
                            pair[7] = feature.getAttribute("name1").toString();
                        }
                        iterator.close();
                        break;
                    }
                }

                // 市区町村が見つからなかったときの処理
                if(pair[7].equals("市区町村")) {
                    System.out.print("\t" + pair[2]);
                    List<GeocoderResult> results = null;
                    results = GeoCoding.getGeocoderResult(pair[6] + " " + pair[2], "jp");
                    if(results.size()<1)
                        results = GeoCoding.getGeocoderResult(pair[9], "jp");

                    if (results.size() > 0) {
                        GeocoderResult result = results.get(0);
                        pair[10] = result.getGeometry().getLocation().getLat().toString();
                        pair[11] = result.getGeometry().getLocation().getLng().toString();
                    }
                    FeatureIterator iterator2 = c.features();
                    while (iterator2.hasNext()) {
                        SimpleFeature feature = (SimpleFeature) iterator2.next();
                        if (point.within((MultiPolygon) feature.getAttribute("the_geom")) || point.touches((MultiPolygon) feature.getAttribute("the_geom"))) {
                            pair[5] = feature.getAttribute("code").toString();
                            pair[7] = feature.getAttribute("name2").toString();
                            if(pair[7].equals("")) {
                                pair[7] = feature.getAttribute("name1").toString();
                            }
                            iterator2.close();
                            break;
                        }
                    }
                    System.out.println("\t" + pair[7]);
                }

                String write = pair[0];
                for (int j = 1; j < pair.length; j++)
                    write = write + "," + pair[j];

                pw.write("\n" + write);
                count++;
                if(count>(list.size()/100)) {
                    counter++;
                    System.out.println("\t" + counter + " %完了しました( " + list.size() + " 中)。");
                    count = 0;
                }
            }
            store.dispose();

        } catch (FileNotFoundException e) {
            System.out.println("HCRISIS全医療機関マスターファイルが見つかりません。");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("市区町村行政Shapeファイルが指定した文字コードをサポートしていません。");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("市区町村行政Shapeファイルを開いている際にエラーが生じました。");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HCRISIS全医療機関マスターファイルに市区町村情報を追加しました。");
    }

    public static void updateMedicalArea2MedicalInstiuteMaster(File hcrisisMedicalInstituteMaster) {
        System.out.println("HCRISIS全医療機関マスターファイルに医療圏情報を追加します。");
        LinkedList<String> list = new LinkedList<String>();

        // HCRISISのマスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMaster), "Shift_JIS"))) {
            // 全行をメモリにストック
            String line;
            while ((line = br.readLine()) != null) {
                list.addLast(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(list.size());
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMaster), "Shift_JIS"))) {
            // 各医療機関がどの医療圏に所属するかを判別する
            // Shapeファイルを指定する
            File file = new File("files/shape/medical_area/medical_area.shp");
            ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
            store.setCharset(Charset.forName("UTF-8"));
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection c = featureSource.getFeatures();

            int count = 0;
            int counter = 0;
            pw.write("hcode,ecode,name1,name2,pref_code,marea_code,city_code,pref_name,marea_name,city_name,post_code,address,lat,lon,saigai,kyukyu,hibaku,dmat");
            for (int i = 1; i < list.size(); i++) {
                String pair[] = list.get(i).split(",");
                double lat = Double.parseDouble(pair[10]); // 緯度
                double lon = Double.parseDouble(pair[11]); // 経度

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
                        iterator.close();
                        break;
                    }
                }
                String write = pair[0];
                // 都道府県コードと市区町村コードの間に医療圏コードを入れる
                for (int j = 1; j < 5; j++)
                    write = write + "," + pair[j];
                write = write + "," + code;

                // 都道府県名と市区町村名の間に医療圏名を入れる
                for (int j = 5; j < 7; j++)
                    write = write + "," + pair[j];
                write = write + "," + name;

                for (int j = 7; j < pair.length; j++)
                    write = write + "," + pair[j];

                pw.write("\n" + write);
                count++;
                if(count>(list.size()/100)) {
                    counter++;
                    System.out.println("\t" + counter + " %完了しました( " + list.size() + " 中)。");
                    count = 0;
                }
            }
            store.dispose();
        } catch (FileNotFoundException e) {
            System.out.println("HCRISIS全医療機関マスターファイルが見つかりません。");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("医療圏Shapeファイルが指定した文字コードをサポートしていません。");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("医療圏Shapeファイルを開いている際にエラーが生じました。");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HCRISIS全医療機関マスターファイルに医療圏情報を追加しました。");
    }

    public static void updateMesh2MedicalInstiuteMaster(File hcrisisMedicalInstituteMaster, File hcrisisMedicalInstituteMasterWithMesh, int level) {
        MeshMap mm = new MeshMap(new File("files/shape/mesh"), "utf-8");

        System.out.println("HCRISIS全医療機関マスターファイルにメッシュ情報を追加します。");

        // HCRISISのマスターファイルを読む
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(hcrisisMedicalInstituteMaster), "Shift_JIS")) ;
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(hcrisisMedicalInstituteMasterWithMesh), "Shift_JIS"))){

            String line = br.readLine();
            if(level==1)
                line = line + ",mesh1st";
            else if(level==2)
                line = line + ",mesh1st,mesh2nd";
            else if(level==3)
                line = line + ",mesh1st,mesh2nd,mesh3rd";
            else if(level==4)
                line = line + ",mesh1st,mesh2nd,mesh3rd,mesh4th";
            else if(level==5)
                line = line + ",mesh1st,mesh2nd,mesh3rd,mesh4th,mesh5th";
            pw.write(line);

            while ((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                double lat = Double.parseDouble(pair[12]); // 緯度
                double lon = Double.parseDouble(pair[13]); // 経度

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));

                LinkedList<String> meshIDs = mm.getMeshIDs(level, point);
                for(int i=0; i<meshIDs.size(); i++)
                    line = line + "," + meshIDs.get(i);
                pw.write("\n" + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
