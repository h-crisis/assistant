package distance_calculation;

/**
 * Created by jiao on 2016/12/19.
 * 一つのshapeファイルのを2次メッシュによる分割します。
 */

import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class transShape {

    private static File srcfilepath;
    private static File destfilepath;
    private static String value;
    /**
     * @param srcfilepath;//全道路リンクshapeファイル
     * @param destfilepath;//分割されたファイル；
     * @param value;//２次メッシュコード
     */
    transShape(File srcfilepath, File destfilepath, String value) throws Exception {
        trans_Shape(srcfilepath, destfilepath, value);
    }


    //public void transShape(String srcfilepath, String destfilepath, String value) {
        public static void trans_Shape(File srcfilepath, File destfilepath, String value) {
    //    public static void main(String[] args) {
            String t;
            //String mesh="ファイル名";
       // String value="523644";
            try {
            //元shapeファイル
                ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(srcfilepath.toURI().toURL());
                shapeDS.setCharset(Charset.forName("SHIFT-JIS"));//文字コード

                //出力shapeファイル作り
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
                params.put(ShapefileDataStoreFactory.URLP.key, destfilepath.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
            // 属性設定
            SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);

            ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), DefaultGeographicCRS.WGS84));

            //writer初期化

            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            //書き出す
                SimpleFeatureIterator it = fs.getFeatures().features();
            try {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    Iterator<Property> its = f.getProperties().iterator();
                    while(its.hasNext()) {
                        Property pro = its.next();
                        t= String.valueOf(pro.getValue());
                        if(t.equals(value)){
                            //System.out.println(pro.getValue());
                            SimpleFeature fNew = writer.next();
                            fNew.setAttributes(f.getAttributes());
                            writer.write();
                        }
                    }
                }
            } finally {
                it.close();
            }
            writer.close();
            ds.dispose();
            shapeDS.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
