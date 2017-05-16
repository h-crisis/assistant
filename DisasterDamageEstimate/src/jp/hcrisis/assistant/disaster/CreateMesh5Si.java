package jp.hcrisis.assistant.disaster;

import com.vividsolutions.jts.geom.MultiPolygon;
import gis.CreateShape;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 5次メッシュの震度分布CSVを作成するクラス
 * Created by manabu on 2016/09/13.
 */
public class CreateMesh5Si {

    /**
     * SIP4日立から受け取る震度分布データから5次メッシュの震度分布CSVを作成するメソッド
     * @param dir データディレクトリ
     * @param outFile 出力ファイル
     */
    public static void createMesh5SiFromSIP4(File dir, File outFile) {
        File[] files = dir.listFiles(); // データファイルを取得する

        try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {
            pw.print("5次メッシュコード,震度,全壊建物数,全半壊建物数,死者数,重傷者数,負傷者数,避難者数"); // 出力ファイルの見出しを記入する
            for(File file : files) {
                try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"))) {
                    String line = br.readLine(); // 1行目は見出しなので無視
                    while(line != null) {
                        if(line.startsWith("2,")) {
                            String pair[] = line.split(",");
                            System.out.println(pair.length + " " + line);
                            if (pair.length == 8) {
                                pw.print("\n" + pair[7] + "," + pair[5] + ",,,");
                            } else if(pair.length == 9) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + ",,,,,");
                            } else if(pair.length == 10) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + "," + pair[9] + ",,,,");
                            } else if(pair.length == 11) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + "," + pair[9] + "," + pair[10] + ",,,");
                            } else if(pair.length == 12) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + "," + pair[9] + "," + pair[10] + "," + pair[11] + ",,");
                            } else if(pair.length == 13) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + "," + pair[9] + "," + pair[10] + "," + pair[11] + "," + pair[12] + ",");
                            } else if(pair.length == 14) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + "," + pair[9] + "," + pair[10] + "," + pair[11] + "," + pair[12] + "," + pair[13]);
                            } else {
                                System.out.println("項目数が足りていません。");
                                System.exit(1);
                            }
                        }
                        line = br.readLine();
                    }
                } catch (IOException e) {
                    System.out.println("入力ファイル " + file.getName() + " を開いている際にエラーが生じました。");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("出力ファイル " + outFile.getName() + " が見つかりません。");
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.out.println("出力ファイル " + outFile.getName() + " は、文字コード Shift_JISをサポートしていません。");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * JSHISの震度ファイルから5次メッシュの震度分布CSVを作成するメソッド
     * @param inFile 入力ファイル
     * @param outFile 出力ファイル
     */
    public static void createMesh5SiFromJSHIS(File inFile, File outFile) {
        try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {
            pw.print("5次メッシュコード,震度,全壊建物数,全半壊建物数,重傷者数"); // 出力ファイルの見出しを記入する
            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"))) {
                String line = br.readLine(); // 1行目は見出しなので無視
                boolean isRead = false;
                while(line != null) {
                    if(line.startsWith("# CODE")) {
                        isRead = true;
                    }
                    else if(isRead) {
                        String pair[] = line.split(",");
                        if (pair.length != 6) {
                            System.out.println("入力ファイル " + inFile.getName() + " の列数が6ではありません。");
                            System.exit(1);
                        } else {
                            pw.print("\n" + pair[0] + "," + pair[5] + ",-,-,-");
                        }
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                System.out.println("入力ファイル " + inFile.getName() + " を開いている際にエラーが生じました。");
                e.printStackTrace();
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("出力ファイル " + outFile.getName() + " が見つかりません。");
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.out.println("出力ファイル " + outFile.getName() + " は、文字コード Shift_JISをサポートしていません。");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void createMesh5SiShape(File meshFile, File siFile, File dataFile, File outFile) throws Exception {
        HashMap<String, String> siData = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(siFile), "Shift_JIS"))) {
            String line = br.readLine(); // 1行目は見出しなので無視
            while ((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                siData.put(pair[0], line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, String> dataData = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "Shift_JIS"))) {
            String line = br.readLine(); // 1行目は見出しなので無視
            while ((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                dataData.put(pair[0], line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //メッシュファイルを開いて被災地リストを作成する
        ShapefileDataStore fileDataStore = null;
        ArrayList<SimpleFeature> featuresList = new ArrayList<>();
        try {
            fileDataStore = new ShapefileDataStore(meshFile.toURI().toURL());
            fileDataStore.setCharset(Charset.forName("Shift_JIS")); // 文字コードの設定
            SimpleFeatureSource featureSource = fileDataStore.getFeatureSource();
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            FeatureIterator<SimpleFeature> featureI = featureCollection.features();
            featuresList = new ArrayList<SimpleFeature>();
            while (featureI.hasNext()) {
                SimpleFeature feature = featureI.next();
                if (siData.containsKey(feature.getAttribute("MESH5TH").toString())) {
                    featuresList.add(feature);
                }
            }
            featureI.close();
            fileDataStore.dispose();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //被災地5次メッシュを作成する
        String namePolygon = "MESH5TH Damage";
        String geomPolygon = "the_geom:Polygon:srid=4612,";
        SimpleFeatureType typePolygon = DataUtilities.createType(namePolygon, geomPolygon +
                "MESH5TH:string,MESH4TH:string,MESH3RD:string,MESH2ND:string,MESH1ST:string,pop:double,house:double,si_s:string,si_d:double,zenkai:double,zenhankai:double,dead:double,injured:double,severe:double,evacuee:double");
        SimpleFeatureBuilder featureBuilderPolygon = new SimpleFeatureBuilder(typePolygon);
        List<SimpleFeature> featuresPolygon = new ArrayList<SimpleFeature>();
        for(SimpleFeature feature : featuresList) {
            String siLine = siData.get(feature.getAttribute("MESH5TH").toString());
            String siPair[] = siLine.split(",");
            System.out.println(siLine + " : " + siPair.length);
            String dataLine = dataData.get(feature.getAttribute("MESH4TH").toString());
            if(dataLine == null) {
                dataLine = "0,0,0,0,0";
            }
            String dataPair[] = dataLine.split(",");
            MultiPolygon polygon = (MultiPolygon)feature.getDefaultGeometry();
            featureBuilderPolygon.add(polygon);
            featureBuilderPolygon.add(feature.getAttribute("MESH5TH").toString());
            featureBuilderPolygon.add(feature.getAttribute("MESH4TH").toString());
            featureBuilderPolygon.add(feature.getAttribute("MESH3RD").toString());
            featureBuilderPolygon.add(feature.getAttribute("MESH2ND").toString());
            featureBuilderPolygon.add(feature.getAttribute("MESH1ST").toString());
            if(dataPair[1].equals("")) {
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(Double.parseDouble(dataPair[1])/4);
            }

            if(dataPair[4].equals("")) {
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(Double.parseDouble(dataPair[4]) / 4);
            }

            if(siPair.length < 2) {
                featureBuilderPolygon.add(null);
                featureBuilderPolygon.add(null);
            }
            else if(siPair[1].equals("")) {
                featureBuilderPolygon.add(null);
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(siPair[1]);
                featureBuilderPolygon.add(Double.parseDouble(siPair[1]));
            }

            if(siPair.length < 3) {
                featureBuilderPolygon.add(null);
            }
            else if(siPair[2].equals("")) {
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(Double.parseDouble(siPair[2]));
            }

            if(siPair.length < 4) {
                featureBuilderPolygon.add(null);
            }
            else if(siPair[3].equals("")) {
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(Double.parseDouble(siPair[3]));
            }

            if(siPair.length < 5) {
                featureBuilderPolygon.add(null);
            }
            else if(siPair[4].equals("")) {
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(Double.parseDouble(siPair[4]));
            }

            if(siPair.length < 6) {
                featureBuilderPolygon.add(null);
            }
            else if(siPair[5].equals("")) {
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(Double.parseDouble(siPair[5]));
            }

            if(siPair.length < 7) {
                featureBuilderPolygon.add(null);
            }
            else if(siPair[6].equals("")) {
                featureBuilderPolygon.add(null);
            }
            else {
                featureBuilderPolygon.add(Double.parseDouble(siPair[6]));
            }

            if (siPair.length > 3 && (!siPair[3].equals("")) && (!dataPair[4].equals("0"))) {
                double evacuee = Double.parseDouble(siPair[3]) * (Double.parseDouble(dataPair[1]) / Double.parseDouble(dataPair[4]));
                featureBuilderPolygon.add(evacuee);
            }
            else {
                featureBuilderPolygon.add(null);
            }
            SimpleFeature f = featureBuilderPolygon.buildFeature(null);
            featuresPolygon.add(f);
        }
        CreateShape.createShapeFile(outFile, "UTF-8", typePolygon, featuresPolygon);
    }
}
