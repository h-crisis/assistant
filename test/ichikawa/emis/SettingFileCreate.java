package ichikawa.emis;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import ichikawa.assist.MeshMap;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manabu on 2016/07/08.
 */
public class SettingFileCreate {

    public static void main(String args[]) throws IOException {
        File inFile = new File("files/ShapeFiles/medical_institute/medical_institute.shp");
        File outFile = new File("files/OUT/medical_institute_with_mesh.csv");
        File meshFileDir = new File("files/ShapeFiles/mesh");
        File mesh1stFile = new File("files/ShapeFiles/mesh/Mesh1st.shp");

        try {
            createMedicalInstituteMeshFile(inFile, outFile, "UTF-8", "UTF-8", meshFileDir, mesh1stFile);

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 医療機関が属する５次メッシュを判別し医療機関コードと５次メッシュコードのCSVを出力する
     * @param inFile 医療機関Shapeファイル
     * @param outFile 出力ファイル
     * @param inFileEncording 医療機関Shapeファイルの文字コード
     * @param outFileEncording 出力ファイルの文字コード
     * @throws IOException
     */
    public static void createMedicalInstituteMeshFile(File inFile, File outFile, String inFileEncording, String outFileEncording,
                                                      File meshFileDir, File mesh1stFile) {

        MeshMap mm = new MeshMap(meshFileDir, "UTF-8");

        try {
            // 医療機関Shapeファイルを開く
            ShapefileDataStore inFileStore = new ShapefileDataStore(inFile.toURI().toURL());
            inFileStore.setCharset(Charset.forName(inFileEncording));
            SimpleFeatureSource featureSource = inFileStore.getFeatureSource();
            SimpleFeatureCollection c = featureSource.getFeatures();
            FeatureIterator i = c.features();

            // 出力ファイルの準備をする
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile,false),"UTF-8"));
            pw.write("CODE,LAT,LON,MESH1ST,MESH2ND,MESH3RD,MESH4TH,MESH5TH");

            // 1次メッシュファイルを開く準備をする
            ShapefileDataStore mesh1FileDataStore = new ShapefileDataStore(mesh1stFile.toURI().toURL());
            mesh1FileDataStore.setCharset(Charset.forName("UTF-8"));
            SimpleFeatureSource mesh1FeatureSource = mesh1FileDataStore.getFeatureSource();
            SimpleFeatureCollection mesh1FeatureCollection = mesh1FeatureSource.getFeatures();

            int num = 1;
            while(i.hasNext()) {
                SimpleFeature medicalInstitute = (SimpleFeature) i.next();
                System.out.println(num + "/" + c.size() + ": " + medicalInstitute.getAttribute("NAME") + "の5次メッシュを設定します");
                FeatureIterator mesh1I = mesh1FeatureCollection.features();

                // 1次メッシュ絞り込み
                String mesh1Code = "";
                while(mesh1I.hasNext()) {
                    SimpleFeature mesh = (SimpleFeature) mesh1I.next();
                    if(((Point)medicalInstitute.getAttribute("the_geom")).within((MultiPolygon)mesh.getAttribute("the_geom")) || ((Point)medicalInstitute.getAttribute("the_geom")).touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                        mesh1Code = (String) mesh.getAttribute("MESH1ST");
                        System.out.println("\t" + medicalInstitute.getAttribute("NAME") + "の1次メッシュは " + mesh1Code + " です。");
                        break;
                    }
                }
                mesh1I.close();

                // 2次メッシュ絞り込み
                String mesh2Code = "";
                List<SimpleFeature> mesh2List = mm.getMap(1).get(mesh1Code);
                for(int j=0; j<mesh2List.size(); j++) {
                    SimpleFeature mesh = mesh2List.get(j);
                    if(((Point)medicalInstitute.getAttribute("the_geom")).within((MultiPolygon)mesh.getAttribute("the_geom")) || ((Point)medicalInstitute.getAttribute("the_geom")).touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                        mesh2Code = (String) mesh.getAttribute("MESH2ND");
                        System.out.println("\t" + medicalInstitute.getAttribute("NAME") + "の2次メッシュは " + mesh2Code + " です。");
                        break;
                    }
                }

                // 3次メッシュ絞り込み
                String mesh3Code = "";
                List<SimpleFeature> mesh3List = mm.getMap(2).get(mesh2Code);
                for(int j=0; j<mesh3List.size(); j++) {
                    SimpleFeature mesh = mesh3List.get(j);
                    if(((Point)medicalInstitute.getAttribute("the_geom")).within((MultiPolygon)mesh.getAttribute("the_geom")) || ((Point)medicalInstitute.getAttribute("the_geom")).touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                        mesh3Code = (String) mesh.getAttribute("MESH3RD");
                        System.out.println("\t" + medicalInstitute.getAttribute("NAME") + "の3次メッシュは " + mesh3Code + " です。");
                        break;
                    }
                }

                // 4次メッシュ絞り込み
                String mesh4Code = "";
                List<SimpleFeature> mesh4List = mm.getMap(3).get(mesh3Code);
                for(int j=0; j<mesh4List.size(); j++) {
                    SimpleFeature mesh = mesh4List.get(j);
                    if(((Point)medicalInstitute.getAttribute("the_geom")).within((MultiPolygon)mesh.getAttribute("the_geom")) || ((Point)medicalInstitute.getAttribute("the_geom")).touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                        mesh4Code = (String) mesh.getAttribute("MESH4TH");
                        System.out.println("\t" + medicalInstitute.getAttribute("NAME") + "の4次メッシュは " + mesh4Code + " です。");
                        break;
                    }
                }

                // 5次メッシュ絞り込み
                String mesh5Code = "";
                List<SimpleFeature> mesh5List = mm.getMap(4).get(mesh4Code);
                for(int j=0; j<mesh5List.size(); j++) {
                    SimpleFeature mesh = mesh5List.get(j);
                    if(((Point)medicalInstitute.getAttribute("the_geom")).within((MultiPolygon)mesh.getAttribute("the_geom")) || ((Point)medicalInstitute.getAttribute("the_geom")).touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                        mesh5Code = (String) mesh.getAttribute("MESH5TH");
                        System.out.println("\t" + medicalInstitute.getAttribute("NAME") + "の5次メッシュは " + mesh5Code + " です。");
                        break;
                    }
                }

                pw.write("\n" + medicalInstitute.getAttribute("CODE") + "," + medicalInstitute.getAttribute("LAT") + "," + medicalInstitute.getAttribute("LON") + ","
                         + mesh1Code + "," + mesh2Code + "," + mesh3Code + "," + mesh4Code + "," + mesh5Code);
                num++;
            }
            i.close();
            pw.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

        }
    }
}
