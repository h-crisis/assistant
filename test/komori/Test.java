package komori;

import ichikawa.assist.*;
import java.io.*;

/**
 * Created by komori on 2016/07/13.
 */
public class Test {
    public static void main(String args[]) throws Exception {
        Shape2GeoJson.createGeoJson(new File("/Users/komori/Desktop/N10-15_39_GML/EMIS_Kouchi.shp"),"utf-8",
                new File("/Users/komori/Desktop/N10-15_39_GML/EMIS_Kouchi.geojson"),"utf-8");
    }
}
