package ichikawa.assistant.emis;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by manabu on 2016/07/26.
 */
public class DownloadEMIS {

    public static void main(String args[]) {
        if(args.length==2) {
            File dir = new File(args[0]);
            if(dir.exists() && dir.isDirectory())
                downloadEmisStatusFile(dir, args[1]);
            else
                System.out.println("出力先フォルダが見つかりません。");
        }
        else
            System.out.println("引数の数が違います。");
    }

    public static void downloadEmisStatusFile(File dir, String time) {
        try{
	        /* URLを構築します。引数にダウンロード先のURLを指定します。*/
            URL url = new URL("https://www.wds.emis.go.jp/dmatshiryo/docs/sip/emis-sip_" + time + ".zip");

            HttpURLConnection urlConnection=
                    (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            int num;
            byte buf[] = new byte[4096];

	        /* DataInputStreamを使用してファイルに書き出します。*/
            DataInputStream dis =
                    new DataInputStream(urlConnection.getInputStream());

            /* 保存するファイルの FileOutputStream を構築します。
            引数にファイル名を指定します。*/
            FileOutputStream fos = new FileOutputStream(dir.getPath() + "/emis-sip_" + time + ".zip");

            while((num=dis.read(buf))!=-1) {
                fos.write(buf,0,num);
            }
            dis.close();
            System.out.println("ダウンロード完了しました");
        }catch(Exception e){
            System.out.println(e + "例外が発生しました");
        }
    }
}
