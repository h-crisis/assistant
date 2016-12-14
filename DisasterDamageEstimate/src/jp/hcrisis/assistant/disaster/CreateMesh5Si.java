package jp.hcrisis.assistant.disaster;

import java.io.*;

/**
 * 5次メッシュの震度分布CSVを作成するクラス
 * Created by manabu on 2016/09/13.
 */
public class CreateMesh5Si {

    /**
     * SIP4日立から受け取る震度分布データから5次メッシュの震度分布CSVを作成するメソッド
     * @param dir データディレクトリ
     * @param outFile 出力ファイル
     */
    public static void createMesh5SiFromSIP4(File dir, File outFile) {
        File[] files = dir.listFiles(); // データファイルを取得する

        try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {
            pw.print("5次メッシュコード,震度,全壊建物数,全半壊建物数,重傷者数"); // 出力ファイルの見出しを記入する
            for(File file : files) {
                try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"))) {
                    String line = br.readLine(); // 1行目は見出しなので無視
                    while(line != null) {
                        if(line.startsWith("2,")) {
                            String pair[] = line.split(",");
                            if (pair.length == 8) {
                                pw.print("\n" + pair[7] + "," + pair[5] + ",,,");
                            } else if(pair.length == 9) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + ",,");
                            } else if(pair.length == 10) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + "," + pair[9] + ",");
                            } else if(pair.length == 11) {
                                pw.print("\n" + pair[7] + "," + pair[5] + "," + pair[8] + "," + pair[9] + "," + pair[10]);
                            } else {
                                System.out.println("項目数が足りていません。");
                                System.exit(1);
                            }
                        }
                        line = br.readLine();
                    }
                } catch (IOException e) {
                    System.out.println("入力ファイル " + file.getName() + " を開いている際にエラーが生じました。");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("出力ファイル " + outFile.getName() + " が見つかりません。");
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.out.println("出力ファイル " + outFile.getName() + " は、文字コード Shift_JISをサポートしていません。");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * JSHISの震度ファイルから5次メッシュの震度分布CSVを作成するメソッド
     * @param inFile 入力ファイル
     * @param outFile 出力ファイル
     */
    public static void createMesh5SiFromJSHIS(File inFile, File outFile) {
        try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {
            pw.print("5次メッシュコード,震度,全壊建物数,全半壊建物数,重傷者数"); // 出力ファイルの見出しを記入する
            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"))) {
                String line = br.readLine(); // 1行目は見出しなので無視
                boolean isRead = false;
                while(line != null) {
                    if(line.startsWith("# CODE")) {
                        isRead = true;
                    }
                    else if(isRead) {
                        String pair[] = line.split(",");
                        if (pair.length != 6) {
                            System.out.println("入力ファイル " + inFile.getName() + " の列数が6ではありません。");
                            System.exit(1);
                        } else {
                            pw.print("\n" + pair[0] + "," + pair[5] + ",-,-,-");
                        }
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                System.out.println("入力ファイル " + inFile.getName() + " を開いている際にエラーが生じました。");
                e.printStackTrace();
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("出力ファイル " + outFile.getName() + " が見つかりません。");
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.out.println("出力ファイル " + outFile.getName() + " は、文字コード Shift_JISをサポートしていません。");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
