package jp.hcrisis.assistant.masterfiles.shelter;


import com.google.code.geocoder.model.GeocoderResult;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Created by manabu on 2016/09/24.
 */
public class ShelterDownload {

    // Yahoo避難所の各都道府県のページ数
    public static final int[] id = {
            177, 45, 52, 46, 48, 51, 63, 50, 29, 41,
            89, 73, 67, 62, 69, 32, 33, 39, 37, 87,
            83, 29, 85, 72, 33, 42, 87, 87, 28, 63,
            31, 51, 61, 69, 38, 57, 20, 59, 46, 78,
            18, 64, 40, 43, 45, 43, 38
    };

    // 都道府県名の配列。Yahoo避難所の順番に合わせる必要がある。
    public static final String[] prefecture_name = {
            "北海道", "青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県", "茨城県", "栃木県", "群馬県",
            "埼玉県", "千葉県", "東京都", "神奈川県", "新潟県", "富山県", "石川県", "福井県", "山梨県", "長野県",
            "岐阜県", "静岡県", "愛知県", "三重県", "滋賀県", "京都府", "大阪府", "兵庫県", "奈良県", "和歌山県",
            "鳥取県", "島根県", "岡山県", "広島県", "山口県", "徳島県", "香川県", "愛媛県", "高知県", "福岡県",
            "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県"
    };

    // 作業フォルダ
    public static File workingDir;

    /**
     * Yahoo避難所のクローリングを行うメソッド。
     * @param args 第1引数に作業フォルダのパス。省略するとfiles/YahooShelterフォルダで作業を行う
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        if(args.length>0) {
            workingDir = new File(args[0]);
        }
        else {
            workingDir = new File("files/YahooShelter");
        }
        if(!workingDir.exists()) {
            System.out.println("作業フォルダが存在しません。");
            System.exit(1);
        }
        //createShelterMasterYahoo(workingDir); // 全ての都道府県の避難所一覧を作成する場合
        //createShelterMasterYahoo(workingDir, 1); // 指定した都道府県の避難所一覧を作成する場合
        combineShleterFiles(workingDir);
        updateShelterFiles(new File("files/YahooShelter/0_全国.csv"), new File("files/shape/japan_ver80/japan_ver80.shp"), new File("files/YahooShelter/0_全国改.csv"));
    }

    /**
     * Yahoo避難所から指定した都道府県の避難所一覧ページを作成する
     * @param dir 出力フォルダ
     * @param i 指定する都道府県番号
     */
    public static void createShelterMasterYahoo(File dir, int i) {
            readSheltersFromPrefecture(new File(dir.getPath() + "/" + i + "_" + prefecture_name[i-1] + ".csv"), i);
    }

    /**
     * Yahoo避難所から全国の避難所一覧ページを作成する
     * @param dir 出力フォルダ
     */
    public static void createShelterMasterYahoo(File dir) {
        for(int i = 1; i<=id.length; i++) {
            readSheltersFromPrefecture(new File(dir.getPath() + "/" + i + "_" + prefecture_name[i-1] + ".csv"), i);
        }
    }

