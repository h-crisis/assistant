package jp.hcrisis.assistant.zenrin.area2;

import files.FileManagement;

import java.io.File;
import java.io.IOException;

/**
 * Created by manabu on 2016/12/22.
 */
public class Main {
    /**
     * ゼンリンAreaMap2をShapeファイルに変換するプログラム
     * @param args ゼンリンAreaMap2のルートフォルダ、出力先フォルダ、マルチスレッド数
     */
    public static void main(String args[]) throws IOException {

        //String separator = "/";
        String separator = "\\";

        if(args.length!=4) { // 引数が4つ以外の場合はエラー処理
            System.out.println("Error: Zenrin Area2 Converter needs at least four arguments. ");
            System.exit(1);
        }
        else {
            // 追記モードの確認
            Boolean doType = false;
            if(args[3].equals("true")) {
                doType = true;
            }
            else if(args[3].equals("false")) {
                doType = false;
            }
            else {
                System.out.println("Zenrin AreaMap2 Converter Error: 第4引数はtrue(追記モード)かfalse(新規モード)を記載してください。");
                System.exit(1);
            }

            // スレッド数の確認
            int threadNum = Integer.parseInt(args[2]);

            // ゼンリンデータフォルダのチェック
            File dataDir = new File(args[0]);
            if(!dataDir.exists()) {
                System.out.println("Zenrin AreaMap2 Converter Error: ゼンリン地図データフォルダが存在しません。"
                        + dataDir.getPath() + separator + dataDir.getName());
                System.exit(1);
            }

            File outDir = new File(args[1]);
            if(outDir.exists() && outDir.isDirectory()) { // 出力フォルダが存在する場合
                if(!doType) { // 新規モードの時はフォルダの中身を削除
                    FileManagement.removeFiles(outDir);
                }
            }
            else if(outDir.exists()) { // 出力ファイルが存在する場合
                System.out.println("Zenrin AreaMap2 Converter Error: 出力フォルダがファイルとして存在します。ファイルを削除するか出力フォルダを変更してください。");
                System.exit(1);
            }
            else {
                outDir.mkdir();
            }
            ReadDatFiles rdf = new ReadDatFiles(dataDir, outDir, threadNum);
            rdf.readDataFiles();
        }
    }
}
