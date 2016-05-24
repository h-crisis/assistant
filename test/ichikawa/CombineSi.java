package ichikawa;

import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Geometry;

import java.io.File;

/**
 * Created by manabu on 2016/05/24.
 */
public class CombineSi {
    public static void main(String args[]) throws Exception {
        File file1 = new File("files/ShapeFiles/municipalities/municipalities.shp");
        File file2 = new File("files/ShapeFiles/municipalities/municipalities.shp");
        File outFile = new File("files/ShapeFiles/municipalities/out.shp");
        combineSiFromShape(file1, file2, outFile);
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
