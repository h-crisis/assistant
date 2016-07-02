package ichikawa.ZmapArea2;

import java.io.*;

/**
 * Created by manabu on 2016/06/27.
 */
public class ReadDatFiles {
    public static void main(String args[]) throws IOException {
        File file = new File("files/ZenrinFiles/MAPDAT/JPNMAP");
        readFiles(file);
    }

    public static void readFiles(File dir) throws IOException {
        System.out.println("フォルダ内のDATファイルを探す");
        if(dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            for(int i=0; i<fileList.length; i++) {
                if(fileList[i].isFile())
                    if(isDatFile(fileList[i]))
                        readDatFile(fileList[i]);
                    else
                        ;
                else {
                    ;
                }
            }
        }
        else {
            System.out.println("\tError: 出力パスがディレクトリではありません。");
            throw new RuntimeException("Error: 出力パスがディレクトリではありません。");
        }
    }

    public static boolean isDatFile(File file) {
        if(file.getName().endsWith("DAT"))
            return true;
        else
            return false;
    }

    public static void readDatFile(File file) throws IOException {
        file.setReadOnly();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"));
        String line;
        String figNum;
        while((line = br.readLine()) != null) {
            // [HEADER]パラグラフを解析する
            if(line.equals("[HEADER]")) {
                while(line!=null) { // 空行までループ
                    if(line.startsWith("FigNum")) {
                        figNum = line;
                        System.out.println("変換するFigureの数: " + figNum);
                    }
                    else if(line.equals(""))
                        break; // パラグラフの終わりが来たら解析終了
                    line = br.readLine();
                }
            }
            // 空行の処理
            else if(line.equals(""))
                System.out.println("空行です");
            else if(line.startsWith("[")){ // 上記以外の処理　[から始まるパラグラフ
                String str = "";
                int figType = 0;
                while(line!=null) { // ファイルの終わりまでループ
                    if(line.startsWith("[")) {
                        str = line;
                    }
                    else if(line.startsWith("FIGTYPE")) {
                        String pair[] = line.split("=");
                        str = str + "\n" + line;
                        figType = Integer.parseInt(pair[1]);
                    }
                    else if(line.equals(""))
                        break;
                    else
                        str = str + "\n" + line;
                    line = br.readLine();
                }
                System.out.println(figType);
            }

        }

        br.close();
    }
}
