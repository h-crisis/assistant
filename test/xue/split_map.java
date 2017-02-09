package org.geotools.Shelter;

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
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jiao on 2017/01/06.
 * 地図をstart end点による分割するプログラム
 *
 */
public class split_map {

    private static File srcfilepath;
    private static File destfilepath;
    double lat1, lon1,lat2,lon2; //lat1 lon1: start点の緯度経度　lat2,lon2：end点の緯度経度

    /**
     * @param roadDir 全道路リンクshapeファイルのフォルダ
     * @param outDir 分割されたshapeファイルのフォルダ
     *
     */
    split_map(File roadDir, File outDir, Double lat1, Double lon1, Double lat2, Double lon2) throws IOException {
        File srcfilepath = new File(roadDir.getPath()+"/全道路リンク.shp");//全道路リンク
        File destfilepath= new File(outDir.getPath()+"/split_map.shp");//start点からend点まで挟んでいるshape
        if(!outDir.exists()) {
            outDir.createNewFile();
        }
        ArrayList mesh_list=shape_mesh(lat1,lon1,lat2,lon2);
        //System.out.println(mesh_list);
        slipt_Shape(srcfilepath,destfilepath,mesh_list);

    }



    public static void slipt_Shape(File srcfilepath, File destfilepath, ArrayList mesh_list) {
        //    public static void main(String[] args)
        // srcfilepath;//全道路リンクshapeファイル
        //destfilepath;//分割されたファイル；
        //mesh_list;//
        String t;
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
                    while (its.hasNext()) {
                        Property pro = its.next();
                        t = String.valueOf(pro.getValue());
                        if (mesh_list.contains(t)) {
                            SimpleFeature fNew = writer.next();
                            fNew.setAttributes(f.getAttributes());
                            writer.write();
                        }
                    }
                }
            } finally {
                it.close();
                shapeDS.dispose();

            }
            writer.close();
            ds.dispose();
           // shapeDS.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList shape_mesh(Double lat1, Double lon1, Double lat2, Double lon2) {
        //start点からend点まで挟んでいる2時メッシュコードのリスト
        int mesh2nd_start = find_mesh2nd(lat1, lon1);
        int mesh2nd_end = find_mesh2nd(lat2, lon2);
        int pw1, pw2, qw1, qw2, pu1, pu2, qv1, qv2;
        pu1 = (int) (mesh2nd_start / 10000);
        pu2 = (int) (mesh2nd_end / 10000);
        qv1 = (int) (mesh2nd_start / 100 % 100);
        qv2 = (int) (mesh2nd_end / 100 % 100);
        pw1 = (int) (mesh2nd_start % 100 / 10);
        pw2 = mesh2nd_start % 10;
        qw1 = (int) (mesh2nd_end % 100 / 10);
        qw2 = mesh2nd_end % 10;
        ArrayList<String> b = new ArrayList<String>();//2次メッシュリストを記録する

        ///startとendは同じ1次メッシュ内なら
        if ((int) (mesh2nd_start / 100) == (int) (mesh2nd_end / 100)) {
            ArrayList<Integer> a = new ArrayList<Integer>();
            int cal = 0;
            for (int i = pw1 > qw1 ? qw1 : pw1; i <= (pw1 < qw1 ? qw1 : pw1); i++) {
                a.add(i);
                cal++;
            }
            for (int i = pw2 > qw2 ? qw2 : pw2; i <= (pw2 < qw2 ? qw2 : pw2); i++) {
                for (int m = 0; m < cal; m++) {
                    b.add(String.valueOf((int) (mesh2nd_start / 100) * 100 + a.get(m) * 10 + i));
                }
            }

        }

        ///startとendは同じ緯度なら
        //
        else if (pu1 == pu2) {
            int i = qv1 < qv2 ? qv1 : qv2;//小さいqv
            // ArrayList<Integer> a = new ArrayList<Integer>();
            //  if (i == qv1) {
            int t2 = qv2 * 100 + pw1 * 10 + qw2;
            int t1 = qv1 * 100 + pw1 * 10 + pw2;
            while (t2 != mesh2nd_end % 10000) {
                if (i == qv1) {
                    while (t1 <= t2) {
                        b.add(String.valueOf(pu1 * 10000 + t1));
                        t1 = add_one(t1);
                    }
                } else {
                    while (t1 >= t2) {
                        b.add(String.valueOf(pu1 * 10000 + t1));
                        t1 = minus_one(t1);
                    }
                }
                int m = pw1 > qw1 ? qw1 : pw1;
                if (m == pw1) {
                    t1 = qv1 * 100 + (pw1 + 1) * 10 + pw2;
                    t2 = qv2 * 100 + (pw1 + 1) * 10 + qw2;
                    pw1++;
                } else {
                    t1 = qv1 * 100 + (pw1 - 1) * 10 + pw2;
                    t2 = qv2 * 100 + (pw1 - 1) * 10 + qw2;
                    pw1--;
                }
            }
            if (i == qv1) {
                while (t1 <= t2) {//最後t2 == mesh2nd_end % 10000 の処理
                    b.add(String.valueOf(pu1 * 10000 + t1));
                    t1 = add_one(t1);
                }
            } else {
                while (t1 >= t2) {//最後t2 == mesh2nd_end % 10000 の処理
                    b.add(String.valueOf(pu1 * 10000 + t1));
                    t1 = minus_one(t1);
                }
            }
        }