    /**
     * 都道府県ごとの避難所情報をクローリングするメソッド
     * @param file 都道府県避難所情報を保存するファイル
     * @param i 都道府県番号
     */
    public static void readSheltersFromPrefecture(File file, int i) {
        try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "Shift_JIS"))) {

            pw.write(getColName());

            String prefectureID = Integer.toString(i);
            if (i <= 9) {
                prefectureID = "0" + prefectureID;
            }

            for (int j = 1; j <= id[i - 1]; j++) {
                Document doc = Jsoup.connect("http://crisis.yahoo.co.jp/shelter/list/" + prefectureID + "/?p=" + j).get(); // 各都道府県の避難所一覧ページを読み込む
                //Document doc = Jsoup.connect("http://crisis.yahoo.co.jp/shelter/list/" + prefectureID + "/?p=1000").get(); // 各都道府県の避難所一覧ページを読み込む
                Element element = doc.getElementById("shltlst"); // id=shltlstの要素を取り出す
                Elements tbody = element.getElementsByTag("tr");
                for (int k = 1; k < tbody.size(); k++) {
                    Element shelter = tbody.get(k);
                    pw.write("\n" + getShelterData(shelter, prefecture_name[i - 1]));
                }
                System.out.println(prefecture_name[i - 1] + " : " + j + "/" + id[i - 1] + " が終わりました。");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("出力ファイルが見つかりませんでした。");
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("出力ファイルの文字コードがサポートされていません。");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Yahooとの接続でエラーが生じました。");
            System.exit(1);
        }
    }


    /**
     * 出力ファイル用の見出しを作るメソッド
     * @return 見出しの文字列
     */
    public static String getColName() {
        return "都道府県,市区町村,避難所名,住所,緯度,経度,避難所,一時避難所,広域避難所,地震,風水害,津波,収容人数,備蓄品,備考";
    }

    /**
     * Yahoo避難所に掲載される1つの避難所の情報をカンマ区切りの文字列として返すメソッド
     * @param shelter 避難所の該当HTML
     * @param prefecture 都道府県名
     * @return カンマ区切りの避難所情報文字列
     */
    public static String getShelterData(Element shelter, String prefecture) {
        try {
            String str = prefecture; // 都道府県名を追加
            str = str + "," + checkComma(shelter.getElementsByClass("autonomy").text()); // 自治体名
            str = str + "," + checkComma(shelter.getElementsByClass("name").text()); // 避難所名
            str = str + "," + checkComma(shelter.getElementsByClass("address").text()); // 住所
            String tmpURL = "http://crisis.yahoo.co.jp" + shelter.getElementsByTag("a").attr("href");
            Document tmpDoc = Jsoup.connect(tmpURL).get();
            str = str + "," + tmpDoc.getElementById("lat").text() + "," + tmpDoc.getElementById("lon").text(); // 緯度・経度

            // 避難所の種別
            String type = shelter.getElementsByClass("shelter").text();
            if (type.contentEquals("")) {
                str = str + ",-,-,-";
            } else {
                if (type.contains("避難所")) {
                    str = str + ",1";
                } else {
                    str = str + ",0";
                }
                if (type.contains("一時避難場所")) {
                    str = str + ",1";
                } else {
                    str = str + ",0";
                }
                if (type.contains("広域避難場所")) {
                    str = str + ",1";
                } else {
                    str = str + ",0";
                }
            }

            // 災害種別
            String disaster = shelter.getElementsByClass("disaster").text();
            if (disaster.equals("")) {
                str = str + ",-,-,-";
            } else {
                if (disaster.contains("地震")) {
                    str = str + ",1";
                } else {
                    str = str + ",0";
                }
                if (disaster.contains("風水害")) {
                    str = str + ",1";
                } else {
                    str = str + ",0";
                }
                if (disaster.contains("津波")) {
                    str = str + ",1";
                } else {
                    str = str + ",0";
                }
            }
            str = str + "," + removeComma(shelter.getElementsByClass("capacity").text()); // 収容人数
            str = str + "," + checkComma(shelter.getElementsByClass("stock").text()); // 備蓄品
            str = str + "," + checkComma(shelter.getElementsByClass("note").text()); // 備考
            return str;
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Yahooとの接続でエラーが生じました。");
            System.exit(1);
        }
        return null;
    }

    /**
     * 文字列内のカンマをチェックするメソッド。句点に変換する
     * @param str チェックする文字列
     * @return チェック後の文字列
     */
    public static String checkComma(String str) {
        if(str.contains(",")) {
            String pair[] = str.split(",");
            String tmp = "";
            for(int i=0; i<pair.length; i++) {
                if(i==0) {
                    tmp = pair[i];
                }
                else {
                    tmp = tmp + "、" + pair[i];
                }
            }
            return tmp;
        }
        else {
            return str;
        }
    }

    /**
     * 文字列内のカンマを削除するメソッド。
     * @param str 対象文字列
     * @return カンマ削除後の文字列
     */
    public static String removeComma(String str) {
        if(str.contains(",")) {
            String pair[] = str.split(",");
            String tmp = "";
            for(int i=0; i<pair.length; i++) {
                if(i==0) {
                    tmp = pair[i];
                }
                else {
                    tmp = tmp + pair[i];
                }
            }
            return tmp;
        }
        else {
            return str;
        }
    }

    /**
     * 都道府県別の避難所情報ファイルを結合するメソッド
     * @param dir 避難所情報ファイルが保存されているフォルダ
     */
    public static void combineShleterFiles(File dir) {
        if(!dir.isDirectory()) {
            System.out.println("避難所ファイルが存在するディレクトリではありません");
        }
        else {
            try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir.getPath() + "/0_全国.csv"), false), "Shift_JIS"))) {
                pw.write(getColName());
                for (int i=1; i<=id.length; i++) {
                    File file = new File(dir.getPath() + "/" + i + "_" + prefecture_name[i - 1] + ".csv");
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"))) {
                        String line = br.readLine();
                        while((line = br.readLine()) != null) {
                            pw.write("\n" + line);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 避難所の市区町村情報、住所情報を更新するプログラム（住所から市区町村情報を除くプログラム）
     * @param shelterFile Yahooからダウンロードした避難所ファイル
     * @param shapeFile ESRIの市区町村境界Shapeファイル
     * @param outFile 出力ファイル
     */
    public static void updateShelterFiles(File shelterFile, File shapeFile, File outFile) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(shelterFile), "Shift_JIS")) ;
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 市区町村境界のFeatureを全て読み込む。また市区町村コード、郡、市区町村のDBを作成する。
            ArrayList<SimpleFeature> municipalities = new ArrayList<>();
            HashMap<String, String> jcodeDB = new HashMap<>(); // 都道府県郡市区町村名をキー、市区町村コードを値
            HashMap<String, String> gunDB = new HashMap<>(); // 市区町村コードをキー、郡名を値
            HashMap<String, String> sikuchosonDB = new HashMap<>(); // 市区町村コードをキー、市区町村名を値

            ShapefileDataStore store = new ShapefileDataStore(shapeFile.toURI().toURL());
            store.setCharset(Charset.forName("Shift_JIS"));
            SimpleFeatureSource featureSource = store.getFeatureSource();
            FeatureIterator iFeature = featureSource.getFeatures().features();
            while(iFeature.hasNext()) {
                SimpleFeature feature = (SimpleFeature)iFeature.next();
                municipalities.add(feature);
                jcodeDB.put(feature.getAttribute("KEN").toString() + feature.getAttribute("GUN").toString() + feature.getAttribute("SEIREI").toString() + feature.getAttribute("SIKUCHOSON").toString()
                        ,feature.getAttribute("JCODE").toString());
                gunDB.put(feature.getAttribute("JCODE").toString(), feature.getAttribute("GUN").toString());
                sikuchosonDB.put(feature.getAttribute("JCODE").toString(), feature.getAttribute("SEIREI").toString() + feature.getAttribute("SIKUCHOSON").toString());
            }
            store.dispose();

            // 見出しを「避難所コード,市区町村コード,都道府県,郡,市区町村,避難所名,住所,緯度,経度,避難所,一時避難所,広域避難所,地震,風水害,津波,収容人数,備蓄品,備考」にする。
            String line = br.readLine();
            String headline[] = line.split(",");
            line = "避難所コード,市区町村コード," + headline[0] + ",郡,市区町村";
            for(int i = 2; i<=14; i++) {
                line = line + "," + headline[i];
            }
            pw.write(line);

            // 1行ずつ読み込みレコードを修正していく
            HashMap<String, Integer> idDB = new HashMap<>();
            while((line=br.readLine())!=null) {
                String str[] = line.split(","); // 行を分解

                // 基本情報を保存する
                double lat = Double.parseDouble(str[4]); // 緯度
                double lon = Double.parseDouble(str[5]); // 経度
                Coordinate latlon = new Coordinate(lat, lon);
                Point point = JTSFactoryFinder.getGeometryFactory().createPoint(new Coordinate(lon, lat)); // ポイント
                String scode = "SH";
                String jcode = "";
                String pref = "";
                String gun = "";
                String sikuchoson = "";

                // 所在地の市区町村マッチングを行う。
                for(SimpleFeature feature : municipalities) {
                    MultiPolygon mp = (MultiPolygon) feature.getAttribute("the_geom");
                    if(point.within(mp)) { // 所在する市区町村を見つけた時の処理
                        jcode = feature.getAttribute("JCODE").toString();
                        pref = feature.getAttribute("KEN").toString();
                        gun = feature.getAttribute("GUN").toString();
                        sikuchoson = feature.getAttribute("SEIREI").toString() + feature.getAttribute("SIKUCHOSON").toString();
                        break;
                    }
                }

                // マッチングの結果、都道府県を超えている場合
                if(!pref.equals(str[0])) {
                    pref = str[0];
                    jcode = jcodeDB.get(str[0] + str[1]);
                    gun = gunDB.get(jcode);
                    sikuchoson = sikuchosonDB.get(jcode);
                }

                // 所在地と住所の確認
                if(!str[3].startsWith(pref)) { // 住所に都道府県が含まれない場合 ReverseGeoCoiding
                    if(str[3].startsWith(sikuchoson)) {
                        str[3] = pref + str[3];
                    }
                    else {
                        BigDecimal blat = new BigDecimal(lat);
                        BigDecimal blon = new BigDecimal(lon);
                        List<GeocoderResult> results = gis.GeoCoding.getReverseGeocoderResult(blat, blon, "ja");
                        String address = results.get(0).getFormattedAddress();
                        String pair[] = address.split(pref);
                        str[3] = pref + pair[1];
                    }
                }
                else if(!str[3].contains(sikuchoson)) { // 住所に設定市区町村が含まれない場合
                    if (str[3].contains(str[1])) { // YahooDBで住所と市区町村が一致する場合
                        jcode = jcodeDB.get(str[0] + str[1]);
                        gun = gunDB.get(jcode);
                        sikuchoson = sikuchosonDB.get(jcode);
                    } else { // YahooDBで住所と市区町村が一致しない場合、ReverseGeoCooding
                        BigDecimal blat = new BigDecimal(lat);
                        BigDecimal blon = new BigDecimal(lon);
                        List<GeocoderResult> results = gis.GeoCoding.getReverseGeocoderResult(blat, blon, "ja");
                        String address = results.get(0).getFormattedAddress();
                        String pair[] = address.split(pref);
                        str[3] = pref + pair[1];
                    }
                }

                if(!str[3].contains(sikuchoson)) { // 住所に市区町村がまだ含まれない場合の最終処理→住所を正とする
                    for(String s : jcodeDB.keySet()) {
                        if(str[3].contains(s)) {
                            jcode = jcodeDB.get(s);
                            gun = gunDB.get(jcode);
                            sikuchoson = sikuchosonDB.get(jcode);
                            break;
                        }
                    }
                }

                String addressPair[] = str[3].split(sikuchoson);
                if(addressPair.length==2) {
                    str[3] = addressPair[1];
                }
                else {
                    str[3] = "";
                }

                // 避難所コードの作成
                int id;
                if(idDB.containsKey(jcode)) {
                    id = idDB.get(jcode);
                    id++;
                }
                else {
                    id = 1001; // 先頭は1001からスタート
                }
                idDB.put(jcode, id);
                scode = scode + jcode + Integer.toString(id); // 避難所コード決定



                // 「避難所コード,市区町村コード,都道府県,郡,市区町村,避難所名,住所,緯度,経度,避難所,一時避難所,広域避難所,地震,風水害,津波,収容人数,備蓄品,備考」の作成
                String s = scode + "," + jcode + "," + pref + "," + gun + "," + sikuchoson;
                for(int i = 2; i<=14; i++) {
                    if(i>(str.length-1))
                        s = s + ",";
                    else
                        s = s + "," + str[i];
                }
                pw.write("\n" + s);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
