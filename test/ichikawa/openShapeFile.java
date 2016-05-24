package ichikawa;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;

/**
 * Created by manabu on 2016/05/24.
 */
public class openShapeFile {
    public static void main(String args[]) throws Exception {
        File file = new File("files/ShapeFiles/municipalities/municipalities.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        SimpleFeatureIterator i = featureSource.getFeatures().features();
        while(i.hasNext()) {
            System.out.println(((SimpleFeature)i.next()).getAttributes().size());
        }
    }
}