//同じ経度なら
        else if (qv1 == qv2) {
            int i = pu1 < pu2 ? pu1 : pu2;//小さいpu
            // ArrayList<Integer> a = new ArrayList<Integer>();
            int t2 = pu2 * 10000 + qv1 * 100 + qw1 * 10 + pw2;
            int t1 = pu1 * 10000 + qv1 * 100 + pw1 * 10 + pw2;//t1

            while (t2 != mesh2nd_end) {
                if (i == pu1) {
                    while (t1 <= t2) {
                        b.add(String.valueOf(t1));
                        t1 = add_one2(t1);

                    }
                } else {
                    while (t1 >= t2) {
                        b.add(String.valueOf(t1));
                        t1 = minus_one2(t1);
                    }
                }
                int m = pw2 > qw2 ? qw2 : pw2;
                if (m == pw2) {
                    t1 = pu1 * 10000 + qv1 * 100 + pw1 * 10 + pw2 + 1;
                    t2 = pu2 * 10000 + qv1 * 100 + qw1 * 10 + pw2 + 1;
                    pw2++;
                } else {
                    t1 = pu1 * 10000 + qv1 * 100 + pw1 * 10 + pw2 - 1;
                    t2 = pu2 * 10000 + qv1 * 100 + qw1 * 10 + pw2 - 1;
                    pw2--;
                }
            }
            if (i == pu1) {
                while (t1 <= t2) {//最後t2 == mesh2nd_end % 10000 の処理
                    b.add(String.valueOf(t1));
                    t1 = add_one2(t1);
                }
            } else {
                while (t1 >= t2) {//最後t2 == mesh2nd_end % 10000 の処理
                    b.add(String.valueOf(t1));
                    t1 = minus_one2(t1);
                }
            }
        }
        /////////////////////////全部違うなら
        else {
            int i = pu1 < pu2 ? pu1 : pu2;//小さいpu
            int t2 = pu2 * 10000 + qv1 * 100 + qw1 * 10 + pw2;
            int t1 = pu1 * 10000 + qv1 * 100 + pw1 * 10 + pw2;//t1


            while (t2 != mesh2nd_end) {
                int p1 = t1;
                //  int p2=t2;
                if (i == pu1) {
                    while (t1 <= t2) {
                        b.add(String.valueOf(t1));
                        t1 = add_one2(t1);

                    }
                } else {
                    while (t1 >= t2) {
                        b.add(String.valueOf(t1));
                        t1 = minus_one2(t1);
                    }
                }
                t1 = p1;

                int m = qv1 < qv2 ? qv1 : qv2;
                if (m == qv1) {
                    t1 = add_one(t1);
                    t2 = add_one(t2);
                } else {
                    t1 = minus_one(t1);
                    t2 = minus_one(t2);
                }
            }
            if (i == pu1) {
                while (t1 <= t2) {//最後t2 == mesh2nd_end % 10000 の処理
                    b.add(String.valueOf(t1));
                    t1 = add_one2(t1);
                }
            } else {
                while (t1 >= t2) {//最後t2 == mesh2nd_end % 10000 の処理
                    b.add(String.valueOf(t1));
                    t1 = minus_one2(t1);
                }

            }

        }
        return b;
    }


    public static int add_one(int a) {
        int t;
        if(a%10<7){
            t=a+1;
        }
        else{
            t=(int)(a/10000)*10000+((int) (a / 100 % 100)+1)*100+(int)(a%100/10)*10;
        }
        return t;
    }


    public static int add_one2(int a) {
        //例 470077＋10＝480007
        int t;
        if((int)(a%100/10)<7){
            t=a+10;
        }
        else{

            t=(int)((a+10000)/100)*100+a%10;
        }
        return t;
    }

    public static int minus_one(int a) {
        int t;
        if(a%10>0){
            t=a-1;
        }
        else{
            t=(int)(a/10000)*10000+((int) (a / 100 % 100)-1)*100+(int)(a%100/10)*10+7;
        }
        return t;
    }

    public static int minus_one2(int a) {
        int t;
        if((int)(a%100/10)>0){
            t=a-10;
        }
        else{
            t=(int)((a-10000)/100)*100+70+a%10;
        }
        return t;
    }

    public static int find_mesh2nd(Double lat1, Double lon1) {
        ///緯度軽度から2次メッシュコードを計算する
        int pu, qv, rw1, rw2, mesh;
        double a;
        pu = (int) (lat1 * 60 / 40);
        qv = (int) (lon1 - 100);
        rw1 = (int) (lat1 * 60 % 40 / 5);
        rw2 = (int) ((lon1 - 100 - qv) * 60 / 7.5);
        mesh = pu * 10000 + qv * 100 + rw1 * 10 + rw2;
        //String mesh2nd = Integer.toString(mesh);
        return mesh;
    }

}
