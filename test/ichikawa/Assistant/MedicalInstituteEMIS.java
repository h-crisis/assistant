package ichikawa.assistant;


import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manabu on 2016/06/24.
 */
public class MedicalInstituteEMIS {

    private File medicalInstituteMasterFile;
    private File medicalInstituteStatusFile;
    private File combinedFile;
    private File shapeFile;
    private File jsonFile;
    private SimpleFeatureType simpleFeatureType;
    private List<SimpleFeature> features;

    MedicalInstituteEMIS(String masterFilePath, String statusFilePath, String outFilePath) throws IOException, SchemaException {
        medicalInstituteMasterFile = new File(masterFilePath);
        medicalInstituteStatusFile = new File(statusFilePath);
        combinedFile = new File(outFilePath);
        shapeFile = new File("files/WorkingFiles/medical_status.shp");
        jsonFile = new File("files/WorkingFiles/medical_status.geojson");

        simpleFeatureType = createFeatureType();
        features = new ArrayList<SimpleFeature>();

        checkFiles(medicalInstituteMasterFile, medicalInstituteStatusFile, combinedFile);
    }

    // ShapeファイルのFeatureタイプと測地系及び属性値の設定を行う
    private SimpleFeatureType createFeatureType() throws SchemaException {
        SimpleFeatureType type = DataUtilities.createType("Medical Institute",
                "the_geom:Point:srid=4612," +
                "code:String," +
                "name:String"
        );
        System.out.println("CreateFeatureType: " + type);
        return type;
    }

    private static void checkFiles(File masterFile, File statusFile, File outFile) throws IOException {
        if(!masterFile.exists())
            throw new RuntimeException("医療機関マスターファイルがありません。");
        else if(!statusFile.exists())
            throw new RuntimeException("医療機関状況ファイルがありません。");
        else if(!outFile.exists())
            outFile.createNewFile();

        BufferedReader masterFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(masterFile),"Shift_JIS"));
        BufferedReader statusFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(statusFile),"Shift_JIS"));

        String str1 = masterFileBR.readLine();
        String str2 = statusFileBR.readLine();
        int col1 = str1.split(",").length;
        int col2 = str2.split(",").length;
        int line1 = 1;
        int line2 = 1;

        while(str1!=null) {
            if(col1 != str1.split(",").length)
                throw new RuntimeException("医療機関マスターファイルの列数が揃っていません: " + line1);
            str1 = masterFileBR.readLine();
            line1++;
        }

        while(str2!=null) {
            if(col2 != str2.split(",").length) {
                System.out.println(str2.split(",").length + " " + col2);
                throw new RuntimeException("医療機関状況ファイルの列数が揃っていません: " + line2);
            }
            str2 = statusFileBR.readLine();
            line2++;
        }
    }

    public void comineMedicalInstituteFiles() throws IOException {
        System.out.println("医療機関マスターファイルと状況ファイルを結合します");
        HashMap<String, String> masterMap = new HashMap<String, String>();
        HashMap<String, String> statusMap = new HashMap<String, String>();
        LinkedList<String> codeList = new LinkedList<String>();
        BufferedReader masterFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(medicalInstituteMasterFile),"Shift_JIS"));
        BufferedReader statusFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(medicalInstituteStatusFile),"Shift_JIS"));
        PrintWriter pw    = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(combinedFile),"UTF-8")));

        String masterStr;
        while((masterStr = masterFileBR.readLine()) != null) {
            String pair[] = masterStr.split(",");
            for(int i =0; i<pair.length; i++)
                pair[i].trim();

            codeList.addLast(pair[0]);
            masterMap.put(pair[0], masterStr);
        }
        masterFileBR.close();


        String statusStr;
        while((statusStr = statusFileBR.readLine()) != null) {
            String pair[] = statusStr.split(",");
            for(int i =0; i<pair.length; i++)
                pair[i].trim();

            String str = "," + pair[3] + "," + pair[4];
            for(int i =6; i<pair.length; i++) {
                str = str + "," + pair[i];
            }
            statusMap.put(pair[0], str);
        }
        statusFileBR.close();

        for(int i=0; i<codeList.size(); i++) {
            String code = codeList.get(i);
            String str = masterMap.get(code);
            if(statusMap.containsKey(code)) {
                String pair[] = statusMap.get(code).split(",");
                for(int j=1; j<pair.length; j++)
                    str = str + "," + pair[j];
            }
            else
                str = str + ",未,未入力,0,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,, , , , , , ,0,0,0,0,0,0,, , ,0,,1";
            pw.println(str);
        }
        pw.close();
        System.out.println("医療機関マスターファイルと状況ファイルを結合が完了しました");
    }

    public void createFeatures() throws Exception {
        System.out.println("Featureの作成を開始します");
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(combinedFile),"UTF-8"));
        try {
            String line = br.readLine();

            while((line=br.readLine())!=null) {
                String pair[] = line.split(",");

                double lat = Double.parseDouble(pair[6]);
                double lon = Double.parseDouble(pair[7]);
                String code = pair[0];
                String name = pair[1];

                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));

                featureBuilder.add(point);
                featureBuilder.add(code);
                featureBuilder.add(name);
                SimpleFeature feature = featureBuilder.buildFeature(null);
                features.add(feature);
            }
        }
        finally {
            br.close();
        }
        System.out.println("Featureの作成が完了しました");
    }

    public void createShapeFile() throws Exception {
        CreateShape.createShapeFile(shapeFile, "utf-8", simpleFeatureType, features);
    }

    public void createGeoJsonFile() throws Exception {
        Shape2GeoJson.createGeoJson(shapeFile, "utf-8", jsonFile, "utf-8");
    }
}
