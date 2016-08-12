package ichikawa;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manabu on 2016/06/01.
 */
public class MedicalInstitute {
    public static void main(String args[]) throws Exception {
        plusMesh("test");
    }

    public static void plusMesh(String path) throws Exception {
        // 医療機関shapeファイルを読み込む
        //File medicalInstitute = new File(path);
        File medicalInstitute = new File("files/ShapeFiles/medical_institute/medical_institute.shp");

        // 標準１次メッシュから５次メッシュまでを読み込む
        File mesh1st = new File("shape/mesh/Mesh1st_Center.shp");
        File mesh2nd = new File("shape/mesh/Mesh2nd_Center.shp");
        File mesh3rd = new File("shape/mesh/Mesh3rd_Center.shp");
        File mesh4th = new File("shape/mesh/Mesh4th_Center.shp");
        File mesh5th = new File("shape/mesh/Mesh5th_Center.shp");

        // 読み込みモードで開く
        medicalInstitute.setReadOnly();
        mesh1st.setReadOnly();
        mesh2nd.setReadOnly();
        mesh3rd.setReadOnly();
        mesh4th.setReadOnly();
        mesh5th.setReadOnly();


        // ShapeファイルをDataStoreに紐付け、DataStoreをFeatureSourceに関連づける
        ShapefileDataStore medicalInstituteDataStore = new ShapefileDataStore(medicalInstitute.toURI().toURL());
        ShapefileDataStore mesh1stDataStore = new ShapefileDataStore(mesh1st.toURI().toURL());
        ShapefileDataStore mesh2ndDataStore = new ShapefileDataStore(mesh2nd.toURI().toURL());
        ShapefileDataStore mesh3rdDataStore = new ShapefileDataStore(mesh3rd.toURI().toURL());
        ShapefileDataStore mesh4thDataStore = new ShapefileDataStore(mesh4th.toURI().toURL());
        ShapefileDataStore mesh5thDataStore = new ShapefileDataStore(mesh5th.toURI().toURL());

        // 文字コードの設定
        Charset cs = Charset.forName("UTF-8");
        medicalInstituteDataStore.setCharset(cs);
        mesh1stDataStore.setCharset(cs);
        mesh2ndDataStore.setCharset(cs);
        mesh3rdDataStore.setCharset(cs);
        mesh4thDataStore.setCharset(cs);
        mesh5thDataStore.setCharset(cs);

        // DataStoreからSimpleFeatureSourceオブジェクトを作成する。
        SimpleFeatureSource medicalInstituteFeatureSource = medicalInstituteDataStore.getFeatureSource();
        SimpleFeatureSource mesh1stFeatureSource = mesh1stDataStore.getFeatureSource();
        SimpleFeatureSource mesh2ndFeatureSource = mesh2ndDataStore.getFeatureSource();
        SimpleFeatureSource mesh3rdFeatureSource = mesh3rdDataStore.getFeatureSource();
        SimpleFeatureSource mesh4thFeatureSource = mesh4thDataStore.getFeatureSource();
        SimpleFeatureSource mesh5thFeatureSource = mesh5thDataStore.getFeatureSource();

        // FeatureSourceから全Featureを要素にもつコレクションが取り、イテレーターを定義する。
        SimpleFeatureCollection medicalInstituteC = medicalInstituteFeatureSource.getFeatures();
        SimpleFeatureCollection mesh1stC = mesh1stFeatureSource.getFeatures();
        SimpleFeatureCollection mesh2ndC = mesh2ndFeatureSource.getFeatures();
        SimpleFeatureCollection mesh3rdC = mesh3rdFeatureSource.getFeatures();
        SimpleFeatureCollection mesh4thC = mesh4thFeatureSource.getFeatures();
        SimpleFeatureCollection mesh5thC = mesh5thFeatureSource.getFeatures();

        FeatureIterator medicalInstituteI = medicalInstituteC.features();

        HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
        // 全ての医療機関について、最寄りのメッシュポイントを紐付ける
        while(medicalInstituteI.hasNext()) {
            // 医療機関を取得する
            SimpleFeature medicalInstituteFeature = (SimpleFeature)medicalInstituteI.next();
            List list = medicalInstituteFeature.getAttributes();
            Point medicalInstitutePoint = (Point)medicalInstituteFeature.getDefaultGeometry();
            String medicalInstituteCode = list.get(1).toString();

            // 標準メッシュのイテレーター準備
            FeatureIterator mesh1stI = mesh1stC.features();
            FeatureIterator mesh2ndI = mesh2ndC.features();
            FeatureIterator mesh3rdI = mesh3rdC.features();
            FeatureIterator mesh4thI = mesh4thC.features();
            FeatureIterator mesh5thI = mesh5thC.features();

            HashMap<String, String> meshMap = new HashMap<String, String>();
            meshMap.put("Mesh1st", "0000");
            meshMap.put("Mesh2nd", "000000");
            meshMap.put("Mesh3rd", "00000000");
            meshMap.put("Mesh4th", "000000000");
            meshMap.put("Mesh5th", "0000000000");

            // 正しい座標が与えられている場合のみ対応
            if(medicalInstitutePoint.getX() > 0) {
                // 標準1次メッシュの最寄り点を検索する
                double d1 = Double.MAX_VALUE;
                while(mesh1stI.hasNext()) {
                    SimpleFeature mesh1stFeature = (SimpleFeature) mesh1stI.next();
                    Point mesh1stPoint = (Point) mesh1stFeature.getDefaultGeometry();
                    String mesh1stCode = mesh1stFeature.getAttribute("MESH1ST").toString();

                    CoordinateReferenceSystem crs = CRS.decode("EPSG: 4612");
                    GeodeticCalculator gc = new GeodeticCalculator(crs);
                    gc.setStartingPosition( JTS.toDirectPosition( new Coordinate(medicalInstitutePoint.getY(),medicalInstitutePoint.getX()), crs ) );
                    gc.setDestinationPosition( JTS.toDirectPosition( new Coordinate(mesh1stPoint.getY(), mesh1stPoint.getX()), crs ) );

                    double distance = gc.getOrthodromicDistance();
                    if(distance < d1) {
                        meshMap.put("Mesh1st", mesh1stCode);
                        d1 = distance;
                    }
                }

                // 標準2次メッシュの最寄り点を検索する
                double d2 = Double.MAX_VALUE;
                while(mesh2ndI.hasNext()) {
                    SimpleFeature mesh2ndFeature = (SimpleFeature) mesh2ndI.next();
                    Point mesh2ndPoint = (Point) mesh2ndFeature.getDefaultGeometry();
                    String mesh2ndCode = mesh2ndFeature.getAttribute("MESH2ND").toString();

                    CoordinateReferenceSystem crs = CRS.decode("EPSG: 4612");
                    GeodeticCalculator gc = new GeodeticCalculator(crs);
                    gc.setStartingPosition( JTS.toDirectPosition( new Coordinate(medicalInstitutePoint.getY(),medicalInstitutePoint.getX()), crs ) );
                    gc.setDestinationPosition( JTS.toDirectPosition( new Coordinate(mesh2ndPoint.getY(), mesh2ndPoint.getX()), crs ) );

                    double distance = gc.getOrthodromicDistance();
                    if(distance < d2) {
                        meshMap.put("Mesh2nd", mesh2ndCode);
                        d2 = distance;
                    }
                }

                // 標準3次メッシュの最寄り点を検索する
                double d3 = Double.MAX_VALUE;
                while(mesh3rdI.hasNext()) {
                    SimpleFeature mesh3rdFeature = (SimpleFeature) mesh3rdI.next();
                    Point mesh3rdPoint = (Point) mesh3rdFeature.getDefaultGeometry();
                    String mesh3rdCode = mesh3rdFeature.getAttribute("MESH3RD").toString();

                    CoordinateReferenceSystem crs = CRS.decode("EPSG: 4612");
                    GeodeticCalculator gc = new GeodeticCalculator(crs);
                    gc.setStartingPosition( JTS.toDirectPosition( new Coordinate(medicalInstitutePoint.getY(),medicalInstitutePoint.getX()), crs ) );
                    gc.setDestinationPosition( JTS.toDirectPosition( new Coordinate(mesh3rdPoint.getY(), mesh3rdPoint.getX()), crs ) );

                    double distance = gc.getOrthodromicDistance();
                    if(distance < d3) {
                        meshMap.put("Mesh3rd", mesh3rdCode);
                        d3 = distance;
                    }
                }

                // 標準4次メッシュの最寄り点を検索する
                double d4 = Double.MAX_VALUE;
                while(mesh4thI.hasNext()) {
                    SimpleFeature mesh4thFeature = (SimpleFeature) mesh4thI.next();
                    Point mesh4thPoint = (Point) mesh4thFeature.getDefaultGeometry();
                    String mesh4thCode = mesh4thFeature.getAttribute("MESH4TH").toString();

                    CoordinateReferenceSystem crs = CRS.decode("EPSG: 4612");
                    GeodeticCalculator gc = new GeodeticCalculator(crs);
                    gc.setStartingPosition( JTS.toDirectPosition( new Coordinate(medicalInstitutePoint.getY(),medicalInstitutePoint.getX()), crs ) );
                    gc.setDestinationPosition( JTS.toDirectPosition( new Coordinate(mesh4thPoint.getY(), mesh4thPoint.getX()), crs ) );

                    double distance = gc.getOrthodromicDistance();
                    if(distance < d4) {
                        meshMap.put("Mesh4th", mesh4thCode);
                        d4 = distance;
                    }
                }

                // 標準3次メッシュの最寄り点を検索する
                double d5 = Double.MAX_VALUE;
                while(mesh5thI.hasNext()) {
                    SimpleFeature mesh5thFeature = (SimpleFeature) mesh5thI.next();
                    Point mesh5thPoint = (Point) mesh5thFeature.getDefaultGeometry();
                    String mesh5thCode = mesh5thFeature.getAttribute("MESH5TH").toString();

                    CoordinateReferenceSystem crs = CRS.decode("EPSG: 4612");
                    GeodeticCalculator gc = new GeodeticCalculator(crs);
                    gc.setStartingPosition( JTS.toDirectPosition( new Coordinate(medicalInstitutePoint.getY(),medicalInstitutePoint.getX()), crs ) );
                    gc.setDestinationPosition( JTS.toDirectPosition( new Coordinate(mesh5thPoint.getY(), mesh5thPoint.getX()), crs ) );

                    double distance = gc.getOrthodromicDistance();
                    if(distance < d5) {
                        meshMap.put("Mesh5th", mesh5thCode);
                        d5 = distance;
                    }
                }

                System.out.println(meshMap);

            }
            else {
                String str = list.get(2).toString() + ": "
                        + list.get(1).toString();
                //System.out.println(str);
            }

            break;
        }

        medicalInstituteI.close();

    }
}
