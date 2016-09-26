package jp.hcrisis.assistant.zenrin.area2;

import java.io.*;

/**
 * Created by manabu on 2016/06/27.
 */
public class ReadDatFiles {
    public static void main(String args[]) throws IOException {
        File jpnmapFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/ZENKOKU/MAPDAT/JPNMAP");
        File widemapFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/ZENKOKU/MAPDAT/WIDEMAP");
        File geograFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/ZENKOKU/MAPDAT/GEOGRA");
        File topogrFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/ZENKOKU/MAPDAT/TOPOGR");
        File outDir = new File("/Users/manabu/Desktop/OUT/TXT");

        File hokkaidoFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/HOKKAIDO/MAPDAT/TOWNMP");
        File tohokuFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/TOHOKU/MAPDAT/TOWNMP");
        File kantoFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/KANTO/MAPDAT/TOWNMP");
        File chubuFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/CHUBU/MAPDAT/TOWNMP");
        File kinkiFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/KINKI/MAPDAT/TOWNMP");
        File chugokushikokuFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/CHUGOKU_SHIKOKU/MAPDAT/TOWNMP");
        File kyushuFile = new File("/Volumes/My Book Pro 12TB/AreaMap2/KYUSHU_OKINAWA/MAPDAT/TOWNMP");

        //readFiles(jpnmapFile, outDir, 1);
        //readFiles(widemapFile, outDir, 2);
        //readFiles(geograFile, outDir, 3);
        //readFiles(topogrFile, outDir, 4);
        //readFiles(hokkaidoFile, outDir, 5);
        readFiles(tohokuFile, outDir, 5);
        readFiles(kantoFile, outDir, 5);
        readFiles(chubuFile, outDir, 5);
        readFiles(kinkiFile, outDir, 5);
        readFiles(chugokushikokuFile, outDir, 5);
        readFiles(kyushuFile, outDir, 5);
    }

    public static void readFiles(File dir, File outDir, int level) throws IOException {
        System.out.println(dir + "を探索します。");
        if(dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            for(int i=0; i<fileList.length; i++) {
                if(fileList[i].isFile()) {
                    if (isDatFile(fileList[i]))
                        readDatFile(fileList[i], outDir, level);
                }
                else
                    readDir(fileList[i], outDir, level);
            }
        }
        else {
            System.out.println("\tError: 出力パスがディレクトリではありません。");
            throw new RuntimeException("Error: 出力パスがディレクトリではありません。");
        }
    }

    public static void readDir(File dir, File outDir, int level) throws IOException {
        System.out.println("\t" + dir + "を探索します。");
        if(dir.listFiles()!=null) {
            File fileList[] = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile())
                    if (isDatFile(fileList[i]))
                        readDatFile(fileList[i], outDir, level);
                else {
                    readDir(fileList[i], outDir, level);
                }
            }
        }
        else
            System.out.println("\t" + dir + "はNULLでした。");
    }

    public static boolean isDatFile(File file) {
        if(file.getName().endsWith("DAT"))
            return true;
        else
            return false;
    }

    public static void readDatFile(File file, File outDir, int level) throws IOException {
        //file.setReadOnly();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"));
        String line;
        String figNum;
        while((line = br.readLine()) != null) {
            // [HEADER]パラグラフを解析する
            if(line.equals("[HEADER]")) {
                while(line!=null) { // 空行までループ
                    if(line.startsWith("FigNum")) {
                        figNum = line;
                        System.out.println("\t\t" + file.getName() + "の変換するFigureの数: " + figNum.substring(7,figNum.length()-1));
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
                int layer = 0;
                while(line!=null) { // ファイルの終わりまでループ
                    if(line.startsWith("[")) {
                        str = line;
                    }
                    else if(line.startsWith("LAYER")) {
                        String pair[] = line.split("=");
                        str = str + "\n" + line;
                        layer = Integer.parseInt(pair[1]);
                    }
                    else if(line.startsWith("DISPLEVEL")) {
                        String pair[] = line.split("=");
                        int DispLevel = Integer.parseInt(pair[1]);
                        DispLevel = DispLevel/2 + (level-1) * 5 + 1;
                        str = str + "\n" + pair[0] + "=" + Integer.toString(DispLevel);

                    }
                    else if(line.equals(""))
                        break;
                    else
                        str = str + "\n" + line;
                    line = br.readLine();
                }

                File outFile = new File(outDir.getPath() + "/" + Integer.toString(layer) + ".txt");
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, true),"Shift_JIS"));
                pw.write(str + "\n\n");
                pw.close();
            }

        }

        br.close();
    }
}
