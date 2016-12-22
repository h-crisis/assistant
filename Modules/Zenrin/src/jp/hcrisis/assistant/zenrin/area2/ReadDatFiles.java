package jp.hcrisis.assistant.zenrin.area2;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by manabu on 2016/06/27.
 */
public class ReadDatFiles {
    private File outDir;
    private int threadNum;

    private File jpnmapFile;
    private File widemapFile;
    private File geograFile;
    private File topogrFile;

    private File hokkaidoFile;
    private File tohokuFile;
    private File kantoFile;
    private File chubuFile;
    private File kinkiFile;
    private File chugokushikokuFile;
    private File kyushuFile;

    private ArrayList<File> files;

    public ReadDatFiles(File dataDir, File outDir, int threadNum) {
        this.outDir = outDir;
        this.threadNum = threadNum;

        jpnmapFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/ZENKOKU/MAPDAT/JPNMAP");
        files.add(jpnmapFile);
        widemapFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/ZENKOKU/MAPDAT/WIDEMAP");
        files.add(widemapFile);
        geograFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/ZENKOKU/MAPDAT/GEOGRA");
        files.add(geograFile);
        topogrFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/ZENKOKU/MAPDAT/TOPOGR");
        files.add(topogrFile);

        hokkaidoFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/HOKKAIDO/MAPDAT/TOWNMP");
        files.add(hokkaidoFile);
        tohokuFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/TOHOKU/MAPDAT/TOWNMP");
        files.add(tohokuFile);
        kantoFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/KANTO/MAPDAT/TOWNMP");
        files.add(kantoFile);
        chubuFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/CHUBU/MAPDAT/TOWNMP");
        files.add(chubuFile);
        kinkiFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/KINKI/MAPDAT/TOWNMP");
        files.add(kinkiFile);
        chugokushikokuFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/CHUGOKU_SHIKOKU/MAPDAT/TOWNMP");
        files.add(chugokushikokuFile);
        kyushuFile = new File(dataDir.getPath() + "/" + dataDir.getName() + "/KYUSHU_OKINAWA/MAPDAT/TOWNMP");
        files.add(kyushuFile);

        dataFileCheck();
    }

    private void dataFileCheck() {
        for(File file : files) {
            if(!file.exists()) {
                System.out.println("Zenrin AreaMap2 Converter Error: ゼンリン地図データファイルが存在しません。"
                        + file.getPath() + "/" + file.getName());
                System.exit(1);
            }
        }
    }

    public void readDataFiles() throws IOException {
        readFiles(jpnmapFile, outDir, 1);
        readFiles(widemapFile, outDir, 2);
        readFiles(geograFile, outDir, 3);
        readFiles(topogrFile, outDir, 4);
        readFiles(hokkaidoFile, outDir, 5);
        readFiles(tohokuFile, outDir, 5);
        readFiles(kantoFile, outDir, 5);
        readFiles(chubuFile, outDir, 5);
        readFiles(kinkiFile, outDir, 5);
        readFiles(chugokushikokuFile, outDir, 5);
        readFiles(kyushuFile, outDir, 5);
    }

    private static void readFiles(File dir, File outDir, int level) throws IOException {
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

    private static void readDir(File dir, File outDir, int level) throws IOException {
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

    private static boolean isDatFile(File file) {
        if(file.getName().endsWith("DAT"))
            return true;
        else
            return false;
    }

    private static void readDatFile(File file, File outDir, int level) throws IOException {
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
