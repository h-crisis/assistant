package ichikawa;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.math.Line;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Geometry;

import java.io.File;
import java.util.HashMap;

/**
 * Created by manabu on 2016/05/24.
 */
public class CombineSi {


    public static void siCombine(String municipalitiesFilePath, String hallsFilePath, String siFilePath, String outFilePath) throws Exception {
        File municipalitiesFile = new File(municipalitiesFilePath);
        File hallsFile = new File(hallsFilePath);
        File siFile = new File(siFilePath);
        File outFile = new File(outFilePath);

        FileDataStore municipalitiesDS = FileDataStoreFinder.getDataStore(municipalitiesFile);
        FileDataStore hallsDS = FileDataStoreFinder.getDataStore(hallsFile);
        FileDataStore siDS = FileDataStoreFinder.getDataStore(siFile);

        SimpleFeatureSource municipalitiesFS = municipalitiesDS.getFeatureSource();
        SimpleFeatureSource hallsFS = hallsDS.getFeatureSource();
        SimpleFeatureSource siFS = siDS.getFeatureSource();

        SimpleFeatureCollection municipalitiesCol = municipalitiesFS.getFeatures();
        SimpleFeatureCollection hallsCol = hallsFS.getFeatures();
        SimpleFeatureCollection siCol = siFS.getFeatures();
        int i = 0;

        SimpleFeatureIterator siI = siCol.features();

        HashMap<String, Object> munisipalitiesSiMap = new HashMap<String, Object>();

        while(siI.hasNext()) { // 震度ポイントを全て検索する
            SimpleFeature siFeature = siI.next();
            Point siPoint = (Point) siFeature.getAttribute("the_geom");
            SimpleFeatureIterator municipalitiesI = municipalitiesCol.features();
            while(municipalitiesI.hasNext()) {
                SimpleFeature municipalityFeature = municipalitiesI.next();
                MultiPolygon municipalityPolygon = (MultiPolygon) municipalityFeature.getAttribute("the_geom");
                if(siPoint.within(municipalityPolygon)) {
                    System.out.println("Num Si: " + siCol.size() + "Num: " + i++);
                    continue;
                }
            }
        }
    }


    public static void combineSiFromShape(File fileMunicipalities, File fileSi, File outFile) throws Exception {
        FileDataStore storeMunicipalities = FileDataStoreFinder.getDataStore(fileMunicipalities);
        FileDataStore storeSi = FileDataStoreFinder.getDataStore(fileSi);
        SimpleFeatureSource featureSourceMunicipalities = storeMunicipalities.getFeatureSource();
        SimpleFeatureSource featureSourceSi = storeSi.getFeatureSource();


        SimpleFeatureIterator iMunicipalities = featureSourceMunicipalities.getFeatures().features();
        SimpleFeatureIterator iSi = featureSourceSi.getFeatures().features();

        Geometry geometrySi;


        while(iSi.hasNext()) {
            SimpleFeature sf = (SimpleFeature)iSi.next();
            if(!((((MultiPolygon)sf.getAttribute("the_geom")).getCentroid().within((MultiPolygon)sf.getAttribute("the_geom"))))) {
                System.out.println(sf.getAttribute("code"));
            };
        }

    }
}
