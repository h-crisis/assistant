package ichikawa.setting;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by manabu on 2016/07/18.
 */
public class SearchMunicipalityThread implements Callable<SimpleFeature> {
    private SimpleFeatureCollection municipalitiesC;
    private SimpleFeature mesh;
    private GeometryFactory geometryFactory;
    private SimpleFeatureBuilder featureBuilder;
    private HashMap<String, HashMap<String, String>> meshDataMap;

    public SearchMunicipalityThread(SimpleFeature feature, SimpleFeatureCollection c, GeometryFactory factory, SimpleFeatureBuilder builder, HashMap<String, HashMap<String, String>> map) {
        this.mesh = feature;
        this.municipalitiesC = c;
        this.geometryFactory = factory;
        this.featureBuilder = builder;
        this.meshDataMap = map;
    }

    public SimpleFeature call() throws Exception {
        // 各メッシュが属する市区町村を探索する
        FeatureIterator<SimpleFeature> municipalitiesI = municipalitiesC.features();
        String code = "00000";

        while(municipalitiesI.hasNext()) {
            SimpleFeature municipality = municipalitiesI.next();
            // メッシュの中心が市区町村の境界内か境界上であればそのコードを取得する
            if(((MultiPolygon)mesh.getAttribute("the_geom")).getCentroid().within((MultiPolygon)municipality.getAttribute("the_geom")) || ((MultiPolygon)mesh.getAttribute("the_geom")).getCentroid().touches((MultiPolygon)municipality.getAttribute("the_geom"))) {
                code = (String) municipality.getAttribute("CODE");
                break;
            }
        }
        municipalitiesI.close();

        // Featureの作成
        Polygon polygon = geometryFactory.createPolygon(((MultiPolygon)mesh.getAttribute("the_geom")).getCoordinates());
        featureBuilder.add(polygon);
        featureBuilder.add(mesh.getAttribute("MESH5TH"));
        featureBuilder.add(mesh.getAttribute("MESH4TH"));
        featureBuilder.add(mesh.getAttribute("MESH3RD"));
        featureBuilder.add(mesh.getAttribute("MESH2ND"));
        featureBuilder.add(mesh.getAttribute("MESH1ST"));
        featureBuilder.add(code);
        if (meshDataMap.containsKey(mesh.getAttribute("MESH4TH")) && (!code.equals("00000"))) {
            featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("POP"))/4));
            featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("POP_M"))/4));
            featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("POP_F"))/4));
            featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("HOUSE"))/4));
        }
        else {
            featureBuilder.add(0);
            featureBuilder.add(0);
            featureBuilder.add(0);
            featureBuilder.add(0);
        }

        SimpleFeature feature = featureBuilder.buildFeature(null);
        return feature;
    }
}
