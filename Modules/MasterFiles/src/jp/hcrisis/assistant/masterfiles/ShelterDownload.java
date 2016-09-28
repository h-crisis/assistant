package jp.hcrisis.assistant.masterfiles;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;


/**
 * Created by manabu on 2016/09/24.
 */
public class ShelterDownload {

    // Yahoo避難所の各都道府県のページ数
    public static final int[] id = {
            183, 51, 54, 49, 48, 51, 63, 51, 29, 42,
            89, 76, 72, 64, 73, 35, 35, 42, 37, 89,
            86, 29, 86, 77, 34, 42, 87, 94, 30, 63,
            32, 52, 63, 71, 39, 59, 20, 59, 47, 81,
            19, 65, 40, 46, 48, 44, 39
    };

    // 都道府県名の配列。Yahoo避難所の順番に合わせる必要がある。
    public static final String[] prefecture_name = {
            "北海道", "青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県", "茨城県", "栃木県", "群馬県",
            "埼玉県", "千葉県", "東京都", "神奈川県", "新潟県", "富山県", "石川県", "福井県", "山形県", "長野県",
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
            workingDir = new File("files/YahoSshelter");
        }
        if(!workingDir.exists()) {
            System.out.println("作業フォルダが存在しません。");
            System.exit(1);
        }
        createShelterMasterYahoo(workingDir, 1);
        combineShleterFiles(workingDir);
    }

    /**
     * Yahoo避難所から全国の避難所一覧ページを作成する
     * @param dir 出力フォルダ
     * @param start どの都道府県から始めるかの番号指定
     */
    public static void createShelterMasterYahoo(File dir, int start) {
        for(int i = start; i<=id.length; i++) {
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
}
